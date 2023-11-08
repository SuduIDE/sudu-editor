package org.sudu.experiments;

import org.sudu.experiments.fonts.FontDesk;

public class TestCanvas extends Canvas {

  private final Canvas testingCanvas;

  int setFontCalls = 0;
  int measureTextCalls = 0;
  int drawTextCalls = 0;
  int clearCalls = 0;

  public TestCanvas(Canvas testingCanvas) {
    super(testingCanvas.cleartype);
    this.testingCanvas = testingCanvas;
  }

  @Override
  public void setFont(String font, float size, int weight, int style) {
    testingCanvas.setFont(font, size, weight, style);
    setFontCalls++;
  }

  @Override
  public void setFont(FontDesk font) {
    testingCanvas.setFont(font);
    setFontCalls++;
  }

  @Override
  public float measureText(String s) {
    measureTextCalls++;
    return testingCanvas.measureText(s);
  }

  @Override
  public void drawText(String s, float x, float y) {
    testingCanvas.drawText(s, x, y);
    drawTextCalls++;
  }

  @Override
  public void setTopMode(boolean top) {
    testingCanvas.setTopMode(top);
  }

  @Override
  public void setFillColor(int r, int g, int b) {
    testingCanvas.setFillColor(r, g, b);
  }

  @Override
  public void clear() {
    testingCanvas.clear();
    clearCalls++;
  }

  @Override
  public void setTextAlign(int align) {
    testingCanvas.setTextAlign(align);
  }

  public Canvas getRealCanvas() {
    return testingCanvas;
  }

  public void debug() {
    Debug.consoleInfo("\tsetFontCalls: " + setFontCalls);
    Debug.consoleInfo("\tmeasureTextCalls: " + measureTextCalls);
    Debug.consoleInfo("\tdrawTextCalls: " + drawTextCalls);
    Debug.consoleInfo("\tclearCalls: " + clearCalls);
  }

}
