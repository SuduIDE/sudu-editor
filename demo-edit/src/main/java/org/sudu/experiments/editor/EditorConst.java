package org.sudu.experiments.editor;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.ui.UiFont;

public interface EditorConst {
  String FONT = Fonts.Consolas;

  int BLANK_LINES = 5;
  int TEXTURE_WIDTH = 1024;
  int RIGHT_PADDING = 40;
  int CARET_X_OFFSET = 30;
  int DEFAULT_FONT_SIZE = 16;
  int MIN_FONT_SIZE = 7;
  int MIN_CACHE_LINES = 7;
  int VIEWPORT_OFFSET = 100;
  int FIRST_LINES = 250;
  int FILE_SIZE_5_KB = 5 * 1024;
  int FILE_SIZE_10_KB = 10 * 1024;
  int FILE_SIZE_50_KB = 50 * 1024;

  float LINE_HEIGHT_MULTI = 1.25f;

  int LINE_NUMBERS_TEXTURE_SIZE = 20;

  double TYPING_STOP_TIME = 1./32.;
  int BIG_RESOLVE_TIME_MS = 100;

  int weightRegular = FontDesk.WEIGHT_LIGHT;
  int weightBold    = FontDesk.WEIGHT_SEMI_BOLD;

  boolean DEFAULT_DISABLE_PARSER = true;
  boolean DEFAULT_SYNC_ORPHANS = false;
  boolean DEFAULT_SYNC_EXCLUDED = true;
  boolean DEFAULT_ENABLE_SYNC_EDIT = false;

  String tabIndent = "  ";

  static FontDesk setFonts(
      String name, float size,
      FontDesk[] fonts, WglGraphics g
  ) {
    return setFonts(name, size, weightRegular, weightBold, fonts, g);
  }

  static FontDesk setFonts(UiFont newFont, float dpr, FontDesk[] fonts, WglGraphics g) {
    return setFonts(newFont.familyName, newFont.size * dpr,
        newFont.weightRegular, newFont.weightBold, fonts, g);
  }

  static FontDesk setFonts(
      String name, float pixelSize,
      int weightRegular, int weightBold,
      FontDesk[] fonts, WglGraphics g
  ) {
    fonts[CodeElement.fontIndex(false, false)] =
        g.fontDesk(name, pixelSize, weightRegular, FontDesk.STYLE_NORMAL);
    fonts[CodeElement.fontIndex(false, true)] =
        g.fontDesk(name, pixelSize, weightRegular, FontDesk.STYLE_ITALIC);
    fonts[CodeElement.fontIndex(true, false)] =
        g.fontDesk(name, pixelSize, weightBold, FontDesk.STYLE_NORMAL);
    fonts[CodeElement.fontIndex(true, true)] =
        g.fontDesk(name, pixelSize, weightBold, FontDesk.STYLE_ITALIC);
    return fonts[CodeElement.fontIndex(false, false)];
  }
}
