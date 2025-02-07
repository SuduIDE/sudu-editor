package org.sudu.experiments.js.node;

import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.GbkEncoding;
import org.sudu.experiments.encoding.GbkEncodingJs;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Uint8Array;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class SshFileHandle extends NodeFileHandle0 {

  static final boolean debugOpenClose = false;
  static final boolean debugRead = false;

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
  public void getSize(IntConsumer result) {
    if (attrs != null) {
      result.accept(attrs.getSize());
    } else {
      SshPool.sftp(credentials, sftp -> {
          sftp.stat(jsPath(), (error, stats) -> {
            if (!JSObjects.isUndefined(stats)) {
              attrs = stats;
              result.accept(stats.getSize());
            } else {
              JsHelper.consoleInfo2(
                  "sftp.stats error", JsHelper.message(error));
              LoggingJs.error(JsHelper.concat(
                  "sftp.stats error", JsHelper.message(error)));
              result.accept(0);
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
          result.accept(0);
        }
      );
    }
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
              "sftp.open completed handle =", debugHandle(),
              ", path=", jsPath());
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
            int toRead = length > 0 ? length : attrs.getSize();
            doRead(sftp, consumer, onError, begin, toRead);
          }
        } else {
          JsHelper.consoleInfo2(
              "sftp.open error: path=", jsPath(), "error=", e);
          onError.accept(e.getMessage());
        }
      });
    }, JsHelper.wrapError("sftp.open error ", onError));
  }

  void doRead(
      JsSftpClient sftp,
      Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length
  ) {
    if (debugRead) JsHelper.consoleInfo2("reading " + length + " bytes at " +
        begin + " from", jsPath());
    byte[] data = new byte[length];
    if (length == 0) consumer.accept(data);
    else sftp.read(handle, JsBuffer.from(data), 0, length, begin,
        (e, bytesRead, buffer, position) -> {
          if (JSObjects.isUndefined(e)) {
            if (debugRead) JsHelper.consoleInfo2(
                "sftp.read completed path =", jsPath(),
                ", bytesRead=", JSNumber.valueOf(bytesRead));
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

    var we = JsHelper.wrapError("sftp.open error ", onError);
    SshPool.sftp(credentials, sftp -> sftp.open(
        jsPath(), OPEN_MODE.write_or_create(), (e, newHandle) -> {
          if (JSObjects.isUndefined(e)) {
            handle = newHandle;
            if (debugOpenClose) JsHelper.consoleInfo2(
                "sftp.open_for_write completed handle =", debugHandle(),
                ", path=", jsPath());
            sftp.write(handle,
                JsBuffer.from(data), 0, data.length, 0,
                error -> {
                  if (JSObjects.isUndefined(error)) {
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
  public void copyTo(String path, Runnable onComplete, Consumer<String> onError) {
    onError.accept("unsupported operation");
  }

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {
    onError.accept("unsupported operation");
  }
}
