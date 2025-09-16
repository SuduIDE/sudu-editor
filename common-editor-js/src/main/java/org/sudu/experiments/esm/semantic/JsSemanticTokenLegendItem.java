package org.sudu.experiments.esm.semantic;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public interface JsSemanticTokenLegendItem extends JSObject {
  @JSProperty
  JSString getTokenType();

  String tokenTypeProperty = "tokenType";

  default boolean hasTokenType() {
    return JSObjects.hasProperty(this, tokenTypeProperty);
  }

  @JSProperty
  JsSemanticTokenColorSettings getColor();

  String colorProperty = "color";

  default boolean hasColor() {
    return JSObjects.hasProperty(this, colorProperty);
  }

  @JSProperty
  JSArray<JSString> getModifiers();

  String modifiersProperty = "modifiers";

  default boolean hasModifiers() {
    return JSObjects.hasProperty(this, modifiersProperty);
  }

  default String print() {
    StringBuilder sb = new StringBuilder();
    sb.append("JsSemanticTokenLegendItem{");

    if (hasTokenType()) {
      sb.append("tokenType=").append(getTokenType().toString());
    }
    if (hasColor() && getColor() != JSObjects.undefined()) {
      if (sb.length() > "JsSemanticTokenLegendItem{".length()) {
        sb.append(", ");
      }
      sb.append("color=").append(getColor().print());
    }
    if (hasModifiers()) {
      if (sb.length() > "JsSemanticTokenLegendItem{".length()) {
        sb.append(", ");
      }
      sb.append("modifiers=").append(getModifiers().toString());
    }

    sb.append("}");
    return sb.toString();
  }
}
