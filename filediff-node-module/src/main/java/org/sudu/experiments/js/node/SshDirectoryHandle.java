package org.sudu.experiments.js.node;

import org.sudu.experiments.JsFileInputSsh.JaSshCredentials;
import org.sudu.experiments.SshPool;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class SshDirectoryHandle extends NodeDirectoryHandle0 {
  JaSshCredentials credentials;

  public SshDirectoryHandle(String name, String[] path, JaSshCredentials cred) {
    super(name, path, sep());
    this.credentials = cred;
  }

  static JSString sep() { return JSString.valueOf("/"); }

  public SshDirectoryHandle(JSString jsPath, JaSshCredentials cred) {
    super(pathBasename(jsPath), pathDirname(jsPath), sep());
    this.credentials = cred;
  }

  static JSString pathBasename(JSString jsPath) {
    int lastIndexOf = jsPath.lastIndexOf(sep());
    return lastIndexOf < 0 ? jsPath : jsPath.slice(0, lastIndexOf);
  }

  static JSString pathDirname(JSString jsPath) {
    int lastIndexOf = jsPath.lastIndexOf(sep());
    return lastIndexOf < 0 ? null : jsPath.slice(lastIndexOf + 1);
  }

  @Override
  public void read(Reader reader) {
    Promise<SshPool.Record> connected = SshPool.connect(credentials);
    connected.then(
        connection -> {
          JsSftpClient sftp = connection.getSftp();
          JSString jsPath = jsPath();
          jsPath = JSString.valueOf("/home");
          JsHelper.consoleInfo2("sftp.readdir jsPath=", jsPath);
          sftp.readdir(jsPath, (error, list) -> {
            if (JSObjects.isUndefined(error) || error == null) {
              JsHelper.consoleInfo2("sftp.readdir completed, l = ", list);
            } else {
              JsHelper.consoleInfo2("sftp.readdir error:", error);
            }
            reader.onComplete();
          });
        },
        error -> {
          JsHelper.consoleInfo2(
              "SshDirectoryHandle.read connect error:", JsHelper.getMessage(error));
          reader.onComplete();
        }
    );
  }

  @Override
  public void copyTo(String path, Runnable onComplete, Consumer<String> onError) {
    JSString from = jsPath();
    JSString to = JSString.valueOf(path);
    JSString toParent = Fs.pathDirname(to);
  }

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {
    JSString from = jsPath();
  }
}
