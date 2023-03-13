package org.sudu.experiments.win32;

import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.GL;
import org.sudu.experiments.LazyInit;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.function.Consumer;

public class Win32Graphics extends WglGraphics {

  final D2dFactory canvasFactory;

  public Win32Graphics(D2dFactory canvasFactory) {
    super(new Win32AngleGL(), canvasFactory);
    this.canvasFactory = canvasFactory;
  }

  public static LazyInit<Win32Graphics> lazyInit(D2dFactory cf) {
    return new LazyInit<>() {
      protected Win32Graphics create() {
        return new Win32Graphics(cf);
      }
    };
  }

  public FontDesk fontDesk(String family, float size, int weight, int style) {
    return canvasFactory.getFont(family, size, weight, style);
  }

  public void loadImage(String src, Consumer<GL.Texture> onLoad) {}

  public Win32AngleGL getAngleGl() {
    return (Win32AngleGL) gl;
  }

  XorShiftRandom random;

  XorShiftRandom random() {
    return random != null ? random : (random = new XorShiftRandom());
  }

}
