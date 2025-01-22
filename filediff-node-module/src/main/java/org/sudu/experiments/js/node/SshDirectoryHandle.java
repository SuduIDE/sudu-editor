package org.sudu.experiments.js.node;

import org.sudu.experiments.JsFileInputSsh.JaSshCredentials;
import org.sudu.experiments.LoggingJs;
import org.sudu.experiments.SshPool;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class SshDirectoryHandle extends NodeDirectoryHandle0 {
  JaSshCredentials credentials;

  public SshDirectoryHandle(String name, String[] path, JaSshCredentials cred) {
    super(name, path);
    this.credentials = cred;
  }

  public SshDirectoryHandle(JSString jsPath, JaSshCredentials cred) {
    super(jsPath);
    this.credentials = cred;
  }

  @Override
  public void read(Reader reader) {
    Promise<SshPool.Record> connected = SshPool.connect(credentials);
    connected.then(
        connection -> {
          JsSftpClient sftp = connection.getSftp();

        },
        error -> LoggingJs.error(
            JsHelper.concat("", JsHelper.getMessage(error))
        )
    );
    reader.onComplete();
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
