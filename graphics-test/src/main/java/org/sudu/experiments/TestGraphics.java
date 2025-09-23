package org.sudu.experiments;

import org.sudu.experiments.fonts.FontDesk;

import java.util.function.Consumer;

public class TestGraphics extends WglGraphics {
  public final TestContext testContext() { return (TestContext) gl; };

  public TestGraphics() {
    this(new TestContext(), TestCanvas::new);
  }

  public TestGraphics(TestContext ct, CanvasFactory factory) {
    super(ct, factory, true, 1, 1);
  }


  @Override
  public Runnable repaint() {
    return () -> {};
  }

  @Override
  public FontDesk fontDesk(String family, float size, int weight, int style) {
    return new FontDesk(
        family, size, weight, style,
        10, 4,
        10, 12, 2, null);
  }

  @Override
  public void loadImage(String src, Consumer<GL.Texture> onLoad) {}
}
