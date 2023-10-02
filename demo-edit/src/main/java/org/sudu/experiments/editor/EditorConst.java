package org.sudu.experiments.editor;

import org.sudu.experiments.fonts.Fonts;

public interface EditorConst {
  String FONT = Fonts.Consolas;

  float CONTRAST = .5f;
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

  float LINE_HEIGHT = 1.25f;

  int LINE_NUMBERS_RIGHT_PADDING = 25;

  double TYPING_STOP_TIME = 1./32.;
}
