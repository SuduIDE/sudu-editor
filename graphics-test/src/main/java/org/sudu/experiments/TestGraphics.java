package org.sudu.experiments;

import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;

public class TestGraphics extends WglGraphics {

  private final WglGraphics testGraphics;

  int totalDrawCalls = 0;
  int stepDrawCalls = 0;
  int createTextureCalls = 0;

  public TestGraphics(WglGraphics g) {
    super(g.gl, g.canvasFactory, g.cleartypeSupported, g.ctTextPow, g.grTextPow);
    this.testGraphics = g;
  }

  @Override
  public TestTexture createTexture() {
    createTextureCalls++;
    return new TestTexture(super.createTexture());
  }

  @Override
  public FontDesk fontDesk(String family, float size, int weight, int style) {
    return testGraphics.fontDesk(family, size, weight, style);
  }

  @Override
  public void loadImage(String src, Consumer<GL.Texture> onLoad) {
    testGraphics.loadImage(src, onLoad);
  }

  @Override
  public void drawText(int x, int y, V2i size, V4f texRect, GL.Texture texture, V4f color, V4f bgColor, boolean cleartype) {
    totalDrawCalls++;
    stepDrawCalls++;
    testGraphics.drawText(x, y, size, texRect, texture, color, bgColor, cleartype);
  }

  @Override
  public void drawRect(int x, int y, V2i size, V4f color) {
    totalDrawCalls++;
    stepDrawCalls++;
    testGraphics.drawRect(x, y, size, color);
  }

  @Override
  public void drawRect(int x, int y, V2i size, GL.Texture texture) {
    totalDrawCalls++;
    stepDrawCalls++;
    testGraphics.drawRect(x, y, size, texture);
  }

  public void debug() {
    Debug.consoleInfo("\tcreateTextureCalls: " + createTextureCalls);
    Debug.consoleInfo("\ttotalDrawCalls: " + totalDrawCalls);
    Debug.consoleInfo("\tstepDrawCalls: " + stepDrawCalls);
    stepDrawCalls = 0;
  }

}
