package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

public class CodeElementColor {
  public final Color colorF;
  public final Color colorB;
  public final Color selColorF;
  public final Color selColorB;

  public CodeElementColor(Color colorF, Color colorB, Color selColorF, Color selColorB) {
    this.colorF = colorF;
    this.colorB = colorB;
    this.selColorF = selColorF;
    this.selColorB = selColorB;
  }

  public CodeElementColor(Color colorF, Color colorB, Color selColorB) {
    this(colorF, colorB, i(colorF), selColorB);
  }

  static Color i(Color c) {
    return new Color(255 - c.r, 255- c.g, 255-c.b, c.a);
  }

}
