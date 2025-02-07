package org.sudu.experiments;

// export type SshInput = { path: string, ssh: SshCredentials }
// export type FolderInput = string | SshInput;

import netscape.javascript.JSObject;
import org.sudu.experiments.js.node.*;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public abstract class JsFolderInput extends JSObject {

  static DirectoryHandle directoryHandle(org.teavm.jso.JSObject input) {
    if (JSString.isInstance(input)) {
      JSString localPath = input.cast();
      return Fs.isDirectory(localPath) ?
          new NodeDirectoryHandle(localPath) : null;
    }
    if (JsFileInputSsh.isInstance(input)) {
      JSString path = JsFileInputSsh.getPath(input);
      JaSshCredentials ssh = JsFileInputSsh.getSsh(input);
      return JSObjects.isUndefined(path) || JSObjects.isUndefined(ssh)
          ? null : new SshDirectoryHandle(path, ssh);
    }
    return null;
  }
}
