package org.sudu.experiments.js.node;

import org.sudu.experiments.SshPool;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class SshDirectoryHandle extends NodeDirectoryHandle0 {
  static final boolean debugRead = false;

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

  static JSString pathDirname(JSString jsPath) {
    int lastIndexOf = jsPath.lastIndexOf(sep());
    return lastIndexOf < 0 ? null : jsPath.slice(0, lastIndexOf);
  }

  static JSString pathBasename(JSString jsPath) {
    int lastIndexOf = jsPath.lastIndexOf(sep());
    return lastIndexOf < 0 ? jsPath : jsPath.slice(lastIndexOf + 1);
  }

  @Override
  public void read(Reader reader) {
    SshPool.sftp(credentials,
        sftp -> {
          sftp.readdir(jsPath(), (error, list) -> {
            if (JSObjects.isUndefined(error) || error == null) {
              if (debugRead) JsHelper.consoleInfo2(
                  "sftp.readdir completed: path", jsPath(),
                      ", length =", JSNumber.valueOf(list.getLength()));
              String[] childPath = list.getLength() > 0
                  ? ArrayOp.add(path, name) : null;
              for (int i = 0, e = list.getLength(); i < e; i++) {
                JsSftpClient.DirEntry entry = list.get(i);
                String entryName = entry.getFilename().stringValue();
                if (entry.getAttrs().isDirectory()) {
                  var subDir = new SshDirectoryHandle(
                      entryName, childPath, credentials);
                  reader.onDirectory(subDir);
                } else if (entry.getAttrs().isFile()) {
                  var attrs = entry.getAttrs();
                  var file = new SshFileHandle(
                      entryName, childPath, credentials, attrs);
                  reader.onFile(file);
                }
              }
            } else {
              JsHelper.consoleInfo2("sftp.readdir error:", error);
            }
            reader.onComplete();
          });
        },
        error -> {
          JsHelper.consoleInfo2(
              "SshDirectoryHandle.read connect error:", JsHelper.getMessage(error),
              "credentials", credentials);
          reader.onComplete();
        }
    );
  }

  @Override
  public void copyTo(String path, Runnable onComplete, Consumer<String> onError) {
    JSString from = jsPath();
    JSString to = JSString.valueOf(path);
    JSString toParent = Fs.pathDirname(to);
    onError.accept("not implemented yet");
  }

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {
    JSString from = jsPath();
    onError.accept("not implemented yet");
  }
}
