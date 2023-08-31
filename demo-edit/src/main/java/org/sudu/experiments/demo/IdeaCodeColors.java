package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

public interface IdeaCodeColors {

  enum ElementsDarcula {
    defaultText(Darcula.defaultText),
    keyword(new Color(204, 120, 50)),
    field(new Color("#9876AA")),
    string(new Color("#6A8759")),
    comma(new Color("#CC7832")),
    error(new Color(188, 63, 60)),
    unused(new Color("#72737A")),
    number(new Color("#6897BB")),
    method(new Color("#FFC66D")),
    showUsage(Darcula.defaultText, new Color(52, 65, 52)),
    braceMatch(new Color("#FFEF28"), new Color("#3B514D")),
    comment(new Color("#808080")),
    annotation(new Color("#BBB529")),
    type(new Color("#287BDE")),
    operator(new Color("#5F8C8A"));

    public final CodeElementColor v;

    ElementsDarcula(Color color) {
      v = new CodeElementColor(color, null);
    }

    ElementsDarcula(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  // TODO: get colors
  enum ElementsDark {
    defaultText(Darcula.defaultText),
    keyword(new Color(204, 120, 50)),
    field(new Color("#9876AA")),
    string(new Color("#6A8759")),
    comma(new Color("#CC7832")),
    error(new Color(188, 63, 60)),
    unused(new Color("#72737A")),
    number(new Color("#6897BB")),
    method(new Color("#FFC66D")),
    showUsage(Darcula.defaultText, new Color(52, 65, 52)),
    braceMatch(new Color("#FFEF28"), new Color("#3B514D")),
    comment(new Color("#808080")),
    annotation(new Color("#BBB529")),
    type(new Color("#287BDE")),
    operator(new Color("#5F8C8A"));

    public final CodeElementColor v;

    ElementsDark(Color color) {
      v = new CodeElementColor(color, null);
    }

    ElementsDark(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  enum ElementsLight {
    defaultText(Light.defaultText),
    keyword(new Color(0x00, 0x31, 0xbf)),
    field(new Color("#C44193")),
    string(new Color("#3C7C16")),
    comma(Light.defaultText),
    error(new Color("#F93900")),
    unused(new Color("#808080")),
    number(new Color("#164FF1")),
    method(new Color("#396179")),
    showUsage(Light.defaultText, new Color(0xed, 0xeb, 0xfc)),
    braceMatch(new Color("#FFEF28"), new Color("#93D9D9")),
    comment(new Color("#808080")),
    annotation(new Color("#BBB529")),
    type(new Color("#287BDE")),
    operator(new Color("#5F8C8A"));

    public final CodeElementColor v;

    ElementsLight(Color color) {
      v = new CodeElementColor(color, null);
    }

    ElementsLight(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  static CodeElementColor[] codeElementColors() {
    ElementsDarcula[] values = ElementsDarcula.values();
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


  interface Darcula {
    Color cursor = new Color(Caret.grayColor);
    Color defaultText = new Color("#A9B7C6");
    Color editNumbersVLine = new Color(85);
    Color editBg = new Color(43);
    Color editSelectedBg = new Color(33, 66, 131);
    Color editFooterFill = new Color(60, 63, 65);
    Color usageBgColor = new Color("#344134");
    Color definitionBgColor = new Color("#40332B");
    Color scrollBarLine = new Color("#565656");
    Color scrollBarBg = new Color(43, 43, 43, 0);
  }

  interface Dark {

  }

  interface Light {
    Color cursor = new Color(0);
    Color defaultText = new Color(0x00, 0x00, 0x00);
    Color editNumbersVLine = new Color(0xd4, 0xd4, 0xd4);
    Color editBg = new Color(0xff, 0xff, 0xff);
    Color editSelectedBg = new Color(0xa6, 0xd6, 0xff);
    Color editFooterFill = new Color(60, 63, 65);
    Color usageBgColor = new Color("#edebfc");
    Color definitionBgColor = new Color("#edebfc");
    Color scrollBarLine = new Color("#D8D8D8");
    Color scrollBarBg = new Color("#FFFFFF");
  }
}
