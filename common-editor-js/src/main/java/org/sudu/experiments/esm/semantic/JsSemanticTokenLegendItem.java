package org.sudu.experiments.esm.semantic;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
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
  JsArray<JSString> getModifiers();

  String modifiersProperty = "modifiers";

  default boolean hasModifiers() {
    return JSObjects.hasProperty(this, modifiersProperty);
  }

  static String print(JsSemanticTokenLegendItem it) {
    StringBuilder sb = new StringBuilder();
    sb.append("JsSemanticTokenLegendItem{");
    int initL = sb.length();
    if (it.hasTokenType()) {
      sb.append("tokenType=").append(it.getTokenType().stringValue());
    }
    if (it.hasColor() && !JSObjects.isUndefined(it.getColor())) {
      if (sb.length() > "JsSemanticTokenLegendItem{".length()) {
        sb.append(", ");
      }
      sb.append("color=").append(
          JsSemanticTokenColorSettings.print(it.getColor()));
    }
    if (it.hasModifiers()) {
      if (sb.length() > initL) {
        sb.append(", ");
      }
      sb.append("modifiers={");
      JsArray<JSString> modifiers = it.getModifiers();
      for (int i = 0; i < modifiers.getLength(); i++) {
        sb.append(modifiers.get(i).stringValue());
        if (i < modifiers.getLength() - 1)
          sb.append(", ");
      }
      sb.append("}");
    }

    sb.append("}");
    return sb.toString();
  }
}
