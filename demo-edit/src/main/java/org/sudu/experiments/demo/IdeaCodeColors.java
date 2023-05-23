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

  enum ElementsLight {
    defaultText(Colors.defaultTextLight),
    keyword(new Color(0x00, 0x31, 0xbf)),
    field(new Color("#C44193")),
    string(new Color("#3C7C16")),
    comma(Colors.defaultTextLight),
    error(new Color("#F93900")),
    unused(new Color("#808080")),
    number(new Color("#164FF1")),
    method(new Color("#396179")),
    showUsage(Colors.defaultText, new Color(0xed, 0xeb, 0xfc)),
    braceMatch(new Color("#FFEF28"), new Color("#93D9D9")),
    comment(new Color("#808080")),
    annotation(new Color("#BBB529"));

    public final CodeElementColor v;

    ElementsLight(Color color) {
      v = new CodeElementColor(color, null);
    }

    ElementsLight(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  static CodeElementColor[] codeElementColors() {
    Elements[] values = Elements.values();
    CodeElementColor[] array = new CodeElementColor[values.length];
    for (int i = 0; i < values.length; i++) array[i] = values[i].v;
    return array;
  }

  static CodeElementColor[] codeElementColorsLight() {
    ElementsLight[] values = ElementsLight.values();
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

  static LineNumbersColors lineNumberColorsLight() {
    return new LineNumbersColors(
        new Color(0xad),
        new Color(0xf2),
        new Color(0xad),
        new Color(0xfc, 0xfa, 0xed)
    );
  }

  interface Colors {
    Color editBgColor = new Color(43);
    Color defaultText = new Color("#A9B7C6");
    Color editNumbersVLine = new Color(85);
    Color editFooterFill = new Color(60, 63, 65);
    Color editSelectedBg = new Color(33, 66, 131);
    Color editBgColorLight = new Color(0xff, 0xff, 0xff);
    Color defaultTextLight = new Color(0x00, 0x00, 0x00);
    Color editNumbersVLineLight = new Color(0xd4, 0xd4, 0xd4);
    Color editSelectedBgLight = new Color(0xa6, 0xd6, 0xff);
  }
}