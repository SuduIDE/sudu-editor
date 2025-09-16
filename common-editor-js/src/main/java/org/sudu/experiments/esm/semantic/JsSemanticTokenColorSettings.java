package org.sudu.experiments.esm.semantic;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public interface JsSemanticTokenColorSettings extends JSObject {

  @JSProperty
  JSString getForeground();

  String foregroundProperty = "foreground";

  default boolean hasForeground() {
    return JSObjects.hasProperty(this, foregroundProperty);
  }

  @JSProperty
  JSString getBackground();

  String backgroundProperty = "background";

  default boolean hasBackground() {
    return JSObjects.hasProperty(this, backgroundProperty);
  }

  @JSProperty
  JSBoolean getItalic();

  String italicProperty = "italic";

  default boolean hasItalic() {
    return JSObjects.hasProperty(this, italicProperty);
  }

  @JSProperty
  JSBoolean getBold();

  String boldProperty = "bold";

  default boolean hasBold() {
    return JSObjects.hasProperty(this, boldProperty);
  }

  default String print() {
    StringBuilder sb = new StringBuilder();
    sb.append("JsSemanticTokenColorSettings{");

    if (hasForeground()) {
      sb.append("foreground=").append(getForeground().toString());
    }
    if (hasBackground()) {
      if (sb.length() > "JsSemanticTokenColorSettings{".length()) {
        sb.append(", ");
      }
      sb.append("background=").append(getBackground().toString());
    }
    if (hasItalic()) {
      if (sb.length() > "JsSemanticTokenColorSettings{".length()) {
        sb.append(", ");
      }
      sb.append("italic=").append(getItalic().toString());
    }
    if (hasBold()) {
      if (sb.length() > "JsSemanticTokenColorSettings{".length()) {
        sb.append(", ");
      }
      sb.append("bold=").append(getBold().toString());
    }

    sb.append("}");
    return sb.toString();
  }
}

