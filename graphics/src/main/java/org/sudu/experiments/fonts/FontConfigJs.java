package org.sudu.experiments.fonts;

import org.sudu.experiments.math.ArrayOp;

public class FontConfigJs {
  public final String family;
  public final String file;
  public final String style;
  public final int weight;

  public FontConfigJs(String family, String file, String style, int weight) {
    this.family = family;
    this.file = file;
    this.style = style;
    this.weight = weight;
  }

  public static FontConfigJs[] codiconFontConfig(String filename) {
    return ArrayOp.array(
        new FontConfigJs(Fonts.codicon, filename,
            FontDesk.NORMAL, FontDesk.WEIGHT_REGULAR));
  }
}
