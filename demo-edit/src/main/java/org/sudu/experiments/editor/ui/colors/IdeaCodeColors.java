package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;

// todo: add import from XML

public interface IdeaCodeColors {

  static CodeElementColor[] codeElementColorsDarcula() {
    ElementsDarcula[] values = ElementsDarcula.values();
    CodeElementColor[] array = new CodeElementColor[values.length];
    for (int i = 0; i < values.length; i++) array[i] = values[i].v;
    return array;
  }

  static CodeElementColor[] codeElementColorsDark() {
    ElementsDark[] values = ElementsDark.values();
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

  enum ElementsDarcula {
    defaultText(Darcula.defaultText),
    keyword(Darcula.keyword),
    nullLiteral(Darcula.keyword),
    booleanLiteral(Darcula.keyword),
    semi(Darcula.keyword),
    field("#9876AA"),
    string("#6A8759"),
    error("#BC3F3C"),
    number("#6897BB"),
    method("#FFC66D"),
    comment("#808080"),
    annotation("#BBB529"),
    type(Darcula.defaultText), // Old color: #287BDE
    operator("#5F8C8A"),
    escape(Darcula.keyword),
    cppDirective(Darcula.keyword),
    documentation("#629755"),

    br1(Darcula.defaultText),
    br2(Darcula.defaultText),
    br3(Darcula.defaultText);

    public final CodeElementColor v;

    ElementsDarcula(String color) {
      this(new Color(color));
    }

    ElementsDarcula(Color color) {
      v = new CodeElementColor(color, null);
    }

    ElementsDarcula(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  enum ElementsLight {
    defaultText(Light.defaultText),
    keyword(Light.keyword),
    nullLiteral(Light.keyword),
    booleanLiteral(Light.keyword),
    semi(Light.defaultText),
    field("#871094"),
    string("#3C7C16"),
    error("#F93900"),
    number("#164FF1"),
    method("#396179"),
    comment("#808080"),
    annotation("#BBB529"),
    type("#287BDE"),
    operator("#5F8C8A"),
    escape(Light.keyword),
    cppDirective(Light.keyword),
    documentation("#8C8C8C"),

    br1(Light.defaultText),
    br2(Light.defaultText),
    br3(Light.defaultText);

    public final CodeElementColor v;

    ElementsLight(String color) {
      this(new Color(color));
    }

    ElementsLight(Color color) {
      v = new CodeElementColor(color, null);
    }

    ElementsLight(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  enum ElementsDark {
    defaultText(Dark.defaultText),
    keyword(Dark.keyword),
    nullLiteral(Dark.keyword),
    booleanLiteral(Dark.keyword),
    semi(Dark.defaultText),
    field("#C77DBB"),
    string("#6AAB73"),
    error(Dark.error),
    number("#2AACB8"),
    method("#56A8F5"),
    comment("#7A7E85"),
    annotation("#B3AE60"),
    type(Dark.defaultText),
    operator("#5F8C8A"),
    escape(Dark.keyword),
    cppDirective(Dark.keyword),
    documentation("#5F826B"),

    br1(Dark.defaultText),
    br2(Dark.defaultText),
    br3(Dark.defaultText);

    public final CodeElementColor v;

    ElementsDark(String color) {
      this(new Color(color));
    }

    ElementsDark(Color color) {
      v = new CodeElementColor(color, null);
    }

    ElementsDark(Color color, Color bgColor) {
      v = new CodeElementColor(color, bgColor);
    }
  }

  interface Darcula {
    Color cursor = new Color(187);
    Color defaultText = new Color("#A9B7C6");
    Color editNumbersVLine = new Color(55);
    Color editBg = new Color(43);
    Color editSelectedBg = new Color(33, 66, 131);
    Color editFooterFill = new Color(60, 63, 65);
    Color usageBgColor = new Color("#344134");
    Color definitionBgColor = new Color("#40332B");
    Color scrollBarLine = new Color(85, 85, 85, 128);
    Color scrollBarBg = new Color(43, 43, 43, 0);
    Color caretBg = new Color("#323232");
    Color keyword = new Color("#CC7832");
  }

  interface Dark {
    Color cursor = new Color(206);
    Color defaultText = new Color("#BCBEC4");
    Color editNumbersVLine = new Color("#313438");
    Color editBg = new Color("#1E1F22");
    Color editSelectedBg = new Color("#214283");
    Color editFooterFill = new Color("#2B2D30");
    Color usageBgColor = new Color("#373B39");
    Color definitionBgColor = new Color("#402F33");
    Color scrollBarLine = new Color(107, 106, 107, 128);
    Color scrollBarBg = new Color(30, 31, 34, 0);
    Color caretBg = new Color("#26282E");
    Color error = new Color("#F75464");
    Color unused = new Color("#6F737A");
    Color keyword = new Color("#CF8E6D");
  }

  interface Light {
    Color cursor = new Color(0);
    Color defaultText = new Color(0x08, 0x08, 0x08);
    Color editNumbersVLine = new Color("#EBECF0");
    Color editBg = new Color(0xff, 0xff, 0xff);
    Color editSelectedBg = new Color("#A6D2FF");
    Color editFooterFill = new Color("#F8F9FB");
    Color usageBgColor = new Color("#edebfc");
    Color definitionBgColor = new Color("#FCE8F4");
    Color scrollBarLine = new Color(205, 205, 205, 153);
    Color scrollBarBg = new Color(255, 255, 255, 0);
    Color caretBg = new Color("#F5F8FE");
    Color keyword = new Color("#0033b3");
  }
}
