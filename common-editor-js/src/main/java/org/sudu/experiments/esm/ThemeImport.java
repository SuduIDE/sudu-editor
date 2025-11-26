package org.sudu.experiments.esm;

import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.js.JsArrayReader;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.UiFont;
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
  String uiFont = "uiFont";
  String editorFont = "editorFont";
  boolean debug = true;

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

    importColors(t, theme);
    importFonts(t, theme);

    return theme;
  }

  static void importFonts(JSObject t, EditorColorScheme theme) {
    if (JsHelper.hasProperty(t, editorFont)) {
      JsFont f = JsHelper.getProperty(t, editorFont);
      importFont(f, theme.editorFont);
    }
    if (JsHelper.hasProperty(t, uiFont)) {
      JsFont f = JsHelper.getProperty(t, uiFont);
      importFont(f, theme.treeViewFont);
    }
  }

  static void importFont(JsFont f, UiFont font) {
    font.familyName = f.getFamily();
    font.size = f.getSize();
    font.weightRegular = (int) f.getWeight();
  }

  static void importColors(JSObject jsTheme, EditorColorScheme theme) {
    JsArrayReader<JSString> rdr = jsTheme.cast();
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

    boolean[] modified = new boolean[imported.length];
    for (int i = 0; i < imported.length; i++) {
      Color c = imported[i];
      modified[i] = c != null && theme.modify(i, c);
    }

    if (debug) {
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
        Color c = imported[i];
        if (c != null && modified[i]) {
          JsHelper.consoleInfo(name(i) + '(' + i + ") set to " + c);
        }
      }
    }
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
      case IgnoredResourceForeground -> "IgnoredResourceForeground";
      case PanelHeaderBackground -> "PanelHeaderBackground";
      case PanelHeaderForeground -> "PanelHeaderForeground";
      case EditorBackground -> "EditorBackground";
      case EditorForeground -> "EditorForeground";
      case CurrentLineBorder -> "CurrentLineBorder";
      case CurrentLineBackground -> "CurrentLineBackground";
      case DeletedRegionBackground -> "DeletedRegionBackground";
      case DeletedTextBackground -> "DeletedTextBackground";
      case InsertedRegionBackground -> "InsertedRegionBackground";
      case InsertedTextBackground -> "InsertedTextBackground";
      case LineNumberForeground -> "LineNumberForeground";
      case ActiveLineNumberForeground -> "ActiveLineNumberForeground";
      case LineNumberActiveForeground -> "LineNumberActiveForeground";
      case SyntaxKeyword -> " SyntaxKeyword";
      case SyntaxNull -> " SyntaxNull";
      case SyntaxBoolean -> " SyntaxBoolean";
      case SyntaxSemi -> " SyntaxSemi";
      case SyntaxField -> " SyntaxField";
      case SyntaxString -> " SyntaxString";
      case SyntaxError -> " SyntaxError";
      case SyntaxNumeric -> " SyntaxNumeric";
      case SyntaxMethod -> " SyntaxMethod";
      case SyntaxComment -> " SyntaxComment";
      case SyntaxAnnotation -> " SyntaxAnnotation";
      case SyntaxType -> " SyntaxType";
      case SyntaxOperator -> " SyntaxOperator";

      default -> "bad name " + n;
    };
  }
}
