package org.sudu.experiments;

import org.sudu.experiments.js.node.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

// export type SshInput = { path: string, ssh: SshCredentials }
// export type FileInput =
//      { path: string } | { content: string } | SshInput;

public interface JsFileInput extends JSObject {

  static FileHandle fileHandle(JsFileInput input, boolean mustExists) {
    boolean isString = JSString.isInstance(input);
    if (isString || hasPath(input)) {
      JSString path = isString ? input.cast() : JsHasPath.getPath(input);
      if (!isString && JsSshInput.hasSsh(input)) {
        JsSshCredentials ssh = JsSshInput.getSsh(input);
        return JSObjects.isUndefined(path) || JSObjects.isUndefined(ssh)
            ? null : new SshFileHandle(path, ssh);
      }
      return (!mustExists || Fs.isFile(path)) ?
          new NodeFileHandle(path) : null;
    }
    return null;
  }

  static boolean isPath(JSObject input) {
    return JSString.isInstance(input) || hasPath(input);
  }

  static boolean hasPath(JSObject input) {
    return JSObjects.hasProperty(input, "path");
  }

  static JSString getPath(JSObject input) {
    return JSString.isInstance(input) ? input.cast()
        : JsHasPath.getPath(input);
  }

  static boolean isContent(JSObject input) {
    return hasPath(input) &&
        JSObjects.hasProperty(input, "content");
  }

  static JSString getContent(JSObject input) {
    return input.<JsFileInput>cast().getContent();
  }

  @JSProperty
  JSString getContent();
}
