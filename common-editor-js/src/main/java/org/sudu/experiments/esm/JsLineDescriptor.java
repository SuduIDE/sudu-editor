package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public interface JsLineDescriptor extends JSObject {
  String lineNumberProperty = "lineNumber";
  String contentProperty = "content";

  @JSProperty
  int getLineNumber();

  @JSProperty
  JSString getContent();

  default boolean hasLineNumber() {
    return JSObjects.hasProperty(this, lineNumberProperty);
  }

  default boolean hasContent() {
    return JSObjects.hasProperty(this, contentProperty);
  }
}
