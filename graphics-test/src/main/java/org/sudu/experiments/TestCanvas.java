package org.sudu.experiments;

import org.sudu.experiments.fonts.FontDesk;

public class TestCanvas extends Canvas {

  int setFontCalls = 0;
  int measureTextCalls = 0;
  int drawTextCalls = 0;
  int clearCalls = 0;

  public TestCanvas(int w, int h, boolean cleartype) {
    super(cleartype);
    width = w;
    height = h;
  }

  @Override
  public void setFont(String font, float size, int weight, int style) {
    setFontCalls++;
  }

  @Override
  public void setFont(FontDesk font) {
    setFontCalls++;
  }

  @Override
  public float measureText(String s) {
    measureTextCalls++;
    return s.length() * .375f;
  }

  @Override
  public void drawText(String s, float x, float y) {
    drawTextCalls++;
  }

  @Override
  public void setTopMode(boolean top) {}

  @Override
  public void setFillColor(int r, int g, int b) {}

  @Override
  public void clear() {
    clearCalls++;
  }

  @Override
  public void setTextAlign(int align) {}

  public void debug() {
    Debug.consoleInfo("\tsetFontCalls: " + setFontCalls);
    Debug.consoleInfo("\tmeasureTextCalls: " + measureTextCalls);
    Debug.consoleInfo("\tdrawTextCalls: " + drawTextCalls);
    Debug.consoleInfo("\tclearCalls: " + clearCalls);
  }

}
