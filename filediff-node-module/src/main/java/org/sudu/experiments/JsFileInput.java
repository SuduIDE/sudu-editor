package org.sudu.experiments;

import org.sudu.experiments.js.node.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

// export type SshInput = { path: string, ssh: SshCredentials }
// export type FileInput =
//      { path: string } | { content: string } | SshInput;

public interface JsFileInput extends JSObject {



  static FileHandle fileHandle(JSObject input, boolean mustExists) {
    if (JSString.isInstance(input)) {
      JSString localPath = input.cast();
      return (!mustExists || Fs.isFile(localPath)) ?
          new NodeFileHandle(localPath) : null;
    }
    if (JsFileInputSsh.isInstance(input)) {
      JSString path = JsFileInputSsh.getPath(input);
      JaSshCredentials ssh = JsFileInputSsh.getSsh(input);
      return JSObjects.isUndefined(path) || JSObjects.isUndefined(ssh)
          ? null : new SshFileHandle(path, ssh);
    }
    return null;
  }

  static boolean hasPath(JSObject input) {
    return JSObjects.hasProperty(input, "path");
  }
}
