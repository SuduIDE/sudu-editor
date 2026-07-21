package org.sudu.experiments.swimlane;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.DemoRect;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public class SwimlaneTest extends Scene0 implements MouseListener {

  final WglGraphics g;

  private final SetCursor setCursor;
  final SwimlaneShader shader;
  GL.Mesh mesh;

  DemoRect control = new DemoRect();

  int scrollPos = 0;
  int virtualSize = 5000;

  float animTime, lastTs;
  boolean mouseDown;

  public SwimlaneTest(SceneApi api) {
    super(api);
    Color.Cvt.gray(0, clearColor);
    Color.Cvt.gray(255, control.color);
    api.input.onMouse.add(this);
    api.input.onScroll.add(this::onMouseWheel);
    this.g = api.graphics;
    setCursor = SetCursor.wrap(api.window);
    createMesh();
    shader = new SwimlaneShader(g.gl);
  }

  @Override
  public void dispose() {
    mesh.dispose();
    shader.dispose(g.gl);
    super.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    if (mouseDown) {
      animTime += (float) timestamp - lastTs;
      mesh.dispose();
      createMesh();
    }
    lastTs = (float) timestamp;
    return mouseDown;
  }

  private void createMesh() {
    mesh = SwimlaneShader.createSwRectangle(g.gl,
        -0.5f + 0.5f * (float) Math.sin(animTime),
        0.5f + 0.5f * (float) Math.cos(animTime));
  }

  @Override
  public void paint() {
    g.clear(clearColor);
    drawRect();
    g.enableBlend(true);
    g.enableBlend(false);
  }

  private void drawRect() {
    g.setShader(shader);
    shader.setPosition(g.gl, control.pos.x, control.pos.y, control.size, g.clientRect);
    shader.setColor(g.gl, control.color);
    g.drawMesh(mesh);
  }

  @Override
  public void onResize(V2i size, float dpr) {
    super.onResize(size, dpr);
    int _20 = DprUtil.toPx(20, dpr);
    int _50 = size.x / 2;
    control.size.set(_50, _20);
    control.pos.set((size.x - control.size.x) / 2, (size.y - control.size.y) / 2);
  }

  boolean onMouseWheel(MouseEvent event, float dX, float dY) {
    int change = (Math.abs((int) dY) + 4) / 2;
    int change1 = dY < 0 ? -1 : 1;
    scrollPos = clampScrollPos(scrollPos + change * change1);

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

  int clampScrollPos(int pos) {
    return Math.min(Math.max(0, pos), scrollMaxValue());
  }

  int scrollMaxValue() { return virtualSize - controlHeight(); }

  int controlHeight() { return control.size.y; }

}
