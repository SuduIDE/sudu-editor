package org.sudu.experiments.esm;

import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsArrayReader;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.math.Color;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

/*
  export type Theme = {
      [color in ThemeColor]?: string;
  } & {
      baseTheme: BaseTheme;
  } | BaseTheme;
*/

public interface ThemeImport {

  String baseTheme = "baseTheme";

  static EditorColorScheme fromJs(JSObject t) {
    boolean isString = JSString.isInstance(t);
    boolean valid = isString || JsHelper.hasProperty(t, baseTheme);
    JSString base = isString ? t.cast() : JsHelper.getProperty(t, baseTheme);

    if (!valid || !JSString.isInstance(base)) {
      JsHelper.consoleError("import theme failed for object ", t);
      return null;
    }

    JsHelper.consoleInfo("base theme is ", base);

    var theme = ThemeControl.resolveTheme(base.stringValue());
    if (theme == null) {
      JsHelper.consoleError("resolve theme failed for ", base);
      return null;
    }

    JsArrayReader<JSString> rdr = t.cast();
    for (int i = 0; i < EditorColorScheme.LastIndex; i++) {
      JSString v = rdr.get(i);
      if (JsHelper.jsIf(v)) {
        JsHelper.consoleInfo("import color " + i + ' ', v);
        var c = new Color(v.stringValue());
        theme.modify(i, c);
      }
    }

    return theme;
  }
}
