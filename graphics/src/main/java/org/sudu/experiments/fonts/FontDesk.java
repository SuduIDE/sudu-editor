package org.sudu.experiments.fonts;

import org.sudu.experiments.Debug;
import org.sudu.experiments.math.Numbers;

public class FontDesk {
  public static final int WEIGHT_LIGHT     = 300;
  public static final int WEIGHT_REGULAR   = 400;
  public static final int WEIGHT_SEMI_BOLD = 600;
  public static final int WEIGHT_BOLD      = 700;

  public static final int STYLE_NORMAL  = 0;
  public static final int STYLE_OBLIQUE = 1;
  public static final int STYLE_ITALIC  = 2;

  public static final String OBLIQUE = "oblique";
  public static final String ITALIC = "italic";
  public static final String NORMAL = "normal";

  public final String name;
  public final String sStyle;
  public final float size;
  public final int iSize;
  public final int weight;
  public final int style;

  public final int   iAscent, iDescent;
  public final float fAscent, fDescent;

  public final float spaceWidth, WWidth;
  public final boolean monospace;

  public final Object platformFont;

  // todo: move platformFont to subclass to avoid TeaVM JS Wrappers logic
  public FontDesk(
      String name, float size, int weight, int style,
      float ascent, float descent,
      float spaceWidth, float WWidth, float dotWidth,
      Object platformFont
  ) {
    this.name = name;
    this.size = size;
    this.iSize = iSize(size);
    this.weight = weight;
    this.style = style;
    this.fAscent = ascent;
    this.fDescent = descent;
    this.spaceWidth = spaceWidth;
    this.WWidth = WWidth;
    this.platformFont = platformFont;

    this.iAscent = Numbers.iRnd(fAscent);
    this.iDescent = Numbers.iRnd(fDescent);
    monospace = monospace(spaceWidth, WWidth, dotWidth);
    sStyle = stringStyle(style);
//    debug(dotWidth);
  }

  private void debug(float dotWidth) {
    Debug.consoleInfo("new FontDesk: font " + name
        + ", size " + size + ", style = " + sStyle + ", weight " + weight);
    Debug.consoleInfo("  font ascent = ", fAscent);
    Debug.consoleInfo("  font descent = ", fDescent);

    int dotSize = (int) (dotWidth * 32);
    int spaceSize = (int) (spaceWidth * 32);
    int WSize = (int) (WWidth * 32);
    Debug.consoleInfo("  '.' size * 32 = " + dotSize);
    Debug.consoleInfo("  'W' size * 32 = " + WSize);
    Debug.consoleInfo("  ' ' size * 32 = " + spaceSize);
    Debug.consoleInfo("  monospace = " + monospace);
    Debug.consoleInfo("  platformFont = ", platformFont);
  }

  // usually this is very near to (size * 1.2)
  public int lineHeight() {
    return Numbers.iRnd(fAscent + fDescent);
  }

  public float lineHeightF() {
    return fAscent + fDescent;
  }

  public int lineHeight(float scale) {
    return Numbers.iRnd((fAscent + fDescent) * scale);
  }

  public float uiBaseline() {
    return fAscent - (fAscent + fDescent) / 16;
  }

  public int caretHeight(int lineHeight) {
    return lineHeight();
//    int caretOffer = lineHeight();
//    return lineHeight - (lineHeight / 2 - caretOffer / 2) * 2;
  }

  static int iSize(float size) {
    int iSize = (int)size;
    if (iSize != size) {
      Debug.consoleInfo("FontDesk::FontDesk iSize != size: " + size);
    }
    return iSize;
  }

  static boolean monospace(float spaceWidth, float WWidth, float dotWidth) {
    int dotSize = (int) (dotWidth * 32);
    int spaceSize = (int) (spaceWidth * 32);
    int WSize = (int) (WWidth * 32);

    return spaceSize == dotSize && spaceSize == WSize;
  }

  static String stringStyle(int style) {
    return switch (style) {
      case 1 -> OBLIQUE;
      case 2 -> ITALIC;
      default -> NORMAL;
    };
  }
}
