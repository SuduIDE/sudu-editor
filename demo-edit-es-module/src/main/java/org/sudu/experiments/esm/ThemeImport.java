package org.sudu.experiments.esm;

import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

/*

  export const enum ThemeColor {
      TreeViewBackground = 0,
      DefaultForeground = 1,
      SelectedItemBackground = 2,
      SelectedItemForeground = 3,
      HoveredItemBackground = 4,
      InactiveSelectionBackground = 5,
      ChangedItemBackground = 6
  }

  export type BaseTheme = 'dark' | 'light' | 'darcula';

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

    return theme;
  }


}
