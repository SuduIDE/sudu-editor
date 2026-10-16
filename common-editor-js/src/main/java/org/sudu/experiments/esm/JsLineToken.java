package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

/**
 * One run segment of an {@link JsLineDescriptor}.
 * {@code tokenType} is a free-form name looked up in the document token dictionary;
 * when it is absent the run is plain default text.
 */
public interface JsLineToken extends JSObject {
  String textProperty = "text";
  String tokenTypeProperty = "tokenType";

  @JSProperty
  JSString getText();

  @JSProperty
  JSString getTokenType();

  default boolean hasText() {
    return JSObjects.hasProperty(this, textProperty);
  }

  default boolean hasTokenType() {
    return JSObjects.hasProperty(this, tokenTypeProperty);
  }
}
