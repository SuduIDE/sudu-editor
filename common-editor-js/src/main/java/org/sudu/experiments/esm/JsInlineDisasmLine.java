package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

/**
 * One descriptor of an inline-disassembly document line passed to
 * {@code newTextModelWithInline}.
 *
 * <ul>
 *   <li>{@code lineNumber} present (&gt; 0) => the line is a source line carrying this ordinal.</li>
 *   <li>{@code lineNumber} absent / 0    => the line is an attached disassembly line.</li>
 *   <li>{@code text} is the line content in both cases.</li>
 * </ul>
 *
 * See demo-edit/docs/inline-disasm-open-questions.md (Q2).
 */
public interface JsInlineDisasmLine extends JSObject {

  @JSProperty
  int getLineNumber();

  String lineNumberProperty = "lineNumber";

  default boolean hasLineNumber() {
    return JSObjects.hasProperty(this, lineNumberProperty);
  }

  @JSProperty
  JSString getText();

  String textProperty = "text";

  default boolean hasText() {
    return JSObjects.hasProperty(this, textProperty);
  }
}
