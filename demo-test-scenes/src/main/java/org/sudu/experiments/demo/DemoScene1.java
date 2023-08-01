package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;

import static org.sudu.experiments.demo.IdeaCodeColors.Colors;

public class DemoScene1 extends Scene {
  final V4f bgColor = new V4f(Colors.editBgColor);
  final TextRect demoRect = new TextRect(0, 0, 300, 300);
  final DemoRect mouse = new DemoRect(0, 0, 3, 3);
  final TextRect canvasRect1 = new TextRect(0, 0, 300, 300);
  final Caret caret = new Caret();
  final String[] cursorNames = cursors();
  final DemoRect[] cursors = new DemoRect[cursorNames.length];

  GL.Texture demoRectTexture;
  GL.Texture canvas1Texture;
  GL.Texture canvas2Texture;
  GL.Texture iconTexture;

  Canvas textCanvas;

  V2i drag;

  public DemoScene1(SceneApi api) {
    super(api);
    WglGraphics g = api.graphics;
    api.input.onMouse.add(new MyInputListener());
    api.input.onKeyPress.add(this::onKeyEvent);
    api.input.onKeyRelease.add(this::onKeyEvent);
    api.input.onCopy.add(this::onCopy);
    api.input.onBlur.add(this::onBlur);
    api.input.onContextMenu.add(this::onContextMenu);
    api.input.onScroll.add(this::onScroll);

    demoRectTexture = svgTexture();
    demoRect.setTextureRegionDefault(demoRectTexture);
    demoRect.setSizeToTextureRegion();
    demoRect.bgColor.set(Colors.editBgColor);
    demoRect.color.set(new Color(204, 120, 50));

    String s = " HuaweЙ KeyModifiers 收件人 |";

    textCanvas = g.createCanvas(255, 100);
    textCanvas.setFont(Fonts.SegoeUI, 11);
    System.out.println("textCanvas.getFont() = " + textCanvas.getFont());

    textCanvas.measureText(s);
    textCanvas.setFillColor(169, 183, 198);
    drawSomeText(s, textCanvas);

    canvas1Texture = g.createTexture();
    canvas1Texture.setContent(textCanvas);

    canvasRect1.setTextureRegionDefault(canvas1Texture);
    canvasRect1.setSizeToTextureRegion();

    canvasRect1.setColor(new Color(255));
    canvasRect1.setBgColor(Colors.editBgColor);

    canvas2Texture = g.createTexture();

    if (1 < 0) g.loadImage("img/icon16.png", t -> {
      System.out.println("t = " + t.width() + "x" + t.height());
      iconTexture = Disposable.assign(iconTexture, t);
      mouse.size.set(iconTexture.width(), iconTexture.height());
    });

    mouse.bgColor.set(bgColor);
  }

  void layout(V2i clientRect, float dpr) {
    System.out.println("clientRect = " + clientRect);
    System.out.println("dpr = " + dpr);
    int dp30 = DprUtil.toPx(30, dpr);
    int dp10 = DprUtil.toPx(10, dpr);
    for (int i = 0; i < cursors.length; i++) {
      int x = dp10 * (1 + i) + dp30 * i;
      cursors[i] = new DemoRect(x, dp30, dp30, dp30);
      setRandomColor(cursors[i]);
    }

    mouse.pos.set(clientRect.x / 2 - 1, clientRect.y / 2 - 1);
    demoRect.pos.set(
        (clientRect.x - demoRect.size.x) / 2,
        (clientRect.y - demoRect.size.y) / 2);
    canvasRect1.pos.y = clientRect.y - canvasRect1.size.y;
  }

  public void dispose() {
    demoRectTexture = Disposable.assign(demoRectTexture, null);
    canvas1Texture = Disposable.assign(canvas1Texture, null);
    canvas2Texture = Disposable.assign(canvas2Texture, null);
    iconTexture = Disposable.assign(iconTexture, null);
    textCanvas = Disposable.assign(textCanvas, null);
  }

  private void drawSomeText(String s, Canvas canvas) {
    canvas.drawText(s, 0, 20);
    canvas.drawText(s, .25f, 40);
    canvas.drawText(s, .5f, 60);
    canvas.drawText(s, .75f, 80);
  }

