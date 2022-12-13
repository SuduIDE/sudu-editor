package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

public enum CodeColors {

  defaultText(Colors.defaultTextC),
  keyword(new Color(204, 120, 50)),
  field(new Color("#9876AA")),
  string(new Color("#6A8759")),
  comma(new Color("#CC7832")),
  error(new Color(188, 63, 60)),
  unused(new Color("#72737A")),
  number(new Color("#6897BB")),
  method(new Color("#FFC66D")),
  showUsage(Colors.defaultTextC, new Color(52, 65, 52)),
  braceMatch(new Color("#FFEF28"), new Color("#3B514D"));

  public final CodeElementColor v;

  CodeColors(CodeElementColor color) { v = color; }
  CodeColors(Color color) { v = Colors.makeDefault(color); }
  CodeColors(Color color, Color bgColor) { v = Colors.makeDefault(color, bgColor); }

  public static CodeElementColor[] toArray() {
    CodeColors[] values = CodeColors.values();
    CodeElementColor[] array = new CodeElementColor[values.length];
    for (int i = 0; i < values.length; i++) array[i] = values[i].v;
    return array;
  }
}
