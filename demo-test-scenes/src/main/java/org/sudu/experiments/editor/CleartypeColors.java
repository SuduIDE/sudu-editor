package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import static org.sudu.experiments.math.Color.Cvt.fixHue;
import static org.sudu.experiments.math.Numbers.iRnd;

public class CleartypeColors extends Scene {
  public static final String text = " Cleartype text test: 3.14159265358979," +
      " IDE编码//背后就是莫斯科，我们已无退路。不抛弃、不放弃。\n";

  final Color editorBgColor = IdeaCodeColors.Dark.editBg;
  final V4f bgColor = new V4f(editorBgColor);
  final V2i clientSize = new V2i();
  final V2i cSizeHalf = new V2i();
  final TextRect text1Rect = new TextRect();
  final TextRect text2Rect = new TextRect();

  String[] fonts = Fonts.editorFonts(true);

  FontDesk fontDesk;
  int measured;

  GL.Texture textTextureCT, textTextureBW;

  public CleartypeColors(SceneApi api) {
    super(api);
    api.input.onMouse.add(new MyInputListener());

    init(fonts[(int) (Math.random() * fonts.length)]);

    System.out.println("measured = " + measured);
  }

  private void init(String font) {
    fontDesk = api.graphics.fontDesk(font, 10);

    api.graphics.mCanvas.setFont(fontDesk);
    measured = iRnd(
        api.graphics.mCanvas.measureText(text) +
            api.graphics.mCanvas.measureText("+" + font));


    textTextureCT = Disposable.assign(textTextureCT,
        createTextTexture(true, measured, font, fontDesk, api.graphics));
    textTextureBW = Disposable.assign(textTextureBW,
        createTextTexture(false, measured, font, fontDesk, api.graphics));
    applyToRect(text1Rect, textTextureCT);
    applyToRect(text2Rect, textTextureBW);


    text1Rect.color.set(1,1,1,1);
    text1Rect.setBgColor(bgColor);
    text2Rect.color.set(1,1,1,1);
    text2Rect.setBgColor(bgColor);
  }

  private void applyToRect(TextRect rect, GL.Texture texture) {
    rect.setTextureRegionDefault(texture);
    rect.setSizeToTextureRegion();
  }

  void layout(V2i clientRect, float dpr) {
    clientSize.set(clientRect);
    cSizeHalf.set(clientRect.x / 2, clientRect.y / 2);

    int y0 = clientSize.y / 4 - text1Rect.size.y / 2;
    text1Rect.pos.set(clientRect.x / 4 - measured / 2, y0);
    text2Rect.pos.set(clientRect.x * 3/ 4 - measured / 2, y0);
  }

  public void dispose() {
    textTextureCT = Disposable.assign(textTextureCT, null);
    textTextureBW = Disposable.assign(textTextureBW, null);
  }

  private void drawSomeText(String s, Canvas canvas) {
    canvas.drawText(s, 0, 20);
    canvas.drawText(s, .25f, 40);
    canvas.drawText(s, .5f, 60);
    canvas.drawText(s, .75f, 80);
    canvas.drawText(s, 1f, 100);
  }

  private void setRandomColor(TextRect color) {
    double hText = Math.random();
    double hBg = fixHue(hText - .5 + (Math.random() - .5) * .125);

    double s = .7 + Math.random() * .3;
    double v = .5 + Math.random() * .5;
    Color.Cvt.fromHSV(hText, s, v, 1, color.color);
    Color.Cvt.fromHSV(hBg, s, v, 1, color.bgColor);
  }

  private GL.Texture createTextTexture(
      boolean cleartype, int measured,
      String font, FontDesk fontDesk, WglGraphics g
  ) {
    Canvas canvas = g.createCanvas(measured, 120, cleartype);
    canvas.setFont(fontDesk);

    drawSomeText((cleartype ? "+" : "-" ) + font + text, canvas);

    GL.Texture texture = g.createTexture();
    texture.setContent(canvas);
    canvas.dispose();
    return texture;
  }

  public boolean update(double timestamp) {
    // repaint only we need
    return false;
  }

  public void paint() {
    WglGraphics g = api.graphics;

    g.clear(bgColor);
    g.enableBlend(false);

    int yR = clientSize.y - cSizeHalf.y;
    int xR = clientSize.x - cSizeHalf.x;
    g.drawRect(0, 0, cSizeHalf, text1Rect.bgColor);
    g.drawRect(xR, 0, cSizeHalf, text2Rect.bgColor);
    g.drawRect(0, yR, cSizeHalf, text1Rect.color);
    g.drawRect(xR, yR, cSizeHalf, text2Rect.color);

    int y0 = text1Rect.pos.y;
    int y1 = clientSize.y * 3 / 4 - text1Rect.size.y / 2;
    text1Rect.drawText(g, text1Rect.pos.x, y0, textTextureCT, false, true);
    text1Rect.drawText(g, text1Rect.pos.x, y1, textTextureCT, true, true);
//    g.enableBlend(true);
    text2Rect.drawText(g, text2Rect.pos.x, y0, textTextureBW, false, false);
    text2Rect.drawText(g, text2Rect.pos.x, y1, textTextureBW, true, false);
//    g.enableBlend(false);

    g.checkError("paint complete ");
  }

  public void onResize(V2i size, float dpr) {
    layout(size, dpr);
  }

  class MyInputListener implements MouseListener {
    @Override
    public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
      if (button == MouseListener.MOUSE_BUTTON_LEFT && clickCount == 2) {
        setRandomColor(text1Rect);
        text2Rect.color.set(text1Rect.color);
        text2Rect.bgColor.set(text1Rect.bgColor);
      }
      return true;
    }
  }

}
