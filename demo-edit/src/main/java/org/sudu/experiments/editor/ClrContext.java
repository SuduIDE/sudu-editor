package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.UiFont;

import static org.sudu.experiments.editor.UnderlineConstants.scaleSinParams;
import static org.sudu.experiments.editor.UnderlineConstants.sinParamsDefault;

public class ClrContext {
  public final V4f tRegion = new V4f();
  public final V2i size = new V2i();
  public final V2i underlineSize = new V2i();
  public final V4f underlineParams = new V4f();
  public final LineDiff ld = new LineDiff(0);

  final FontDesk[] fonts = new FontDesk[4];
  final GL.Texture[] textures0 = new GL.Texture[0];

  public FontDesk font;
  public Canvas renderingCanvas;

  float underlineOffset;
  public int lineHeight, underline, underlineHBox;
  public boolean cleartype;

  public ClrContext(boolean cleartype) {
    this.cleartype = cleartype;
    sinParamsDefault(underlineParams);
  }

  public void dispose() {
    renderingCanvas = Disposable.assign(renderingCanvas, null);
  }

  public void enableCleartype(boolean en, WglGraphics g) {
    cleartype = en;
    if (renderingCanvas.cleartype != cleartype && lineHeight != 0) {
      createRenderingCanvas(g);
    }
  }

  public void setFonts(String name, float pixelSize, WglGraphics g) {
    font = EditorConst.setFonts(name, pixelSize, fonts, g);
  }

  public void setFonts(UiFont newFont, float dpr, WglGraphics g) {
    font = EditorConst.setFonts(newFont, dpr, fonts, g);
  }

  public float fontSize() {
    return font != null ? font.size : 0;
  }

  public int setLineHeight(float lineHeightMulti, WglGraphics g) {
    int fontLineHeight = font.lineHeight();
    lineHeight = Numbers.iRnd(fontLineHeight * lineHeightMulti);
    underline = font.underlineShift(lineHeight);
    createRenderingCanvas(g);
    return lineHeight;
  }

  public void setSinDpr(float dpr) {
    sinParamsDefault(underlineParams);
    scaleSinParams(underlineParams, dpr, underlineParams);
    underlineOffset = UnderlineConstants.offset(underlineParams);
    underlineHBox = UnderlineConstants.boxExtend(underlineParams);
    underlineSize.set(0, underlineHBox * 2);
  }

  public void createRenderingCanvas(WglGraphics g) {
    renderingCanvas = Disposable.assign(renderingCanvas,
        g.createCanvas(EditorConst.TEXTURE_WIDTH, lineHeight, cleartype));
  }

  public void drawText(WglGraphics g, GL.Texture texture, int xPos, int yPos, V4f colorF, V4f bgColor) {
    if (size.x == 0 || size.y == 0 ||
        tRegion.w == 0 || tRegion.z == 0) return;

    g.drawText(xPos, yPos, size, tRegion,
        texture, colorF, bgColor, cleartype);
  }

  public void drawIcon(
      WglGraphics g, GL.Texture icon,
      int xPos, int yPos, V4f bgColor, V4f colorF
  ) {
    tRegion.set(0, 0, icon.width(), icon.height());
    size.set(icon.size());
    drawText(g, icon, xPos, yPos, colorF, bgColor);
  }
}
