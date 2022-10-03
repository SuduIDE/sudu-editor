package org.sudu.experiments.demo;

import org.sudu.experiments.math.XorShiftRandom;

public class TestText {
  static CodeElement[] spaces() {
    return new CodeElement[]{
        new CodeElement("  ", Colors.defaultText),
        new CodeElement(" ", Colors.defaultText),
        new CodeElement(".", Colors.defaultText),
        new CodeElement("(", Colors.defaultText),
        new CodeElement(")", Colors.defaultText),
        new CodeElement(" + ", Colors.defaultText),
        new CodeElement("-", Colors.defaultText),
        new CodeElement(".", Colors.defaultText),
        new CodeElement("(", Colors.braceMatchF, Colors.braceMatchB),
        new CodeElement(")", Colors.braceMatchF, Colors.braceMatchB),
        new CodeElement(" + ", Colors.defaultText),
        new CodeElement(";", Colors.keyword),
        new CodeElement("{", Colors.keyword),
        new CodeElement("}", Colors.keyword),

        new CodeElement(" ", Colors.defaultText),
        new CodeElement(" ", Colors.defaultText),
        new CodeElement("(", Colors.defaultText),
        new CodeElement(",", Colors.comma),
        new CodeElement(",", Colors.comma),
        new CodeElement(" ", Colors.defaultText),
        new CodeElement(" ", Colors.defaultText),
        new CodeElement(",", Colors.comma),
        new CodeElement(" ", Colors.defaultText),
        new CodeElement(")", Colors.defaultText),
        new CodeElement(";", Colors.keyword)
    };
  }
  static CodeElement[] words() {
    return new CodeElement[]{
        new CodeElement("return", Colors.keyword),
        new CodeElement("System", Colors.defaultText),
        new CodeElement("Arrays", Colors.defaultText),
        new CodeElement("InputStream", Colors.unused),
        new CodeElement("out", Colors.field),
        new CodeElement("length", Colors.field),
        new CodeElement("println", Colors.defaultText, Colors.showUsageBg),
        new CodeElement("\"textCanvas.getFont() = \"", Colors.string),
        new CodeElement("textCanvas", Colors.error),
        new CodeElement("getFont", Colors.defaultText),
        new CodeElement("37", Colors.number),
        new CodeElement("\"ggg\"", Colors.string),
        new CodeElement("public", Colors.keyword),
        new CodeElement("static", Colors.keyword),
        new CodeElement("class", Colors.keyword),
        new CodeElement("extends", Colors.keyword),
        new CodeElement("implements", Colors.keyword),
        new CodeElement("interface", Colors.keyword),
        new CodeElement("boolean", Colors.keyword),
        new CodeElement("int", Colors.keyword),
        new CodeElement("double", Colors.keyword),
        new CodeElement("onMousePress", Colors.method),
        new CodeElement("MouseEvent", Colors.error),
        new CodeElement("event", Colors.unused),
        new CodeElement("boolean", Colors.keyword),
        new CodeElement("press", Colors.unused),
        new CodeElement("void", Colors.keyword),
        new CodeElement("clickCount", Colors.unused),
    };
  }

  public static CodeLine[] document(int nLines, boolean random) {
    CodeElement[] words = words();
    CodeElement[] spaces = spaces();
    int min = words.length / 5;
    int max = words.length - 1;
    XorShiftRandom r = random ? new XorShiftRandom() : new XorShiftRandom(1,2);
    CodeLine[] doc = new CodeLine[nLines];
    for (int i = 0; i < nLines; i++) {
      CodeElement[] nextLine = generateLine(words, spaces, min, max, r);
      doc[i] = new CodeLine(nextLine);
    }

    return doc;
  }

  public static CodeLine generateLine() {
    CodeElement[] words = words();
    CodeElement[] spaces = spaces();
    int min = words.length * 2 / 3;
    int max = words.length * 3 / 2;
    CodeElement[] line = generateLine(words, spaces, min, max, new XorShiftRandom());
    return new CodeLine(line);
  }

  private static CodeElement[] generateLine(CodeElement[] words, CodeElement[] spaces, int min, int max, XorShiftRandom r) {
    r.shuffle(words);
    CodeElement[] nextLine = new CodeElement[min + r.nextInt(max - min)];
    for (int j = 0; j < nextLine.length; j++) {
      nextLine[j] = (j & 1) == 1
          ? spaces[r.nextInt(spaces.length)]
          : words[j / 2];
    }
    return nextLine;
  }
}
