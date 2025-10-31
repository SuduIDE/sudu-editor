package org.sudu.experiments.esm.semantic;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public interface JsSemanticToken extends JSObject {
  @JSProperty
  int getLine();

  String lineProperty = "line";

  @JSProperty
  int getStartChar();

  default boolean hasLine() {
    return JSObjects.hasProperty(this, lineProperty);
  }

  String startCharProperty = "startChar";

  @JSProperty
  int getLength();

  default boolean hasStartChar() {
    return JSObjects.hasProperty(this, startCharProperty);
  }

  String lengthProperty = "length";

  @JSProperty
  int getLegendIdx();

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
    return print(this);
  }

  static String print(JsSemanticToken it) {
    StringBuilder sb = new StringBuilder();
    sb.append("JsSemanticToken{");

    if (it.hasLine()) {
      sb.append("line=").append(it.getLine());
    }
    if (it.hasStartChar()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("startChar=").append(it.getStartChar());
    }
    if (it.hasLength()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("length=").append(it.getLength());
    }
    if (it.hasLegendIdx()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("legendIdx=").append(it.getLegendIdx());
    }
    if (it.hasText()) {
      if (sb.length() > "JsSemanticToken{".length()) {
        sb.append(", ");
      }
      sb.append("text=").append(it.getText().stringValue());
    }

    sb.append("}");
    return sb.toString();
  }
}
