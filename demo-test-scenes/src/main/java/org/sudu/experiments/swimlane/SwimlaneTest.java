package org.sudu.experiments.swimlane;

import org.sudu.experiments.*;
import org.sudu.experiments.input.*;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2f;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public class SwimlaneTest extends Scene0 implements MouseListener, InputListeners.KeyHandler {

  final WglGraphics g;

  private final SetCursor setCursor;
  final SwimlaneShader shader;

  float[] data1 = new float[8];
  float[] data2 = new float[16000];

  GL.Mesh mesh1;
  GL.Mesh mesh2;

  final V4f color = new V4f(1,1,1,1);
  V2f pos = new V2f();
  V2f size = new V2f();

  V2f posT = new V2f();
  V2f sizeT = new V2f();

  int scrollPos = 0;
  int virtualSize = 5000;

  float animTime, lastTs;
  float scale = 1;
  boolean mouseDown;
  boolean uParameterX = false;

  public SwimlaneTest(SceneApi api) {
    super(api);
    Color.Cvt.gray(0, clearColor);
    Color.Cvt.gray(255, color);
    api.input.onMouse.add(this);
    api.input.onScroll.add(this::onMouseWheel);
    api.input.onKeyPress.add(this);
    api.input.onKeyRelease.add(this);
    this.g = api.graphics;
    setCursor = SetCursor.wrap(api.window);
    shader = new SwimlaneShader(g.gl);
  }

  static GL.Mesh disposeMesh(GL.Mesh mesh) {
    if (mesh != null)
      mesh.dispose();
    return null;
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

  static float[] a(float ... lr) { return lr; }

  @Override
  public void paint() {
    if (mesh1 == null)
      mesh1 = SwimlaneShader.createSwimlaneMesh(g.gl, data1);
    if (mesh2 == null)
      mesh2 = SwimlaneShader.createSwimlaneMesh(g.gl, data2);

    g.setBlend(WglGraphics.blendAddSrcA);
    g.clear(clearColor);
    drawRect();
    g.enableBlend(false);
  }

  private void drawRect() {
    g.setShader(shader);
    shader.setColor(g.gl, color);
    posT.set(pos);
    sizeT.set(size.x * scale, size.y);
    shader.setPosition(g.gl, pos.x, pos.y - sizeT.y - 5, sizeT, g.clientRect);
    g.drawMesh(mesh1);
    shader.setPosition(g.gl, pos.x, posT.y, sizeT, g.clientRect);
    g.drawMesh(mesh2);
  }

  float pixelToGlX(float x, int screenX) { return x * 2.f / screenX - 1.f; }
  float pixelToGlY(float y, int screenY) { return 1.f - y * 2.f / screenY; }

  @Override
  public void onResize(V2i sSize, float dpr) {
    super.onResize(sSize, dpr);
    int _20 = DprUtil.toPx(20, dpr);
    int width = sSize.x * 7 / 8;
    size.set(width, _20);
    pos.set((sSize.x - size.x) / 2, (sSize.y - size.y) / 2);

    data1[0] = pixelToGlX(10-.25f, width);
    data1[1] = pixelToGlX(20+.25f, width);

    data1[2] = pixelToGlX(20+.75f, width);
    data1[3] = pixelToGlX(45+.5f, width);

    data1[4] = pixelToGlX(45+.5f, width);
    data1[5] = pixelToGlX(50, width);

    data1[6] = pixelToGlX(53-1.f/51, width);
    data1[7] = pixelToGlX(58+1.f/51, width);

    for (int i = 0; i < data2.length / 2; i++) {
      data2[i*2    ] = pixelToGlX(i * 3, width);
      data2[i*2 + 1] = pixelToGlX(i * 3 + 1, width);
    }
    disposeMesh();
  }

  private void disposeMesh() {
    mesh1 = disposeMesh(mesh1);
    mesh2 = disposeMesh(mesh2);
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
