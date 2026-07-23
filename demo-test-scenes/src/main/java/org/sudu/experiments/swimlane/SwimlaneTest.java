package org.sudu.experiments.swimlane;

import org.sudu.experiments.*;
import org.sudu.experiments.input.*;
import org.sudu.experiments.math.*;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public class SwimlaneTest extends Scene0 implements MouseListener, InputListeners.KeyHandler {

  public static final int timeRange = 100;
  public static final int durationFrequency = 5;
  public static final int gapFrequency = 2;
  public static final int lineEventsMin = 10000;
  public static final int lineEventsMax = 15000;
  public static final int lines = 20;

  static final boolean render2Lines = false;

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

  int scrollPos = 0;
  int virtualSize = 5000;

  // debug
  final float MIN_SCALE = .5f, MAX_SCALE = 500f;
  final float MIN_OFFSET = -1, MAX_OFFSET = 1;
  float scAnimTime, scLastTs;
  float ofAnimTime, ofLastTs;
  float scale = 25;
  float offset = 0;
  boolean mouseDown;
  boolean uParameterX = false;
  boolean down = false, up = false;
  boolean left = false, right = false;

  final int V_VIRTUAL = 10000, H_VIRTUAL = 20000;
  final ScrollBar vScroll, hScroll;
  int vScrollPos = 0, hScrollPos = 0;

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
        lines + (render2Lines ? lines / 2 : 0),
        lineEventsMin, lineEventsMax,
        timeRange, durationFrequency, gapFrequency);
    color = new V4f[data.length];
    XorShiftRandom r = new  XorShiftRandom();
    for (int i = 0; i < color.length; i++) {
      color[i] = new V4f();
      setRandomColor(color[i], r);
    }
    int events = countEvents(data);

    vScroll = new ScrollBar();
    hScroll = new ScrollBar();
    applyScrollStyle(vScroll);
    applyScrollStyle(hScroll);
    setVScrollPosByScale();
    setHScrollPosByScale();
    api.window.setTitle("Swimlane demo " + events + " events");
  }

  private void applyScrollStyle(ScrollBar bar) {
    bar.setColor(
        new Color(80, 80, 80, 200),
        new Color(43, 43, 43, 228));
  }

  static int countEvents(float[][] data) {
    int count = 0;
    for (float[] line : data)
      count += line.length / 2;
    return count;
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

  float roundToScreenPixel(float offset) {
    int deltaPixels = Math.round(offset * screen.x / 2.f);
    return deltaPixels * 2.f / screen.x;
  }

  @Override
  public boolean update(double timestamp) {
    boolean updated = false;
    if (down ^ up) {
      updated = true;
      float delta = (float) Math.cbrt(scAnimTime) / 10 * (down ? -1 : 1);
      scale += delta;
      scale = Math.max(MIN_SCALE, Math.min(scale, MAX_SCALE));
      scAnimTime += (float) timestamp - scLastTs;
      setVScrollPosByScale();
    } else {
      scAnimTime = 0;
    }
    if (left ^ right) {
      updated = true;
      float delta = (float) (Math.pow(ofAnimTime, .3f)) / screen.x * (left ? -1 : 1);
      offset += delta;
      offset = Math.max(MIN_OFFSET, Math.min(offset, MAX_OFFSET));
      ofAnimTime += (float) timestamp - ofLastTs;
      setHScrollPosByScale();
    } else {
      ofAnimTime = 0;
    }
    scLastTs = ofLastTs = (float) timestamp;
    return updated;
  }

  @Override
  public void paint() {
    if (mesh == null)
      createMesh();

    g.setBlend(WglGraphics.blendAddSrcA);
    g.clear(clearColor);
    drawRect();
    g.enableBlend(false);
    layoutScrollbar();
    drawScrollBar();
  }

  private void layoutScrollbar() {
    vScroll.layoutVertical(vScrollPos, 0, screen.y, V_VIRTUAL, screen.x, 15);
    hScroll.layoutHorizontal(hScrollPos, 0, screen.x, H_VIRTUAL, screen.y, 15);
  }

  private void drawScrollBar() {
    vScroll.drawBg(g);
    hScroll.drawBg(g);
    vScroll.drawButton(g);
    hScroll.drawButton(g);
  }

  private void createMesh() {
    mesh = new GL.Mesh[data.length];
    for (int i = 0; i < data.length; i++) {
      mesh[i] = SwimlaneShader.createSwimlaneMesh(g.gl, data[i]);
    }
  }

  private void drawRect() {
    g.setShader(shader);

    for (int i = 0, p = 0; i < lines; i++) {
      int y = startY + (gapY + sizeY) * i;

      float sx = 2f / timeRange * scale;
      float px = roundToScreenPixel(scale * (offset - 1));
      float sy = 1.f * sizeY / screen.y;
      float py = 1 - (y * 2.f + sizeY) / screen.y;

      shader.setPosition(g.gl, sx, sy, px, py, g.clientRect);
      shader.setColor(g.gl, color[p]);
      g.drawMesh(mesh[p]);
      p++;
      if (render2Lines && i % 2 == 0) {
        shader.setColor(g.gl, color[p]);
        g.drawMesh(mesh[p]);
        p++;
      }
    }
  }

  @Override
  public void onResize(V2i sSize, float dpr) {
    super.onResize(sSize, dpr);
    sizeY = sSize.y / (lines + lines / 2);
    gapY = (sSize.y - sizeY * lines) / lines;
    startY = gapY / 2;
  }

  boolean onMouseWheel(MouseEvent event, float dX, float dY) {
    return true;
  }

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      mouseDown = true;
      if (vScroll.hitButton(event.position))
        return vScroll.onMouseDown(event.position, this::onVScroll, true);
      if (hScroll.hitButton(event.position))
        return hScroll.onMouseDown(event.position, this::onHScroll, false);
      return new Consumer<>() {
        int startX = event.position.x;
        int startY = event.position.y;
        @Override
        public void accept(MouseEvent e) {
          int deltaY = e.position.y - startY;
          scale *= (float) Math.pow(2.f, 2. * deltaY / screen.y);
          int deltaX = e.position.x - startX;
          offset += 2.f * deltaX / screen.x / scale;
          startX = e.position.x;
          startY = e.position.y;
          setVScrollPosByScale();
          setHScrollPosByScale();
        }
      };
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

  private void onVScroll(ScrollBar.Event event) {
    vScrollPos = event.getPosition(V_VIRTUAL - screen.y);
    float p = ((float) vScrollPos) / (V_VIRTUAL - screen.y);
    scale = p * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
  }

  private void onHScroll(ScrollBar.Event event) {
    hScrollPos = event.getPosition(H_VIRTUAL - screen.x);
    float p = ((float) hScrollPos) / (H_VIRTUAL - screen.x);
    offset = p * (MAX_OFFSET - MIN_OFFSET) + MIN_OFFSET;
  }

  private void setVScrollPosByScale() {
    vScrollPos = (int) (((scale - MIN_SCALE) / (MAX_SCALE - MIN_SCALE)) * (V_VIRTUAL - screen.y));
  }

  private void setHScrollPosByScale() {
    hScrollPos = (int) (((offset - MIN_OFFSET) / (MAX_OFFSET - MIN_OFFSET)) * (H_VIRTUAL- screen.x));
  }

  int m = 0;

  @Override
  public boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.ARROW_DOWN) down = event.isPressed;
    if (event.keyCode == KeyCode.ARROW_UP) up = event.isPressed;
    if (event.keyCode == KeyCode.ARROW_LEFT) left = event.isPressed;
    if (event.keyCode == KeyCode.ARROW_RIGHT) right = event.isPressed;

    if (false) {
      if (event.keyCode == KeyCode.ARROW_LEFT && event.singlePress()) {
        // left = event.isPressed;
        offset -= 2.f / screen.x;
        System.out.println("[" + (++m) + "]offset = " + offset);
      }
      if (event.keyCode == KeyCode.ARROW_RIGHT && event.singlePress()) {
        // right = event.isPressed;
        offset += 2.f / screen.x;
        System.out.println("[" + (++m) + "]offset = " + offset);
      }
    }

    if (event.keyCode == KeyCode.SPACE && event.singlePress()) {
      uParameterX = !uParameterX;
      System.out.println("uParameterX = " + uParameterX);
      return true;
    }
//    System.out.println("event = " + event);
    return false;
  }

}
