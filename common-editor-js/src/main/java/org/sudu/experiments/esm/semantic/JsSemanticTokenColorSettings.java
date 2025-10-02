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
  boolean getItalic();

  String italicProperty = "italic";

  default boolean hasItalic() {
    return JSObjects.hasProperty(this, italicProperty);
  }

  @JSProperty
  boolean getBold();

  String boldProperty = "bold";

  default boolean hasBold() {
    return JSObjects.hasProperty(this, boldProperty);
  }

  static String print(JsSemanticTokenColorSettings it) {
    StringBuilder sb = new StringBuilder();
    sb.append("JsSemanticTokenColorSettings{");

    if (it.hasForeground()) {
      sb.append("foreground=").append(it.getForeground().stringValue());
    }
    if (it.hasBackground()) {
      if (sb.length() > "JsSemanticTokenColorSettings{".length()) {
        sb.append(", ");
      }
      sb.append("background=").append(it.getBackground().stringValue());
    }
    if (it.hasItalic()) {
      if (sb.length() > "JsSemanticTokenColorSettings{".length()) {
        sb.append(", ");
      }
      sb.append("italic=").append(it.getItalic());
    }
    if (it.hasBold()) {
      if (sb.length() > "JsSemanticTokenColorSettings{".length()) {
        sb.append(", ");
      }
      sb.append("bold=").append(it.getBold());
    }

    sb.append("}");
    return sb.toString();
  }
}

