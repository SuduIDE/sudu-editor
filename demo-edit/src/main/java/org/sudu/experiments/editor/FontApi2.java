package org.sudu.experiments.editor;

import org.sudu.experiments.ui.UiContext;

public class FontApi2 implements EditorUi.FontApi {
  final EditorComponent editor1;
  final EditorComponent editor2;
  final UiContext uiContext;

  public FontApi2(
      EditorComponent editor1,
      EditorComponent editor2,
      UiContext uiContext
  ) {
    this.editor1 = editor1;
    this.editor2 = editor2;
    this.uiContext = uiContext;
  }

  @Override
  public void increaseFont() {
    editor1.increaseFont();
    editor2.increaseFont();
  }

  @Override
  public void decreaseFont() {
    editor1.decreaseFont();
    editor2.decreaseFont();
  }

  @Override
  public void changeFont(String f) {
    editor1.changeFont(f);
    editor2.changeFont(f);
  }

  @Override
  public void setFontPow(float p) {
    uiContext.setFontPow(p);
  }
}
