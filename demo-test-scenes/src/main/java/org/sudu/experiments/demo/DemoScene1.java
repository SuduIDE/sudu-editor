package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;

import static org.sudu.experiments.demo.IdeaCodeColors.Colors;

public class DemoScene1 extends Scene {
  final WglGraphics g;
  final V4f bgColor = new V4f(Colors.editBgColor);
  final TextRect demoRect = new TextRect(0, 0, 300, 300);
  final DemoRect mouse = new DemoRect(0, 0, 3, 3);
  final TextRect canvasRect1 = new TextRect(0, 0, 300, 300);
  final Caret caret = new Caret();
  final String[] cursorNames = cursors();
  final DemoRect[] cursors = new DemoRect[cursorNames.length];

  GL.Texture mouseTexture;
  GL.Texture demoRectTexture;
  GL.Texture textureCanvas;
  GL.Texture textureCanvas2;
  GL.Texture textureIcon;

  Canvas textCanvas;

  V2i drag;
  int curCursor;

  public DemoScene1(SceneApi api) {
    super(api);
    g = api.graphics;
    V2i clientRect = api.window.getClientRect();

    double r = api.window.devicePixelRatio();
    int dp30 = Numbers.iRnd(r * 30);
    int dp10 = Numbers.iRnd(r * 10);
    for (int i = 0; i < cursors.length; i++) {
      int x = dp10 * (1 + i) + dp30 * i;
      cursors[i] = new DemoRect(x, dp30, dp30, dp30);
      setRandomColor(cursors[i]);
    }

    mouse.pos.set(clientRect.x / 2 - 1, clientRect.y / 2 - 1);
    demoRect.pos.set(clientRect.x - demoRect.size.x, (clientRect.y  - demoRect.size.y) / 2);

    mouseTexture = mouseTexture();
    demoRectTexture = svgTexture();
    demoRect.setTextureRegion(0, 0, demoRectTexture.width(), demoRectTexture.height());

    mouse.size.set(mouseTexture.width(), mouseTexture.height());

    demoRect.bgColor.set(Colors.editBgColor);
    demoRect.color.set(new Color(204, 120, 50));

    api.input.addListener(new MyInputListener());

    textCanvas = g.createCanvas(255, 128);
//    textCanvas.setFont(12, Fonts.Helvetica);

    String s = " HuaweЙ KeyModifiers 收件人 |";
    textCanvas.setFont(Fonts.SegoeUI, 11);
    System.out.println("textCanvas.getFont() = " + textCanvas.getFont());

    textCanvas.measureText(s);
    textCanvas.setFillColor(169, 183, 198);
    drawSomeText(s, textCanvas);
//    textCanvas.addToDocument();

    textureCanvas = g.createTexture();
    textureCanvas.setContent(textCanvas);
    canvasRect1.size.set(textureCanvas.width(), textureCanvas.height());
    canvasRect1.setTextureRegion(0, 0, textureCanvas.width(), textureCanvas.height());

    Color white = new Color(255);
    canvasRect1.setColor(white);
    canvasRect1.setBgColor(Colors.editBgColor);

    textureCanvas2 = g.createTexture();

    g.loadImage("img/icon80.png", t -> {
      System.out.println("t = " + t.width() + "x" + t.height());
      textureIcon = Disposable.assign(textureIcon, t);
      mouse.size.set(textureIcon.width(), textureIcon.height());
    });

    mouse.bgColor.set(bgColor);
  }

  public void dispose() {
    mouseTexture = Disposable.assign(mouseTexture, null);
    demoRectTexture = Disposable.assign(demoRectTexture, null);
    textureCanvas = Disposable.assign(textureCanvas, null);
    textureCanvas2 = Disposable.assign(textureCanvas2, null);
    textureIcon = Disposable.assign(textureIcon, null);
    textCanvas = Disposable.assign(textCanvas, null);
  }

  private void drawSomeText(String s, Canvas canvas) {
    canvas.drawText(s, 0, 20);
    canvas.drawText(s, .25f, 40);
    canvas.drawText(s, .5f, 60);
    canvas.drawText(s, .75f, 80);
  }

  private void layout(V2i clientRect) {
    canvasRect1.pos.y = clientRect.y - canvasRect1.size.y;
  }

  private void setRandomColor(DemoRect rect) {
    Color.Cvt.fromHSV(Math.random(), .5 + Math.random() * .5, .5 + Math.random() * .5, 1, rect.color);
  }

  private GL.Texture svgTexture() {
    Canvas h = g.createCanvas(300, 300);
    h.drawSvgSample();
    h.setFont(Fonts.CourierNew, 11);
    h.setFillColor(187, 187, 187);
    drawSomeText("jsCanvas.setFont(11, CourierNew);", h);
    GL.Texture texture = g.createTexture();
    texture.setContent(h);
    h.dispose();
    return texture;
  }

  private GL.Texture mouseTexture() {
    GL.ImageData image = TGen.chess(5, 5);
    GL.Texture texture = g.createTexture();
    texture.setContent(image);
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
    g.clear(bgColor);

    for (DemoRect cursor : cursors) {
      cursor.draw(g, 0, 0);
    }

    demoRect.drawText(g, demoRectTexture, 0, 0, 0.5f);

    for (int i = 0, N = 7; i < N; i++) {
      canvasRect1.drawText(g, textureCanvas,
          i * (10 + 10 * canvasRect1.size.x / 15) + 5, -5,
          1.f * i / N);
    }

    mouse.drawGrayIcon(g,
        textureIcon != null ? textureIcon : mouseTexture, 0, 0, 0);

    caret.paint(g);

    g.checkError("paint complete ");
  }

  public void onResize(V2i size) {
    layout(size);
  }

  class MyInputListener implements InputListener {
    V2i lastMouse = new V2i();
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

      lastMouse = event.position;

      api.window.setCursor(hitTestCursors(event.position));

      return true;
    }

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      int change = (int) -dY / 10;
      demoRect.size.x += change;
      demoRect.size.y += change;
      demoRect.pos.x -= change / 2;
      demoRect.pos.y -= change / 2;
      return true;
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      String a = press ? "click b=" : "unClick b=";
      System.out.println(a + button + ", count=" + clickCount);

      if (button == InputListener.MOUSE_BUTTON_LEFT && clickCount == 1) {
        V2i p = event.position;
        drag = press && demoRect.isInside(p) ? p : null;
        if (press) {
          caret.setPosition(p.x, p.y);
          caret.startDelay(api.window.timeNow());
        }
      }

      if (button == InputListener.MOUSE_BUTTON_LEFT && clickCount == 2) {
        setRandomColor(demoRect);
      }

      return true;
    }

    @Override
    public boolean onKey(KeyEvent event) {
      System.out.println(
          (event.isPressed ? "key down = " : "key up = ") + event.key +
              ", keyCode = " + event.keyCode +
              ", isRepeated = " + event.isRepeated);

      // do not consume copy\paste keys to fire "copy" event
      // do not consume browser keyboard to allow page reload and debug
      return !KeyEvent.isCopyPasteRelatedKey(event) && !KeyEvent.isBrowserKey(event);
    }

    @Override
    public boolean onContextMenu(MouseEvent event) {
      System.out.println("menu");

      return true;
    }

    @Override
    public boolean onCopy(Consumer<String> setText, boolean isCut) {
      System.out.println("onCopy");
      setText.accept("This is a " + (isCut ? "cut" : "copied") + " text sample");
      return true;
    }

    @Override
    public void onBlur() {
      System.out.println("focus lost");
    }
  }

  static <T> T readArray(T[] array, int pos) {
    return array[pos % array.length];
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
