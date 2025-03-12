package org.sudu.experiments.js.node;

import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.GbkEncoding;
import org.sudu.experiments.encoding.GbkEncodingJs;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Uint8Array;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class SshFileHandle extends NodeFileHandle0 {

  static final boolean debugOpenClose = false;
  static final boolean debugHandle = false;
  static final boolean debugReadWrite = false;
  static final String errorTooLarge = "file is too large";

  SshHash credentials;
  JsSftpClient.Attrs attrs;
  JSObject handle;

  public SshFileHandle(
      String name, String[] path,
      SshHash key,
      JsSftpClient.Attrs attrs
  ) {
    super(name, path, SshDirectoryHandle.sep());
    this.credentials = key;
    this.attrs = attrs;
  }

  public SshFileHandle(
      JSString jsPath,
      SshHash credentials,
      JsSftpClient.Attrs attrs
  ) {
    super(jsPath,
        SshDirectoryHandle.pathBasename(jsPath),
        SshDirectoryHandle.pathDirname(jsPath),
        SshDirectoryHandle.sep());
    this.credentials = credentials;
    this.attrs = attrs;
  }

  public SshFileHandle(JSString jsPath, JsSshCredentials c) {
    this(jsPath, new SshHash(c), null);
  }

  @Override
  public void syncAccess(Consumer<SyncAccess> consumer, Consumer<String> onError) {
    onError.accept("unsupported operation");
  }

  @Override
  public boolean hasSyncAccess() {
    return false;
  }

  JSObject debugHandle() {
    if (handle == null) return JSString.valueOf("null");
    ArrayBufferView abv = handle.cast();
    return Uint8Array.create(abv.getBuffer());
  }

  @Override
  public void getSize(IntConsumer result, Consumer<String> onError) {
    if (attrs != null) {
      postSize(result, onError, null);
    } else {
      fetchStats(r -> postSize(result, onError, r));
    }
  }

  private void postSize(IntConsumer result, Consumer<String> onError, JSError error) {
    if (error != null || attrs == null) {
      onError.accept(error != null ?
          error.getMessage() : "cant read attributes");
    } else {
      double size = attrs.getSize();
      int iLength = (int) size;
      if (iLength == size) {
        result.accept(iLength);
      } else {
        onError.accept(errorTooLarge);
      }
    }
  }

  interface OnResult {
    void f(JSError error);
  }

  void fetchStats(OnResult onResult) {
    SshPool.sftp(credentials, sftp -> {
        sftp.stat(jsPath(), (error, stats) -> {
          if (!JSObjects.isUndefined(stats)) {
            attrs = stats;
            onResult.f(null);
          } else {
            JsHelper.consoleInfo2(
                "sftp.stats error", JsHelper.message(error));
            LoggingJs.error(JsHelper.concat(
                "sftp.stats error", JsHelper.message(error)));
            onResult.f(error);
          }
        });
      },
      error -> {
        JsHelper.consoleInfo2(
            "Ssh connect to", credentials.host,
            "failed, error =", JsHelper.message(error));
        LoggingJs.error(JsHelper.concat(
            JsHelper.concat("Ssh connect to ", credentials.host),
            JsHelper.concat(" failed, error = ", JsHelper.message(error)))
        );
        onResult.f(error);
      }
    );
  }

  @Override
  public void readAsBytes(
      Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length
  ) {
    SshPool.sftp(credentials, sftp -> {
      sftp.open(jsPath(), OPEN_MODE.read(), (e, newHandle) -> {
        if (JSObjects.isUndefined(e)) {
          handle = newHandle;
          if (debugOpenClose) JsHelper.consoleInfo2(
              "sftp.open completed path=", jsPath());
          if (debugHandle) JsHelper.consoleInfo2(
              "  handle =", debugHandle());
          if (length <= 0 && attrs == null) {
            sftp.fstat(newHandle, (error, stats) -> {
              if (debugOpenClose) JsHelper.consoleInfo2("sftp.fstat completed ",
                  jsPath(), ", stats =", stats);
              if (JSObjects.isUndefined(e)) {
                this.attrs = stats;
                doRead(sftp, consumer, onError, begin, attrs.getSize());
              } else {
                onError.accept(e.getMessage());
                doClose(sftp, null);
              }
            });
          } else {
            double toRead = length > 0 ? length : attrs.getSize();
            doRead(sftp, consumer, onError, begin, toRead);
          }
        } else {
          JsHelper.consoleInfo2(
              "sftp.open error: path=", jsPath(), "error=", e);
          onError.accept(e.getMessage());
        }
      });
    }, JsHelper.wrapError("sftp error ", onError));
  }

  void doRead(
      JsSftpClient sftp,
      Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, double length
  ) {
    if (debugReadWrite) JsHelper.consoleInfo2(
        "reading ", JSNumber.valueOf(length),
         "bytes at " + begin + ", path", jsPath());
    int iLength = (int) length;
    if (iLength != length) {
      onError.accept(errorTooLarge);
      return;
    }
    byte[] data = new byte[iLength];
    if (length == 0) consumer.accept(data);
    else sftp.read(handle, JsBuffer.from(data), 0, iLength, begin,
        (e, bytesRead, buffer, position) -> {
          if (JSObjects.isUndefined(e)) {
            if (debugReadWrite) JsHelper.consoleInfo2(
                "sftp.read completed path =", jsPath(),
                ", bytesRead=" + bytesRead + ", pos",
                JSNumber.valueOf(begin));
            byte[] r = bytesRead == data.length ?
                data : Arrays.copyOf(data, bytesRead);
            consumer.accept(r);
            doClose(sftp, null);
          } else {
            onError.accept(e.getMessage());
          }
        });
  }

  void doClose(JsSftpClient sftp, Runnable onComplete) {
    if (handle != null) {
      if (debugOpenClose) JsHelper.consoleInfo2("sftp.close handle ", jsPath());
      var h = handle;
      handle = null;
      sftp.close(h, jsError -> {
        if (!JSObjects.isUndefined(jsError)) {
          var s = JsHelper.concat(
              JsHelper.concat("sftp.close error: path=", jsPath()),
              JsHelper.concat(",error =", JsHelper.message(jsError)));
          JsHelper.consoleError(s);
          LoggingJs.error(s);
        }
        if (onComplete != null) onComplete.run();
      });
    }
  }

  @Override
  public void writeText(
      Object text, String encoding,
      Runnable onComplete, Consumer<String> onError
  ) {
    boolean gbk = FileEncoding.gbk.equals(encoding);
    byte[] data = writeObject(text, gbk);
    if (data == null) {
      onError.accept("bad input text");
      return;
    }

    write0(onComplete, onError, data, OPEN_MODE.write_or_create(), 0);
  }

  @Override
  public void writeAppend(
      int filePosition, byte[] data,
      Runnable onComplete, Consumer<String> onError
  ) {
    write0(onComplete, onError, data, OPEN_MODE.append(), filePosition);
  }

  void write0(
      Runnable onComplete, Consumer<String> onError,
      byte[] data, int flags, int position
  ) {
    var we = JsHelper.wrapError("sftp error ", onError);
    SshPool.sftp(credentials, sftp -> sftp.open(
        jsPath(), flags, (e, newHandle) -> {
          if (JSObjects.isUndefined(e)) {
            handle = newHandle;
            if (debugOpenClose) JsHelper.consoleInfo2(
                "sftp.open(" + Integer.toHexString(flags) +
                    ", pos " + position +
                    ") completed, path=", jsPath());
            if (debugHandle) JsHelper.consoleInfo2(
                "  handle =", debugHandle());
            attrs = null;
            sftp.write(handle,
                JsBuffer.from(data), 0, data.length, position,
                error -> {
                  if (JSObjects.isUndefined(error)) {
                    if (debugReadWrite) {
                      JsHelper.consoleInfo2("sftp.write(" +
                          Integer.toHexString(flags) +
                          ", pos " + position + ") ok, " +
                          "path=", jsPath());
                    }
                    doClose(sftp, onComplete);
                  } else {
                    onError.accept("sftp.write error ".concat(error.getMessage()));
                    doClose(sftp, null);
                  }
                });
          } else {
            we.f(e);
          }
        }
    ), we);
  }

  static byte[] writeObject(Object text, boolean gbk) {
    JSObject jsText = JsHelper.directJavaToJs(text);
    if (JSString.isInstance(jsText))
      return gbk ? GbkEncodingJs.encode(jsText.cast())
          : TextEncoder.toUtf8(jsText.cast());

    if (text instanceof String s) {
      return gbk ? GbkEncoding.encode(s) :
          s.getBytes(StandardCharsets.UTF_8);
    }

    if (text instanceof char[] chars)
      return gbk ? GbkEncoding.encode(chars) :
          // todo: we can improve it by direct Utf16 -> Utf8 encoding
          new String(chars).getBytes(StandardCharsets.UTF_8);
    if (text instanceof byte[] bytes)
      return bytes;

    return null;
  }

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {
    var we = JsHelper.wrapError("sftp file remove error ", onError);
    SshPool.sftp(credentials, sftp -> sftp.unlink(jsPath(),
        error -> {
          if (JSObjects.isUndefined(error))
            onComplete.run();
          else
            we.f(error);
        }), we);
  }

  @Override
  public void stat(BiConsumer<Stats, String> cb) {
    fetchStats(error -> {
      if (error != null || attrs == null) {
        cb.accept(null, error != null ?
            error.getMessage() : "cant read attributes");
      } else {
        cb.accept(new Stats(
            attrs.isDirectory(),
            attrs.isFile(),
            attrs.isSymbolicLink(),
            attrs.getSize()), null);
      }
    });
  }
}
