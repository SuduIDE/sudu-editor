package org.sudu.experiments.js.node;

import org.sudu.experiments.FsItem;
import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.GbkEncoding;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.js.TextDecoder;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import static org.sudu.experiments.encoding.GbkEncodingJs.*;

public class NodeFileHandle extends NodeFileHandle0 {

  NodeFs.Stats stats;

  public NodeFileHandle(String name, String[] path) {
    super(name, path, Fs.pathSep());
  }

  public NodeFileHandle(JSString jsPath) {
    this(jsPath, null);
  }

  public NodeFileHandle(JSString jsPath, NodeFs.Stats stats) {
    super(jsPath,
        Fs.pathBasename(jsPath),
        Fs.pathDirname(jsPath),
        Fs.pathSep());
    this.stats = stats;
  }

  @Override
  public void getSize(DoubleConsumer result, Consumer<String> onError) {
    NodeFs.Stats stats = statsCache();
    result.accept(stats.size());
  }

  private NodeFs.Stats statsCache() {
    return stats == null ? actualStats() : stats;
  }

  NodeFs.Stats actualStats() {
    return stats = Fs.fs().lstatSync(jsPath());
  }

  @Override
  public void syncAccess(Consumer<SyncAccess> consumer, Consumer<String> onError) {
    try {
      int handle = openSyncRead(Fs.fs());
      consumer.accept(new NodeSyncAccess(handle, jsPath()));
    } catch (Exception e) {
      onError.accept(e.getMessage());
    }
  }

  @Override
  public void readAsBytes(
      Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length
  ) {
    double jsSize = (length < 0 ? actualStats() : statsCache()).size();
    int fileSize = (int) jsSize;

    if (begin <= fileSize) {
      if (length < 0) length = fileSize;
      else length = Math.min(length, fileSize - begin);
      if (length > 0) {
        doRead(consumer, onError, begin, length);
      } else {
        consumer.accept(new byte[0]);
      }
    } else {
      onError.accept("");
    }
  }

  private void doRead(Consumer<byte[]> consumer, Consumer<String> onError, int begin, int length) {
    Fs fs = Fs.fs();
    try {
      int h = openSyncRead(fs);
      byte[] bytes = new byte[length];
      int numRead = fs.readSync(h, JsMemoryAccess.uInt8View(bytes), 0, length, begin);
      fs.closeSync(h);
      if (numRead != bytes.length) {
        JsHelper.consoleError("read file error, numRead != bytes.length: ", jsPath());
        bytes = Arrays.copyOf(bytes, numRead);
      }
      consumer.accept(bytes);
    } catch (Exception e) {
      onError.accept(e.getMessage());
    }
  }

  private int openSyncRead(Fs fs) {
    return fs.openSync(jsPath(), fs.constants().O_RDONLY());
  }

  private int openSyncWriteAppend(Fs fs) {
    return fs.openSync(jsPath(), fs.constants().O_APPEND());
  }

  @Override
  public void writeText(
      Object text, String encoding,
      Runnable onComplete, Consumer<String> onError
  ) {
    boolean gbk = FileEncoding.gbk.equals(encoding);
    JSObject writeObject = writeObject(text, gbk);

    if (writeObject != null) {
      Fs.fs().writeFile(jsPath(), writeObject, JSString.valueOf("utf-8"),
          NodeFs.callback(onComplete, onError)
      );
      stats = null;
    } else {
      onError.accept("bad input text");
    }
  }

  @Override
  public void writeAppend(int filePosition, byte[] data, Runnable onComplete, Consumer<String> onError) {
    Fs fs = Fs.fs();
    try {
      var s = actualStats();
      int h = openSyncWriteAppend(fs);
      int written = fs.writeSync(h, JsMemoryAccess.uInt8View(data),
          0, data.length, s.size());
      fs.closeSync(h);
      stats = null;
      if (written < data.length) {
        onError.accept("written " + written + " < data.length " + data.length);
      } else {
        onComplete.run();
      }
    } catch (Exception e) {
      onError.accept(e.getMessage());
    }
  }

  static JSObject writeObject(Object text, boolean gbk) {
    // todo: we can improve it by direct Utf16 -> Utf8 encoding
    if (text instanceof char[] chars)
      return gbk ? JsMemoryAccess.bufferView(GbkEncoding.encode(chars))
          : TextDecoder.decodeUTF16(chars);
    if (text instanceof String s)
      return gbk ? JsMemoryAccess.bufferView(GbkEncoding.encode(s))
          : TextDecoder.decodeUTF16(s.toCharArray());
    if (text instanceof byte[] bytes)
      return JsMemoryAccess.bufferView(bytes);

    JSObject jsText = JsHelper.directJavaToJs(text);
    if (JSString.isInstance(jsText))
      return gbk ? jsStringToGbk(jsText.cast()) : jsText;

    return null;
  }

  @Override
  public boolean canCopyTo(FsItem dst) {
    return dst instanceof NodeDirectoryHandle
        || dst instanceof NodeFileHandle;
  }

  @Override
  public void copyTo(
      FsItem dest,
      Runnable onComplete, Consumer<String> onError
  ) {
    JSString from = jsPath(), to, toParent;

    if (dest instanceof NodeFileHandle file) {
      to = file.jsPath();
      toParent = Fs.pathDirname(to);
    } else if (dest instanceof NodeDirectoryHandle dir) {
      toParent = dir.jsPath();
      to = Fs.concatPath(toParent, dir.sep, JSString.valueOf(name));
    } else {
       throw new IllegalArgumentException("copyTo: bad dest: " + dest);
    }

    doCopy(onComplete, onError, from, to, toParent);
  }

  static void doCopy(
      Runnable onComplete, Consumer<String> onError,
      JSString from, JSString to, JSString toParent
  ) {
    Fs fs = Fs.fs();

    if (!fs.existsSync(toParent)) {
      fs.mkdirSync(toParent, Fs.mkdirOptions(true));
    }

    if (true) JsHelper.consoleInfo(
        JsHelper.concat(
            JsHelper.concat("file copy: ", from),
            JsHelper.concat(" -> ", to)));

    if (fs.existsSync(to)) {
      try {
        fs.unlinkSync(to);
      } catch (Exception e) {
        onError.accept(e.getMessage());
      }
    }
    try {
      Fs.fs().copyFileSync(from, to, 0);
      onComplete.run();
    } catch (Exception e) {
      String message = e.getMessage();
      onError.accept(message);
    }
  }

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {
    JSString path = jsPath();
    try {
      Fs.fs().unlinkSync(path);
      onComplete.run();
    } catch (Exception e) {
      onError.accept(e.getMessage());
    }
  }

  @Override
  public void stat(BiConsumer<Stats, String> cb) {
    try {
      NodeFs.Stats actualStats = actualStats();
      cb.accept(new Stats(
          actualStats.isDirectory(),
          actualStats.isFile(),
          actualStats.isSymbolicLink(),
          actualStats.size()), null);
    } catch (Exception e) {
      cb.accept(null, e.getMessage());
    }
  }
}

