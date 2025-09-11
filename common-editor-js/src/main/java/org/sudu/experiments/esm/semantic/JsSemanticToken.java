package org.sudu.experiments.esm.semantic;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public interface JsSemanticToken extends JSObject {
  @JSProperty
  JSString getLine();

  String lineProperty = "line";

  @JSProperty
  JSNumber getStartChar();

  default boolean hasLine() {
    return JSObjects.hasProperty(this, lineProperty);
  }

  String startCharProperty = "startChar";

  @JSProperty
  JSNumber getLength();

  default boolean hasStartChar() {
    return JSObjects.hasProperty(this, startCharProperty);
  }

  String lengthProperty = "length";

  @JSProperty
  JSNumber getLegendIdx();

  default boolean hasLength() {
    return JSObjects.hasProperty(this, lengthProperty);
  }

  String legendIdxProperty = "legendIdx";

  @JSProperty
  JSString getText();

  default boolean hasLegendIdx() {
    return JSObjects.hasProperty(this, legendIdxProperty);
  }

  String textProperty = "text";

  default boolean hasText() {
    return JSObjects.hasProperty(this, textProperty);
  }

  default String print() {
    StringBuilder sb = new StringBuilder();
    sb.append("JsSemanticToken{");

    if (hasLine()) {
      sb.append("line=").append(getLine().toString());
    }
    if (hasStartChar()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("startChar=").append(getStartChar().toString());
    }
    if (hasLength()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("length=").append(getLength().toString());
    }
    if (hasLegendIdx()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("legendIdx=").append(getLegendIdx().toString());
    }
    if (hasText()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("text=").append(getText().toString());
    }

    sb.append("}");
    return sb.toString();
  }
}
