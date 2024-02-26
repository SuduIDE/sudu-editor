package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.IdeaCodeColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.UiFont;

import static org.sudu.experiments.ui.fonts.Codicons.*;

public class CodiconDemo extends Scene0 {
  static final String text = ""
      + chevron_right + chevron_down
      + diff_single + diff_multiple
      + chrome_minimize + chrome_maximize
      + chrome_restore + chrome_close
      + folder + folder_opened
      + file + file_code + file_binary;

  final V4f bgColor = new Color(20);
  final TextRect textRect = new TextRect(0, 0, 300, 300);

  GL.Texture texture;
  Canvas textCanvas;
  UiFont f = new UiFont(Fonts.codicon, 80);

  public CodiconDemo(SceneApi api) {
    super(api);
    api.input.onKeyPress.add(this::onKeyEvent);
  }

  public void dispose() {
    texture = Disposable.assign(texture, null);
    textCanvas = Disposable.assign(textCanvas, null);
  }

  private void renderIcons() {
    WglGraphics graphics = api.graphics;
    FontDesk codicon = graphics.fontDesk(f.familyName, f.size, dpr);

    graphics.mCanvas.setFont(codicon);
    float measureText = graphics.mCanvas.measureText(text);
//    float measureText2 = graphics.mCanvas.measureText(text2);
    int w = Numbers.iRnd(measureText);
    System.out.println("codicon.pixel.size = " + codicon.size);

    int lineHeight = codicon.lineHeight(Fonts.codiconLineHeight);
    System.out.println("w = " + w + ", lineHeight = " + lineHeight);
    textCanvas = graphics.createCanvas(w, lineHeight);
    textCanvas.setFont(codicon);
    textCanvas.drawText(text, 0, codicon.fAscent);
//    textCanvas.drawText(text2, 0, lineHeight + codicon.fAscent);
//    textCanvas.drawText(text3, 0, lineHeight * 2 + codicon.fAscent);

    texture = graphics.createTexture();
    texture.setContent(textCanvas);

    textRect.setTextureRegionDefault(texture);
    textRect.setSizeToTextureRegion();
    textRect.bgColor.set(IdeaCodeColors.Darcula.editBg);
    textRect.color.set(IdeaCodeColors.Darcula.defaultText);

  }

  public void paint() {
    if (texture == null) renderIcons();
    WglGraphics g = api.graphics;
    g.clear(bgColor);
    textRect.pos.set(
        (size.x - textRect.size.x) / 2,
        (size.y - textRect.size.y) / 2);
    textRect.drawText(g, texture, 0, 0);
  }

  boolean onKeyEvent(KeyEvent event) {
    return false;
  }
}
