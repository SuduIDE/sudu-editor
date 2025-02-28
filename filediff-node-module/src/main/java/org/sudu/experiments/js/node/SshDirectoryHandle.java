package org.sudu.experiments.js.node;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.function.Consumer;

public class SshDirectoryHandle extends NodeDirectoryHandle0 {
  static final boolean debugRead = false;

  SshHash credentials;

  public SshDirectoryHandle(String name, String[] path, SshHash cred) {
    super(name, path, sep());
    this.credentials = cred;
  }

  static JSString sep() { return JSString.valueOf("/"); }

  public SshDirectoryHandle(JSString jsPath, SshHash cred) {
    super(pathBasename(jsPath), pathDirname(jsPath), sep());
    this.credentials = cred;
  }

  public SshDirectoryHandle(JSString jsPath, JsSshCredentials cred) {
    this(jsPath, new SshHash(cred));
  }

  static JSString pathDirname(JSString jsPath) {
    int lastIndexOf = jsPath.lastIndexOf(sep());
    return lastIndexOf < 0 ? null : jsPath.slice(0, lastIndexOf);
  }

  static JSString pathBasename(JSString jsPath) {
    int lastIndexOf = jsPath.lastIndexOf(sep());
    return lastIndexOf < 0 ? jsPath : jsPath.slice(lastIndexOf + 1);
  }

  public String getFullPathWithHost() {
    return credentials.host.toString() + getFullPath();
  }

  @Override
  public void read(Reader reader) {
    SshPool.sftp(credentials,
        sftp -> sftp.readdir(jsPath(), (error, list) -> {
          if (JSObjects.isUndefined(error)) {
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
            reader.onComplete();
          } else {
            JsHelper.consoleInfo2("sftp.readdir error:", error);
            reader.onError(error.getMessage());
          }
        }),
        error -> {
          dumpError("SshDirectoryHandle.read", "connect", error);
          reader.onError(error.getMessage());
        }
    );
  }

  static final String removeDirectory = "SshDirectoryHandle.remove";

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {
    SshPool.sftp(credentials, sftp -> sftp.rmdir(
        jsPath(),
            error -> {
              if (JSObjects.isUndefined(error)) {
                onComplete.run();
              } else {
                onError.accept(error.getMessage());
              }
            }
        ), cError -> {
          dumpError(removeDirectory, "connect", cError);
          onError.accept(cError.getMessage());
        }
    );
  }

  @Override
  public FileHandle createFileHandle(String name) {
    return new SshFileHandle(name, childPath(), credentials, null);
  }

  static final String createDirectory = "SshDirectoryHandle.createDirectory";

  @Override
  public void createDirectory(
      String name,
      Consumer<DirectoryHandle> onComplete, Consumer<String> onError
  ) {
    JSString child = Fs.concatPath(jsPath(), sep, JSString.valueOf(name));
    SshPool.sftp(credentials, sftp -> sftp.mkdir(
            child,
            error -> {
              if (JSObjects.isUndefined(error)) {
                onComplete.accept(
                    new SshDirectoryHandle(name, childPath(), credentials));
              } else {
                onError.accept(error.getMessage());
              }
            }
        ), cError -> {
          dumpError(createDirectory, "connect", cError);
          onError.accept(cError.getMessage());
        }
    );
  }

  private void dumpError(String h, String ctx, JSError error) {
    JsHelper.consoleInfo2(h,
        JSString.valueOf(ctx), "error:", JsHelper.getMessage(error),
        "host:", credentials.host);
  }

  @Override
  public String toString() {
    return FsItem.toString("ssh://" + credentials.host.toString(),
        path, name, false);
  }
}
