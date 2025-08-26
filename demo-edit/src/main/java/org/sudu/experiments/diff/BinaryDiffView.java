package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.UiFont;
import org.sudu.experiments.ui.window.ScrollContent;

import java.util.Objects;

public class BinaryDiffView extends ScrollContent {

  public static final float digitPad = 0.5f;
  public static final float pairPad = 3;
  private UiContext uiContext;
  private EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  Color editBg, textFg, vLine;

  final V2i cellSize = new V2i();
  final V4f texRect = new V4f();

  // a a d d r e s s │ aa bb cc dd ║ aa bb cc dd
  // a a d d r e s s │ aa bb cc dd ║ aa bb cc dd
  // a a d d r e s s │ aa bb cc dd ║ aa bb cc dd

  UiFont uiFont;
  FontDesk fd;
  GL.Texture texture;

  public BinaryDiffView(UiContext uiContext) {
    this.uiContext = uiContext;
    readTheme();
  }

  @Override
  public void dispose() {
    texture = Disposable.dispose(texture);
  }

  public void setTheme(EditorColorScheme colors) {
    theme = colors;
    UiFont oldFont = uiFont;
    readTheme();

    boolean sameFont = Objects.equals(oldFont, colors.editorFont);
    if (!sameFont) {
      if (dpr != 0)
        changeFont();
    }
  }

  private void readTheme() {
    editBg = theme.editor.bg;
    vLine = theme.editor.numbersVLine;
    textFg = theme.codeElement[0].colorF;
    uiFont = theme.editorFont.scale(1.5f);
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    if (uiFont != null)
      changeFont();
  }

  private void changeFont() {
//    .fontDesk(name, pixelSize, weightRegular, FontDesk.STYLE_NORMAL)
    WglGraphics g = uiContext.graphics;
    fd = g.fontDesk(uiFont, dpr, false);
    String all = "0123456789ABCDEF";
    char[] cArray = new char[2];
    float[] measure = new float[256];
    int wMax = 0;
    g.mCanvas.setFont(fd);
    for (int y = 0; y < 16; y++) {
      cArray[0] = all.charAt(y);
      for (int x = 0; x < 16; x++) {
        cArray[1] = all.charAt(x);
        String digit = new String(cArray);
        float measured = g.mCanvas.measureText(digit);
        measure[y * 16 + x] = measured;
        int m = (int) (measured + 15 / 16f);
        wMax = Math.max(wMax, m);
        System.out.println("s = " + digit + ", m = " + m + "px");
      }
    }
    int lineHeight = fd.lineHeight(EditorConst.LINE_HEIGHT_MULTI);
    float baseline = fd.baselineCenterF(lineHeight);
    System.out.println("baseline = " + baseline);
    System.out.println("wMax = " + wMax);
    cellSize.y = lineHeight;
    cellSize.x = wMax;
    Canvas c = g.createCanvas(wMax * 16, lineHeight, true);
    c.setFont(fd);
    for (int i = 0; i < 16; i++) {
      cArray[0] = all.charAt(i);
      cArray[1] = all.charAt(i);
      String digit = new String(cArray);
      float x = i * wMax + 0 * 0.5f * (wMax - measure[i * 17]);
      System.out.println("s = " + digit + ", x = " + x + ", m = " + measure[i] + "px");
      c.drawText(digit, x, baseline);
    }
    texture = g.createTexture(c);
    c.dispose();

  }

  @Override
  public V2i minimalSize() {
    int px20 = DprUtil.toPx(20, dpr);
    return new V2i(px20, px20);
  }

  @Override
  public void draw(WglGraphics g) {
    g.enableScissor(pos, size);
    g.drawRect(pos.x, pos.y, size, editBg);
    for (int i = 0; i < 16; i++) {

    }
    texRect.set(0,0, texture.width(), texture.height());
    g.drawText(pos.x,pos.y, texture.size(), texRect, texture,
        textFg, editBg, true);
    g.disableScissor();
  }
}
