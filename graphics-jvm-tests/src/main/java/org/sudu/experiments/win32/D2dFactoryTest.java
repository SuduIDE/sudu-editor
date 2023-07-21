package org.sudu.experiments.win32;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.Codicon;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.fonts.JetBrainsMono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class D2dFactoryTest {
  public static void main(String[] args) throws IOException {
    Helper.loadDlls();
    Win32.coInitialize();

    D2dFactory f = D2dFactory.create(JetBrainsMono.regular(), Codicon.fontResource());

    int size = 50;
    FontDesk jbMono = f.getFont(Fonts.JetBrainsMono,
        size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
    FontDesk font2 = f.getFont(Fonts.JetBrainsMono,
        size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);

    if (jbMono != font2) throw new RuntimeException("jbMono != font2");

    FontDesk segoeUI = f.getFont(Fonts.SegoeUI,
        size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);
    FontDesk consolas = f.getFont(Fonts.Consolas,
        size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);

    FontDesk codicon = f.getFont(Fonts.codicon,
        size, FontDesk.WEIGHT_REGULAR, FontDesk.STYLE_NORMAL);

    String text = "Text";

    D2dCanvas canvas = f.create(size * 3, size * 3 / 2);

    renderImage(jbMono, text, canvas, "jbMono.bmp");
    canvas.clear();
    renderImage(segoeUI, text, canvas, "segoeUI.bmp");
    canvas.clear();
    renderImage(consolas, text, canvas, "consolas.bmp");
    canvas.clear();
    renderImage(codicon, "\uEB45", canvas, "codicon.bmp");

    canvas.dispose();
    f.dispose();
  }

  static void renderImage(
      FontDesk jbMono, String text, D2dCanvas canvas, String file
  ) throws IOException {
    canvas.setFont(jbMono);
    canvas.drawText(text, 0, jbMono.fAscent);
    GL.ImageData image = canvas.toImage();
    byte[] bmp = BmpWriter.toBmp(image);

    Files.write(Path.of(file), bmp);
  }

}
