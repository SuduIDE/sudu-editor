package org.sudu.experiments.demo;

import org.sudu.experiments.Fonts;

interface EditorConst {
  String FONT1 = Fonts.Consolas;
  String FONT = Fonts.JetBrainsMono;
  float CONTRAST = .5f;
  int DOCUMENT_LINES = 1000;
  int BLANK_LINES = 5;
  int TEXTURE_WIDTH = 1024 * 3;
  int DEFAULT_FONT_SIZE = 16;
  int TOOLBAR_FONT_SIZE = 20;
  String TOOLBAR_FONT_NAME = Fonts.SegoeUI;
  int MIN_FONT_SIZE = 7;
  int MIN_CACHE_LINES = 7;

  // lineHeight = 1.2 * (font.ascent + font.descent)
  int LINE_HEIGHT_NUMERATOR = 12;
  int LINE_HEIGHT_DENOMINATOR = 10;

  int LINE_NUMBERS_TEXTURE_SIZE = 20;
}
