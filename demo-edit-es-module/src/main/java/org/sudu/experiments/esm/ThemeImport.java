package org.sudu.experiments.esm;

import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsArrayReader;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.math.Color;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import static org.sudu.experiments.editor.ui.colors.EditorColorScheme.*;

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
    Color[] imported = new Color[EditorColorScheme.LastIndex];
    boolean hasAlpha = false, missing = false;
    for (int i = 0; i < EditorColorScheme.LastIndex; i++) {
      JSString v = rdr.get(i);
      if (JsHelper.jsIf(v)) {
        var c = new Color(v.stringValue());
        imported[i] = c;
        if (c.a != 255) hasAlpha = true;
      } else {
        missing = true;
      }
    }

    if (hasAlpha) {
      JsHelper.consoleInfo("Colors with alpha:");
      for (int i = 0; i < imported.length; i++) {
        if (imported[i] != null && imported[i].a != 255)
          JsHelper.consoleInfo("  " + name(i) + ": " + imported[i]);
      }
    }

    if (missing) {
      JsHelper.consoleInfo("!!! Missing Colors:");
      for (int i = 0; i < imported.length; i++)
        if (imported[i] == null) JsHelper.consoleInfo("  " + name(i));
    }

    for (int i = 0; i < imported.length; i++) {
      if (imported[i] != null) theme.modify(i, imported[i]);
    }

    return theme;
  }

  static String name(int n) {
    return switch (n) {
      case TreeViewBackground -> "TreeViewBackground";
      case TreeViewForeground -> "TreeViewForeground";
      case SelectedItemBackground -> "SelectedItemBackground";
      case SelectedItemForeground -> "SelectedItemForeground";
      case HoveredItemBackground -> "HoveredItemBackground";
      case InactiveSelectionBackground -> "InactiveSelectionBackground";
      case AddedResourceForeground -> "AddedResourceForeground";
      case DeletedResourceForeground -> "DeletedResourceForeground";
      case ModifiedResourceForeground -> "ModifiedResourceForeground";
      case PanelHeaderBackground -> "PanelHeaderBackground";
      case PanelHeaderForeground -> "PanelHeaderForeground";
      case EditorBackground -> "EditorBackground";
      case EditorForeground -> "EditorForeground";
      case CurrentLineBackground -> "CurrentLineBackground";
      case DeletedRegionBackground -> "DeletedRegionBackground";
      case DeletedTextBackground -> "DeletedTextBackground";
      case InsertedRegionBackground -> "InsertedRegionBackground";
      case InsertedTextBackground -> "InsertedTextBackground";
      default -> "bad name " + n;
    };
  }
}
