package org.sudu.experiments.js.node;

import org.sudu.experiments.JaSshCredentials;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.SshPool;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class SshFileHandle extends NodeFileHandle0 {
  JaSshCredentials credentials;
  JsSftpClient.Attrs attrs;

  public SshFileHandle(
      String name, String[] path,
      JaSshCredentials credentials,
      JsSftpClient.Attrs attrs
  ) {
    super(name, path, SshDirectoryHandle.sep());
    this.credentials = credentials;
    this.attrs = attrs;
  }

  public SshFileHandle(JSString jsPath, JaSshCredentials credentials) {
    super(jsPath,
        SshDirectoryHandle.pathBasename(jsPath),
        SshDirectoryHandle.pathDirname(jsPath),
        SshDirectoryHandle.sep());
    this.credentials = credentials;
  }

  @Override
  public void syncAccess(Consumer<SyncAccess> consumer, Consumer<String> onError) {
    onError.accept("unsupported operation");
  }

  @Override
  public boolean hasSyncAccess() {
    return false;
  }

  @Override
  public void getSize(IntConsumer result) {
    if (attrs != null) {
      result.accept(attrs.getSize());
    } else {
      SshPool.connect(credentials).then(r -> {
          r.getSftp().stat(jsPath(), (error, stats) -> {
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
  public void readAsText(Consumer<String> consumer, Consumer<String> onError) {

  }

  @Override
  public void writeText(Object text, String encoding, Runnable onComplete, Consumer<String> onError) {

  }

  @Override
  public void copyTo(String path, Runnable onComplete, Consumer<String> onError) {

  }

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {

  }

  @Override
  public void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError, int begin, int length) {

  }
}
