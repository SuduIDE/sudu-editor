package org.sudu.experiments.js.node;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.GbkEncoding;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.js.TextDecoder;
import org.sudu.experiments.js.TextEncoder;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class NodeFileHandle implements FileHandle {

  final String name;
  final String[] path;
  JSString jsPath;
  NodeFs.Stats stats;

  public NodeFileHandle(String name, String[] path) {
    this.name = name;
    this.path = path;
  }

  public NodeFileHandle(JSString jsPath) {
    this(jsPath, null);
  }

  public NodeFileHandle(JSString jsPath, NodeFs.Stats stats) {
    this.jsPath = jsPath;
    this.stats = stats;
    this.name = Fs.pathBasename(jsPath).stringValue();
    this.path = new String[] {
        Fs.pathDirname(jsPath).stringValue()
    };
  }

  @Override
  public void getSize(IntConsumer result) {
    result.accept(intSize());
  }

  JSString jsPath() {
    if (jsPath == null)
      jsPath = Fs.concatPath(name, path);
    return jsPath;
  }

  private NodeFs.Stats stats() {
    return stats == null ? stats = actualStats(jsPath()) : stats;
  }

  // For actual size() value
  static NodeFs.Stats actualStats(JSString jsPath) {
    return Fs.fs().lstatSync(jsPath);
  }

  private int intSize() {
    double jsSize = actualStats(jsPath()).size();
    int result = (int) jsSize;
    if (result != jsSize) {
      JsHelper.consoleError(
          "File is too large: " + name + ", size = ",
          JSNumber.valueOf(jsSize));
      return 0;
    }
    return result;
  }

  @Override
  public void syncAccess(Consumer<SyncAccess> consumer, Consumer<String> onError) {
    try {
      int handle = openSync(Fs.fs());
      consumer.accept(new NodeSyncAccess(handle, jsPath()));
    } catch (Exception e) {
      onError.accept(e.getMessage());
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String[] getPath() {
    return path;
  }

  @Override
  public void readAsBytes(
      Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length
  ) {
    int fileSize = intSize();
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
      int h = openSync(fs);
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

  private int openSync(Fs fs) {
    return fs.openSync(jsPath(), fs.constants().O_RDONLY());
  }

  @Override
  public void writeText(
      Object text, String encoding,
      Runnable onComplete, Consumer<String> onError
  ) {
    boolean gbk = FileEncoding.gbk.equals(encoding);
    JSObject jsText = JsHelper.directJavaToJs(text);

    // todo: we can improve it by direct Utf16 -> Utf8 encoding
    if (text instanceof char[] chars)
      jsText = TextDecoder.decodeUTF16(chars);

    if (JSString.isInstance(jsText)) {
      if (gbk) {
        jsText = JsMemoryAccess.bufferView(
            GbkEncoding.encode(TextEncoder.toCharArray(jsText.cast())));
      }
    } else if (text instanceof String string) {
      jsText = gbk ? JsMemoryAccess.bufferView(GbkEncoding.encode(string))
          : JSString.valueOf(string);
    } else if (text instanceof byte[] bytes) {
      jsText = JsMemoryAccess.bufferView(bytes);
    }
    Fs.fs().writeFile(jsPath(), jsText, JSString.valueOf("utf-8"),
        NodeFs.callback(onComplete, onError)
    );
  }

  @Override
  public void copyTo(String path, Runnable onComplete, Consumer<String> onError) {
    var from = jsPath();
    var to = JSString.valueOf(path);
    JSString toParent = Fs.pathDirname(to);
    Fs fs = Fs.fs();

//    if (debug) JsHelper.consoleInfo(
//        JsHelper.concat(
//            JsHelper.concat("file copy: ", from),
//            JsHelper.concat(" -> ", to)));

    if (!fs.existsSync(toParent)) {
      fs.mkdirSync(toParent, Fs.mkdirOptions(true));
    }

    if (fs.existsSync(to)) {
      try {
        fs.unlinkSync(to);
      } catch (Exception e) {
        onError.accept(e.getMessage());
      }
    }

    doCopy(onComplete, onError, from, to);
  }

  static void doCopy(Runnable onComplete, Consumer<String> onError, JSString from, JSString to) {
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
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name) * 31 + Arrays.hashCode(path);
  }

  public boolean isFile() {
    return stats().isFile();
  }
}

