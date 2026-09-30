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
  public static final int SCROLL_WIDTH = 15;

  public final V4f black = new V4f(0, 0, 0, 1);
  public final V4f white = new V4f(1, 1, 1, 1);

  final WglGraphics g;

  private final SetCursor setCursor;
  final SwimlaneShader shader;
  final SwimlaneFromTextureShader tRectShader;

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
  final float FRICTION = 8f;
  final float SCALE_ACCEL = 200f;
  final float OFFSET_ACCEL = .5f;
  final float EPS = 1e-4f;
  float scaleVelocity = 0;
  float offsetVelocity = 0;
  float lastTimestamp = 0;
  float scale = 25;
  float offset = 0;
  boolean mouseDown;
  boolean uParameterX = false;
  float dragScaleVelocity = 0;
  float dragOffsetVelocity = 0;
  float dragLastTs = 0;
  boolean down = false, up = false;
  boolean left = false, right = false;

  final int V_VIRTUAL = 20000, H_VIRTUAL = 40000;
  final ScrollBar vScroll, hScroll;
  int vScrollPos = 0, hScrollPos = 0;

  GL.Texture rtTexture;
  GL.FrameBuffer framebuffer;
  final V4f textureRect =  new V4f();

  public SwimlaneTest(SceneApi api) {
    super(api);
    Color.Cvt.fromHSV(4./6, 1, .125/2, clearColor);
    clearColor.set(0, 0, 0, 1);
//    Color.Cvt.gray(255, color);
    api.input.onMouse.add(this);
    api.input.onScroll.add(this::onMouseWheel);
    api.input.onKeyPress.add(this);
    api.input.onKeyRelease.add(this);
    this.g = api.graphics;
    setCursor = SetCursor.wrap(api.window);
    shader = new SwimlaneShader(g.gl);
    tRectShader = new SwimlaneFromTextureShader(g.gl);
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
    setHScrollPosByOffset();
    api.window.setTitle("Swimlane demo " + events + " events");

    framebuffer = new GL.FrameBuffer(g.gl);
    rtTexture = g.createTexture();
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
    tRectShader.dispose(g.gl);
    framebuffer = Disposable.dispose(framebuffer);
    rtTexture = Disposable.dispose(rtTexture);
    super.dispose();
  }

  float roundToScreenPixel(float offset) {
    int deltaPixels = Math.round(offset * screen.x / 2.f);
    return deltaPixels * 2.f / screen.x;
  }

  @Override
  public boolean update(double timestamp) {
    float dt = lastTimestamp != 0 ? (float) (timestamp - lastTimestamp) : 0;
    lastTimestamp = (float) timestamp;
    if (dt <= 0) return false;

    boolean updated = false;
    if (down ^ up) scaleVelocity += (float) (SCALE_ACCEL * Math.sqrt(scale) * dt * (down ? 1 : -1));
    if (left ^ right) offsetVelocity += OFFSET_ACCEL * dt * (left ? 1 : -1);

    float frictionFactor = (float) Math.exp(-FRICTION * dt);
    scaleVelocity *= frictionFactor;
    offsetVelocity *= frictionFactor;

    if (Math.abs(scaleVelocity) > EPS) {
      scale += scaleVelocity * dt;
      clampScaleValue();
      setVScrollPosByScale();
      updated = true;
    } else {
      scaleVelocity = 0;
    }
    if (Math.abs(offsetVelocity) > EPS) {
      offset += offsetVelocity * dt;
      clampOffsetValue();
      setHScrollPosByOffset();
      updated = true;
    } else {
      offsetVelocity = 0;
    }
    return updated;
  }

  private void clampScaleValue() {
    scale = Math.max(MIN_SCALE, Math.min(scale, MAX_SCALE));
  }

  private void clampOffsetValue() {
    offset = Math.max(MIN_OFFSET, Math.min(offset, MAX_OFFSET));
  }

  @Override
  public void paint() {
    if (rtTexture.width() != g.clientRect.x || rtTexture.height() != lines) {
      System.out.println("resize rtt to " + g.clientRect.x + ", " + lines);
      rtTexture.setSize(g.clientRect.x, lines);
      if (!framebuffer.bindTexture(rtTexture)) {
        System.err.println("Error binding texture");
        return;
      }
    }
    if (mesh == null)
      createMesh();

    framebuffer.bindFramebuffer();
    g.clear(black);
    drawSwimlanes(rtTexture.height());
    g.setDefaultFramebuffer();
    g.clear(clearColor);
    int halfSize = (sizeY + 1) / 2;

    g.enableBlend(false);
    g.drawRect(0,rtTexture.height() / 4, rtTexture.size(), rtTexture);
    drawRects(startY, gapY, sizeY, screen.y);

    layoutScrollbar();
    drawScrollBar();
  }

  private void layoutScrollbar() {
    vScroll.layoutVertical(vScrollPos, 0, screen.y, V_VIRTUAL, screen.x, SCROLL_WIDTH);
    hScroll.layoutHorizontal(hScrollPos, 0, screen.x, H_VIRTUAL, screen.y, SCROLL_WIDTH);
  }

  private void drawScrollBar() {
    g.enableBlend(true);
    vScroll.drawBg(g);
    hScroll.drawBg(g);
    vScroll.drawButton(g);
    hScroll.drawButton(g);
    g.enableBlend(false);
  }

  private void createMesh() {
    mesh = new GL.Mesh[data.length];
    for (int i = 0; i < data.length; i++) {
      mesh[i] = SwimlaneShader.createSwimlaneMesh(g.gl, data[i]);
    }
  }

  final V2i drawTexSize = new V2i();

  private void drawRects(
      int startY, int gapY, int sizeY, int screenY
  ) {
    g.setBlend(WglGraphics.blendNo);
    g.setShader(tRectShader);
    tRectShader.setTexture(g.gl, rtTexture);
    textureRect.set(0, 0, rtTexture.width(), 0);
    drawTexSize.set(rtTexture.width(), sizeY);

    for (int i = 0; i < lines; i++) {
      int y = startY + (gapY + sizeY) * i;

      tRectShader.setPosition(g.gl, 0, y, drawTexSize, g.clientRect);
      textureRect.y = i + 0.5f;
      tRectShader.setTextureRect(g.gl, rtTexture, textureRect);
      tRectShader.set(g.gl, clearColor, color[i]);
      g.drawRect();
    }
  }

  private void drawSwimlanes(int screenY) {
    g.setBlend(WglGraphics.blendAddSrcA);
    g.setShader(shader);

    for (int i = 0, p = 0; i < lines; i++) {
      int y = lines - i - 1;

      float sx = 2f / timeRange * scale;
      float px = roundToScreenPixel(scale * (offset - 1));
      float sy = 1.f / screenY;
      float py = 1 - (y * 2.f + 1) / screenY;

      shader.setPosition(g.gl, sx, sy, px, py, g.clientRect);
      g.drawMesh(mesh[p]);
      p++;
    }
    g.enableBlend(false);
  }

  @Override
  public void onResize(V2i sSize, float dpr) {
    super.onResize(sSize, dpr);
    int textureSize = lines;
    int textureOccupied = textureSize + textureSize / 2;
    int occupied = textureSize + textureSize / 2 + SCROLL_WIDTH;
    sizeY = (sSize.y - occupied) / (lines + lines / 2);
    gapY = (sSize.y - occupied - sizeY * lines) / (lines - 1);
    startY = textureOccupied;
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
          float dt = dragLastTs != 0 ? lastTimestamp - dragLastTs : 0;
          dragLastTs = lastTimestamp;

          int deltaY = e.position.y - startY;
          float scaleDelta = scale * ((float) Math.pow(2.f, 2. * deltaY / screen.y) - 1);
          scale += scaleDelta;
          clampScaleValue();

          int deltaX = e.position.x - startX;
          float offsetDelta = 2.f * deltaX / screen.x / scale;
          offset += offsetDelta;
          clampOffsetValue();

          if (dt > 0) {
            dragScaleVelocity = scaleDelta / dt;
            dragOffsetVelocity = offsetDelta / dt;
          }

          startX = e.position.x;
          startY = e.position.y;
          setVScrollPosByScale();
          setHScrollPosByOffset();
        }
      };
    }
    return Static.emptyConsumer;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    if (button == MOUSE_BUTTON_LEFT) {
      mouseDown = false;
      scaleVelocity = dragScaleVelocity;
      offsetVelocity = dragOffsetVelocity;
      dragScaleVelocity = 0;
      dragOffsetVelocity = 0;
      dragLastTs = 0;
    }
    return true;
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    return false;
  }

  private void onVScroll(ScrollBar.Event event) {
    scaleVelocity = 0;
    vScrollPos = event.getPosition(V_VIRTUAL - screen.y);
    float p = ((float) vScrollPos) / (V_VIRTUAL - screen.y);
    scale = p * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
  }

  private void onHScroll(ScrollBar.Event event) {
    offsetVelocity = 0;
    hScrollPos = event.getPosition(H_VIRTUAL - screen.x);
    float p = ((float) hScrollPos) / (H_VIRTUAL - screen.x);
    offset = -(p * (MAX_OFFSET - MIN_OFFSET) + MIN_OFFSET);
  }

  private void setVScrollPosByScale() {
    vScrollPos = (int) (((scale - MIN_SCALE) / (MAX_SCALE - MIN_SCALE)) * (V_VIRTUAL - screen.y));
  }

  private void setHScrollPosByOffset() {
    hScrollPos = (int) (((-offset - MIN_OFFSET) / (MAX_OFFSET - MIN_OFFSET)) * (H_VIRTUAL - screen.x));
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
