package org.sudu.experiments;

public class TestTexture extends GL.Texture {

  GL.Texture testTexture;
  int updateCalls = 0;
  int setContentCalls = 0;

  public TestTexture(GL.Texture texture) {
    super(texture.ctx);
    this.testTexture = texture;
  }

  @Override
  public void update(Canvas canvas, int xOffset, int yOffset) {
    updateCalls++;
    super.update(getCanvas(canvas), xOffset, yOffset);
  }

  @Override
  public void setContent(Canvas canvas) {
    setContentCalls++;
    super.setContent(getCanvas(canvas));
  }

  private Canvas getCanvas(Canvas canvas) {
    return canvas instanceof TestCanvas ? ((TestCanvas) canvas).getRealCanvas(): canvas;
  }

  public void debug(int number) {
    Debug.consoleInfo("\tTexture # " + number);
    Debug.consoleInfo("\t\tupdateCalls: " + updateCalls);
    Debug.consoleInfo("\t\tsetContentCalls: " + setContentCalls);
  }

  public GL.Texture getRealTexture() {
    return testTexture;
  }
}
