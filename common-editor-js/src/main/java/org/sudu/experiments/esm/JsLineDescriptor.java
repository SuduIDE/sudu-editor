package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;

/**
 * A line of a document built by {@code newTextModelFromLines}.
 * {@code lineNumber > 0} marks a source line; its absence marks an attached
 * (for example disassembly) line. {@code tokens} are the run segments the line
 * text is made of; a plain line is a single run without a token type.
 */
public interface JsLineDescriptor extends JSObject {
  String lineNumberProperty = "lineNumber";
  String tokensProperty = "tokens";

  @JSProperty
  int getLineNumber();

  @JSProperty
  JsArray<JsLineToken> getTokens();

  default boolean hasLineNumber() {
    return JSObjects.hasProperty(this, lineNumberProperty);
  }

  default boolean hasTokens() {
    return JSObjects.hasProperty(this, tokensProperty);
  }
}
