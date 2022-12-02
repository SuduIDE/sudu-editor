package org.sudu.experiments.demo;

import org.sudu.experiments.Fonts;

interface EditorConst {
  String FONT = Fonts.Consolas;
  String FONT2 = Fonts.JetBrainsMono;
  float CONTRAST = .5f;
  int DOCUMENT_LINES = 1000;
  int BLANK_LINES = 5;
  int TEXTURE_WIDTH = 1024 * 3;
  int DEFAULT_FONT_SIZE = 16;
  int TOOLBAR_FONT_SIZE = 20;
  String TOOLBAR_FONT_NAME = Fonts.SegoeUI;
  int MIN_FONT_SIZE = 7;
  int MIN_CACHE_LINES = 7;

  float LINE_HEIGHT = 1.5f - 0.125f;

  int LINE_NUMBERS_TEXTURE_SIZE = 20;
  int LINE_NUMBERS_RIGHT_PADDING = 20;
}