  private void setRandomColor(DemoRect rect) {
    Color.Cvt.fromHSV(Math.random(), .5 + Math.random() * .5, .5 + Math.random() * .5, 1, rect.color);
  }

  private GL.Texture svgTexture() {
    Canvas h = api.graphics.createCanvas(300, 300);
    h.drawSvgSample();
    h.setFont(Fonts.CourierNew, 11);
    h.setFillColor(187, 187, 187);
    drawSomeText("jsCanvas.setFont(11, CourierNew);", h);
    GL.Texture texture = api.graphics.createTexture();
    texture.setContent(h);
    h.dispose();
    return texture;
  }

  int frameN;
  public boolean update(double timestamp) {
    frameN++;
    Color.Cvt.fromHSV(fr(timestamp / 5), 1, 1, 1, mouse.color);
    // repaint only if caret blinking
    return caret.update(timestamp);
  }

  static double fr(double x) {
    return x - (int) x;
  }

  public void paint() {
    WglGraphics g = api.graphics;

    g.clear(bgColor);

    for (DemoRect cursor : cursors) {
      cursor.draw(g, 0, 0);
    }

    demoRect.drawText(g, demoRectTexture, 0, 0, 0.5f);

    GL.Texture texture = canvas1Texture;
    for (int i = 0, N = 7; i < N; i++) {
      canvasRect1.drawText(g, texture,
          i * (10 + 10 * canvasRect1.size.x / 15) + 5, -5,
          1.f * i / N);
    }

    if (iconTexture != null) {
      mouse.drawGrayIcon(g, iconTexture, 0, 0, 0);
    }

    caret.paint(g, new V2i());

    g.checkError("paint complete ");
  }

  public void onResize(V2i size, float dpr) {
    layout(size, dpr);
  }

  boolean onKeyEvent(KeyEvent event) {
    System.out.println(
        (event.isPressed ? "key down = " : "key up = ") + event.key +
            ", keyCode = " + event.keyCode +
            ", isRepeated = " + event.isRepeated);
    return false;
  }

  boolean onCopy(Consumer<String> setText, boolean isCut) {
    System.out.println("onCopy");
    setText.accept("This is a " + (isCut ? "cut" : "copied") + " text sample");
    return true;
  }

  boolean onScroll(MouseEvent event, float dX, float dY) {
    int change = (int) -dY / 10;
    demoRect.size.x += change;
    demoRect.size.y += change;
    demoRect.pos.x -= change / 2;
    demoRect.pos.y -= change / 2;
    return true;
  }

  class MyInputListener implements MouseListener {
    @Override
    public boolean onMouseMove(MouseEvent event) {
      if (drag != null) {
        V2i rcPos = demoRect.pos;
        rcPos.x += event.position.x - drag.x;
        rcPos.y += event.position.y - drag.y;
        drag = event.position;
      }

      int nextX = event.position.x - mouse.size.x; // 2;
      int nextY = event.position.y - mouse.size.y; // 2;
      mouse.pos.set(nextX, nextY);

      api.window.setCursor(hitTestCursors(event.position));

      return true;
    }


    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      String a = press ? "click b=" : "unClick b=";
      System.out.println(a + button + ", count=" + clickCount);

      if (button == MouseListener.MOUSE_BUTTON_LEFT && clickCount == 1) {
        V2i p = event.position;
        drag = press && demoRect.isInside(p) ? p : null;
        if (press) {
          caret.setPosition(p.x, p.y);
          caret.startDelay(api.window.timeNow());
        }
      }

      if (button == MouseListener.MOUSE_BUTTON_LEFT && clickCount == 2) {
        setRandomColor(demoRect);
      }

      return true;
    }
  }

  boolean onContextMenu(MouseEvent event) {
    System.out.println("menu");

    return true;
  }

  private void onBlur() {
    System.out.println("focus lost");
  }


  static String[] cursors() {
    return new String[]{
        Cursor.pointer,
        Cursor.text,
        Cursor.ew_resize,
        Cursor.ns_resize,
    };
  }

  private String hitTestCursors(V2i position) {
    for (int i = 0; i < cursors.length; i++) {
      if (cursors[i].isInside(position)) {
        return cursorNames[i];
      }
    }
    return null;
  }
}
