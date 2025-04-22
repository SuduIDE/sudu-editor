package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Consumer;

public class DemoScene1 extends Scene {
  final Color editorBgColor = new Color(IdeaCodeColors.Dark.editBg);
  final V4f bgColor = new V4f(editorBgColor);
  final V2i clientSize = new V2i();
  final TextRect text1Rect = new TextRect();
  final TextRect text2Rect = new TextRect();
  final DemoRect mouse = new DemoRect(0, 0, 3, 3);
  final TextRect textRect3 = new TextRect(0, 0, 300, 300);
  final Caret caret = new Caret();
  final String[] cursorNames = cursors();
  final DemoRect[] cursors = new DemoRect[cursorNames.length];

  GL.Texture textTextureCT, textTextureBW;
  GL.Texture canvasTextureBW, canvasTextureCT;
  GL.Texture iconTexture;


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

    textTextureCT = centerTextTexture(true);
    textTextureBW = centerTextTexture(false);
    applyToRect(text1Rect, textTextureCT);
    applyToRect(text2Rect, textTextureBW);


    canvasTextureBW = createTextTexture2(false);
    canvasTextureCT = createTextTexture2(true);

    textRect3.setTextureRegionDefault(canvasTextureBW);
    textRect3.setSizeToTextureRegion();

    textRect3.setColor(new Color(169, 183, 198));
    textRect3.setBgColor(editorBgColor);

    text1Rect.color.set(1,1,1,1);
    text1Rect.setBgColor(textRect3.bgColor);

    if (1 < 0) g.loadImage("img/icon16.png", t -> {
      System.out.println("t = " + t.width() + "x" + t.height());
      iconTexture = Disposable.assign(iconTexture, t);
      mouse.size.set(iconTexture.width(), iconTexture.height());
    });

    mouse.bgColor.set(bgColor);
  }

  private void applyToRect(TextRect rect, GL.Texture texture) {
    rect.setTextureRegionDefault(texture);
    rect.setSizeToTextureRegion();
//    rect.bgColor.set(editorBgColor);
//    rect.color.set(new Color(204, 120, 50));
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

    text1Rect.pos.set(clientRect.x / 2 - text1Rect.size.x - 10, 50);
    text2Rect.pos.set(clientRect.x / 2 + 10, 50);
    clientSize.set(clientRect);
//    canvasRect1.pos.y = clientRect.y - canvasRect1.size.y;
  }

  public void dispose() {
    textTextureCT = Disposable.assign(textTextureCT, null);
    textTextureBW = Disposable.assign(textTextureBW, null);
    canvasTextureBW = Disposable.assign(canvasTextureBW, null);
    canvasTextureCT = Disposable.assign(canvasTextureCT, null);
    iconTexture = Disposable.assign(iconTexture, null);
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

  private GL.Texture centerTextTexture(boolean cleartype) {
    Canvas h = api.graphics.createCanvas(200, 100, cleartype);
    // h.drawSvgSample();
    h.setFont(Fonts.CourierNew, 11);
    String text1 = "jsCanvas.setFont(11, CourierNew);";
//    h.setFillColor(255, 255, 255);
    h.drawText(text1, 0, 20);
    h.setFillColor(255, 0, 0);
    h.drawText(text1, .25f, 40);
    h.setFillColor(0, 255, 0);
    h.drawText(text1, .5f, 60);
    h.setFillColor(0, 0, 255);
    h.drawText(text1, .75f, 80);
    GL.Texture texture = api.graphics.createTexture();
    texture.setContent(h);
    h.dispose();
    return texture;
  }

  private GL.Texture createTextTexture2(boolean cleartype) {
    String s = " HuaweЙ KeyModifiers 收件人 |";
    Canvas canvas = api.graphics.createCanvas(255, 100, cleartype);
    canvas.setFont(Fonts.SegoeUI, 10);
    System.out.println("canvas.getFont() = " + canvas.getFont());

    canvas.measureText(s);
    drawSomeText(s, canvas);

    GL.Texture texture = api.graphics.createTexture();
    texture.setContent(canvas);
    canvas.dispose();
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

    textRect3.pos.y = clientSize.y - textRect3.size.y - 5;
    textRect3.pos.x = 0;

    g.enableBlend(false);
    // drawText grayscale
    for (int i = 0, N = 7; i < N; i++) {
      textRect3.drawText(g, canvasTextureBW,
          i * (10 + 10 * textRect3.size.x / 15) + 5, 0
      );
    }

    textRect3.pos.y = clientSize.y - textRect3.size.y * 2 - 10;

    // drawText clear type
    for (int i = 0, N = 7; i < N; i++) {
      GL.Texture texture = canvasTextureCT;
      textRect3.pos.x = i * (10 + 10 * textRect3.size.x / 15) + 5;
      textRect3.drawText(g, texture, true);
    }

    text1Rect.drawText(g, textTextureCT, true);
    g.enableBlend(true);
    text2Rect.draw(g, textTextureBW);
    g.enableBlend(false);

    if (iconTexture != null) {
      mouse.drawGrayIcon(g, iconTexture, 0, 0, 0);
    }

    if (caret.state) caret.paint(g, new V2i());

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
    text1Rect.size.x += change;
    text1Rect.size.y += change;
    text1Rect.pos.x -= change / 2;
    text1Rect.pos.y -= change / 2;
    return true;
  }

  class MyInputListener implements MouseListener {
    @Override
    public boolean onMouseMove(MouseEvent event) {

      int nextX = event.position.x - mouse.size.x; // 2;
      int nextY = event.position.y - mouse.size.y; // 2;
      mouse.pos.set(nextX, nextY);

      api.window.setCursor(hitTestCursors(event.position));

      return true;
    }


    @Override
    public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
      if (button == MouseListener.MOUSE_BUTTON_LEFT && clickCount == 2) {
        setRandomColor(text1Rect);
      }

      return true;
    }

    @Override
    public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
      System.out.println("mouseDown b=" + button);
      if (button == MouseListener.MOUSE_BUTTON_LEFT) {
        V2i p = event.position;
        V2i drag = text1Rect.isInside(p) ? p : null;
        caret.setPos(p.x, p.y);
        caret.startDelay(api.window.timeNow());

        return drag != null ? e -> {
          V2i rcPos = text1Rect.pos;
          rcPos.x += e.position.x - drag.x;
          rcPos.y += e.position.y - drag.y;
          drag.set(e.position);
        } : Static.emptyConsumer;
      }
      return null;
    }

    @Override
    public boolean onMouseUp(MouseEvent event, int button) {
      System.out.println("mouseUp b=" + button);
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
