package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import static org.sudu.experiments.demo.IdeaCodeColors.Colors;

public class CodiconDemo extends Scene {
  static final String text1 = "\uEA84\uEA8F\uEA8C\uEA87\uEA90\uEA83\uEAB6\uEAB4";
  static final String text2 = "\uEADF\uEADC\uEA81\uEB23\uEAD2\uEAD0\uEA64\uEB49";

  final V4f bgColor = new Color(20);
  final TextRect textRect = new TextRect(0, 0, 300, 300);

  GL.Texture texture;
  Canvas textCanvas;

  public CodiconDemo(SceneApi api) {
    super(api);
    api.input.onKeyPress.add(this::onKeyEvent);

    WglGraphics graphics = api.graphics;

    FontDesk codicon = graphics.fontDesk(Fonts.codicon, 88);

    graphics.mCanvas.setFont(codicon);
    float measureText1 = graphics.mCanvas.measureText(text1);
    float measureText2 = graphics.mCanvas.measureText(text2);
    int w = Numbers.iRnd(Math.max(measureText1, measureText2));
    int lineHeight = codicon.lineHeight(Fonts.codiconLineHeight);
    System.out.println("w = " + w + ", lineHeight = " + lineHeight);
    textCanvas = graphics.createCanvas(w, lineHeight * 2);
    textCanvas.setFont(codicon);
    textCanvas.drawText(text1, 0, codicon.fAscent);
    textCanvas.drawText(text2, 0, lineHeight + codicon.fAscent);

    texture = graphics.createTexture();
    texture.setContent(textCanvas);

    textRect.setTextureRegionDefault(texture);
    textRect.setSizeToTextureRegion();
    textRect.bgColor.set(Colors.editBgColor);
    textRect.color.set(Colors.defaultText);
  }

  public void dispose() {
    texture = Disposable.assign(texture, null);
    textCanvas = Disposable.assign(textCanvas, null);
  }

  public void paint() {
    WglGraphics g = api.graphics;
    g.clear(bgColor);
    textRect.drawText(g, texture, 0, 0, 0.5f);
  }

  public void onResize(V2i size, float dpr) {
    textRect.pos.set(
            (size.x - textRect.size.x) / 2,
            (size.y - textRect.size.y) / 2);
  }

  boolean onKeyEvent(KeyEvent event) {
    return false;
  }
}
