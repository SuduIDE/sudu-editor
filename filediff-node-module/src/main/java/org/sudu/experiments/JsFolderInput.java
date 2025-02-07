package org.sudu.experiments;

// export type SshInput = { path: string, ssh: SshCredentials }
// export type FolderInput = string | SshInput;

import org.sudu.experiments.js.node.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

// export type FolderInput = string | SshInput;
public abstract class JsFolderInput implements JSObject {

  static DirectoryHandle directoryHandle(JsFolderInput input) {
    if (JSString.isInstance(input)) {
      JSString localPath = input.cast();
      return Fs.isDirectory(localPath) ?
          new NodeDirectoryHandle(localPath) : null;
    }
    if (JsSshInput.isInstance(input)) {
      JSString path = JsHasPath.getPath(input);
      JsSshCredentials ssh = JsSshInput.getSsh(input);
      return JSObjects.isUndefined(path) || JSObjects.isUndefined(ssh)
          ? null : new SshDirectoryHandle(path, ssh);
    }
    return null;
  }
}
