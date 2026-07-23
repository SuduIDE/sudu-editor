package org.sudu.experiments.swimlane;

import org.sudu.experiments.*;
import org.sudu.experiments.input.*;
import org.sudu.experiments.math.*;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public class SwimlaneTest extends Scene0 implements MouseListener, InputListeners.KeyHandler {

  public static final int timeRange = 100;
  public static final int durationFrequency = 5;
  public static final int gapFrequency = 2;
  public static final int lineEventsMin = 150;
  public static final int lineEventsMax = 300;
  public static final int lines = 20;

  final WglGraphics g;

  private final SetCursor setCursor;
  final SwimlaneShader shader;

  // data
  float[][] data;

  // mesh
  GL.Mesh mesh[];

  // colors
  V4f color[];

  // layout
  int sizeY, gapY, startY;

  V2f sizeT = new V2f();

  int scrollPos = 0;
  int virtualSize = 5000;

  // debug
  float animTime, lastTs;
  float scale = 1;
  boolean mouseDown;
  boolean uParameterX = false;

  public SwimlaneTest(SceneApi api) {
    super(api);
    Color.Cvt.fromHSV(4./6, 1, .125/2, clearColor);
//    Color.Cvt.gray(255, color);
    api.input.onMouse.add(this);
    api.input.onScroll.add(this::onMouseWheel);
    api.input.onKeyPress.add(this);
    api.input.onKeyRelease.add(this);
    this.g = api.graphics;
    setCursor = SetCursor.wrap(api.window);
    shader = new SwimlaneShader(g.gl);
    data = SwimlaneData.create(
        lines, lineEventsMin, lineEventsMax,
        timeRange, durationFrequency, gapFrequency);
    color = new V4f[data.length];
    XorShiftRandom r = new  XorShiftRandom();
    for (int i = 0; i < color.length; i++) {
      color[i] = new V4f();
      setRandomColor(color[i], r);
    }
  }

  private void setRandomColor(V4f c, XorShiftRandom r) {
    double h = r.nextDouble();
    double s = .5 + r.nextDouble() * .25;
    double v = .66 + r.nextDouble() * .33/2;
    Color.Cvt.fromHSV(h, s, v, 1, c);
  }

  static GL.Mesh disposeMesh(GL.Mesh mesh) {
    if (mesh != null)
      mesh.dispose();
    return null;
  }

  private void disposeMesh() {
    for (GL.Mesh m : mesh) {
      m.dispose();
    }
    mesh = null;
  }

  @Override
  public void dispose() {
    disposeMesh();
    shader.dispose(g.gl);
    super.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    if (mouseDown) {
      animTime += (float) timestamp - lastTs;
      scale = (float) (2.25f + Math.sin(animTime) * 2f);
    }
    lastTs = (float) timestamp;
    return mouseDown;
  }

  @Override
  public void paint() {
    if (mesh == null)
      createMesh();

    g.setBlend(WglGraphics.blendAddSrcA);
    g.clear(clearColor);
    drawRect();
    g.enableBlend(false);
  }

  private void createMesh() {
    mesh = new GL.Mesh[data.length];
    for (int i = 0; i < data.length; i++) {
      mesh[i] = SwimlaneShader.createSwimlaneMesh(g.gl, data[i]);

      System.out.println("data[i][data[i].length - 1] = " + data[i][data[i].length - 1]);
    }
  }

  private void drawRect() {
    g.setShader(shader);

    int screenWidth = screen.x * 20 / 18;

    sizeT.x = 1.f * screen.x / timeRange; // * scale;
    sizeT.y = sizeY;

    for (int i = 0; i < mesh.length; i++) {
      int y = startY + (gapY + sizeY) * i;

      float sx = 2f / timeRange * 18 / 20;
      float px = -1 + 2.f / 20;
      float sy = 1.f * sizeY / screen.y;
      float py = 1 - (y * 2.f + sizeY) / screen.y;

      shader.setPosition(g.gl, sx, sy, px, py, g.clientRect);
      shader.setColor(g.gl, color[i]);
      g.drawMesh(mesh[i]);
    }
  }

  @Override
  public void onResize(V2i sSize, float dpr) {
    super.onResize(sSize, dpr);
    sizeY = sSize.y / (data.length + data.length / 2);
    gapY = (sSize.y - sizeY * data.length) / data.length;
    startY = gapY / 2;
  }

  boolean onMouseWheel(MouseEvent event, float dX, float dY) {
    return true;
  }

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      mouseDown = true;
    }

    return Static.emptyConsumer;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      mouseDown = false;
    }
    return true;
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    return false;
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.SPACE && event.singlePress()) {
      uParameterX = !uParameterX;
      System.out.println("uParameterX = " + uParameterX);
      return true;
    }
    System.out.println("event = " + event);
    return false;
  }

}
