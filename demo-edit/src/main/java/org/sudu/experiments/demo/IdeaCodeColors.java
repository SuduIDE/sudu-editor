package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

public interface IdeaCodeColors {

  enum Elements {
    defaultText(Colors.defaultText),
    keyword(new Color(204, 120, 50)),
    field(new Color("#9876AA")),
    string(new Color("#6A8759")),
    comma(new Color("#CC7832")),
    error(new Color(188, 63, 60)),
    unused(new Color("#72737A")),
    number(new Color("#6897BB")),
    method(new Color("#FFC66D")),
    showUsage(Colors.defaultText, new Color(52, 65, 52)),
    braceMatch(new Color("#FFEF28"), new Color("#3B514D")),
    comment(new Color("#808080")),
    annotation(new Color("#BBB529"));

    public final CodeElementColor v;

    Elements(Color color) {
      v = new CodeElementColor(color, null);
    }

    Elements(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  static CodeElementColor[] codeElementColors() {
    Elements[] values = Elements.values();
    CodeElementColor[] array = new CodeElementColor[values.length];
    for (int i = 0; i < values.length; i++) array[i] = values[i].v;
    return array;
  }

  static LineNumbersColors lineNumberColors() {
    return new LineNumbersColors(
        new Color(0x60, 0x63, 0x66),
        new Color(0x31, 0x33, 0x35),
        new Color(0xA4, 0xA3, 0xA3),
        new Color(0x32)
    );
  }

  interface Colors {
    Color editBgColor = new Color(43);
    Color defaultText = new Color("#A9B7C6");
    Color editNumbersVLine = new Color(85);
    Color editFooterFill = new Color(60, 63, 65);
    Color editSelectedBg = new Color(33, 66, 131);
  }
}