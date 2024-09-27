package org.sudu.experiments;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

// export type FileInput = { path: string }

public interface JsFileInputFile extends JSObject {
  static boolean isInstance(JSObject input) {
    return JSObjects.hasProperty(input, "path");
  }

  static JSString getPath(JSObject input) {
    return input.<JsFileInputFile>cast().getPath();
  }

  @JSProperty
  JSString getPath();
}
