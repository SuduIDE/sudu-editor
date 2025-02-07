package org.sudu.experiments;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

// export type FileInput = { content: string }

public interface JsFileInputContent extends JsFileInput {
  static boolean isInstance(JSObject input) {
    return JSObjects.hasProperty(input, "content");
  }

  static JSString getContent(JSObject input) {
    return input.<JsFileInputContent>cast().getContent();
  }

  @JSProperty
  JSString getContent();
}
