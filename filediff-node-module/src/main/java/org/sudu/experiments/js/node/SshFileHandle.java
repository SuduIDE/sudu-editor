package org.sudu.experiments.js.node;

import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.SshPool;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Uint8Array;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class SshFileHandle extends NodeFileHandle0 {
  JaSshCredentials credentials;
  JsSftpClient.Attrs attrs;
  JSObject handle;

  public SshFileHandle(
      String name, String[] path,
      JaSshCredentials credentials,
      JsSftpClient.Attrs attrs
  ) {
    super(name, path, SshDirectoryHandle.sep());
    this.credentials = credentials;
    this.attrs = attrs;
  }

  public SshFileHandle(
      JSString jsPath,
      JaSshCredentials credentials,
      JsSftpClient.Attrs attrs) {
    super(jsPath,
        SshDirectoryHandle.pathBasename(jsPath),
        SshDirectoryHandle.pathDirname(jsPath),
        SshDirectoryHandle.sep());
    this.credentials = credentials;
    this.attrs = attrs;
  }

  public SshFileHandle(JSString jsPath, JaSshCredentials credentials) {
    this(jsPath, credentials, null);
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
    return
        Uint8Array.create(handle.<ArrayBufferView>cast().getBuffer());
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
              "Ssh connect to", credentials.getHost(),
              "failed, error =", JsHelper.message(error));
          LoggingJs.error(JsHelper.concat(
              JsHelper.concat("Ssh connect to ", credentials.getHost()),
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
          JsHelper.consoleInfo2("sftp.open completed handle =",
              debugHandle(), ", path=", jsPath());
          if (length <= 0 && attrs == null) {
            sftp.fstat(newHandle, (error, stats) -> {
              JsHelper.consoleInfo2("sftp.fstat completed ",
                  jsPath(), ", stats =", stats);
              if (JSObjects.isUndefined(e)) {
                this.attrs = stats;
                doRead(sftp, consumer, onError, begin, attrs.getSize());
              } else {
                onError.accept(e.getMessage());
                doClose(sftp);
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
    JsHelper.consoleInfo2("reading " + length + " bytes at " +
        begin + " from", jsPath());
    byte[] data = new byte[length];
    if (length == 0) consumer.accept(data);
    else sftp.read(handle, JsBuffer.from(data), 0, length, begin,
        (e, bytesRead, buffer, position) -> {
          if (JSObjects.isUndefined(e)) {
            JsHelper.consoleInfo2(
                "sftp.read completed path =", jsPath());
            if (bytesRead != data.length) {
              JsHelper.consoleInfo2(
                  "sftp.read bytesRead(" + bytesRead +
                      ") != data.length(" + data.length +
                      "), path", jsPath());
            }
            byte[] r = bytesRead == data.length ?
                data : Arrays.copyOf(data, bytesRead);
            consumer.accept(r);
            doClose(sftp);
          } else {
            onError.accept(e.getMessage());
          }
        });
  }

  void doClose(JsSftpClient sftp) {
    if (handle != null) {
      JsHelper.consoleInfo2("sftp.close handle ", jsPath());
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
      });
    }
  }

  @Override
  public void writeText(Object text, String encoding, Runnable onComplete, Consumer<String> onError) {
    onError.accept("unsupported operation");
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
