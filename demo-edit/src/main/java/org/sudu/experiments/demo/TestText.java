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
        new CodeElement("(", Colors.braceMatchF, Colors.braceMatchB, false, true),
        new CodeElement(")", Colors.braceMatchF, Colors.braceMatchB, false, true),
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
  static CodeElement[] words(XorShiftRandom r) {
    return new CodeElement[]{
        new CodeElement("return", Colors.keyword, r.nextInt(4)),
        new CodeElement("System", Colors.defaultText, r.nextInt(4)),
        new CodeElement("Arrays", Colors.defaultText, r.nextInt(4)),
        new CodeElement("InputStream", Colors.unused, r.nextInt(4)),
        new CodeElement("out", Colors.field, r.nextInt(4)),
        new CodeElement("length", Colors.field, r.nextInt(4)),
        new CodeElement("println", Colors.defaultText,  r.nextInt(4)),
        new CodeElement("\"textCanvas.getFont() = \"", Colors.string, r.nextInt(4)),
        new CodeElement("textCanvas", Colors.error, r.nextInt(4)),
        new CodeElement("getFont", Colors.defaultText, r.nextInt(4)),
        new CodeElement("37", Colors.number, r.nextInt(4)),
        new CodeElement("\"ggg\"", Colors.string, r.nextInt(4)),
        new CodeElement("public", Colors.keyword, r.nextInt(4)),
        new CodeElement("static", Colors.keyword, r.nextInt(4)),
        new CodeElement("class", Colors.keyword, r.nextInt(4)),
        new CodeElement("extends", Colors.keyword, r.nextInt(4)),
        new CodeElement("implements", Colors.keyword, r.nextInt(4)),
        new CodeElement("interface", Colors.keyword, r.nextInt(4)),
        new CodeElement("boolean", Colors.keyword, r.nextInt(4)),
        new CodeElement("int", Colors.keyword, r.nextInt(4)),
        new CodeElement("double", Colors.keyword, r.nextInt(4)),
        new CodeElement("onMousePress", Colors.method, r.nextInt(4)),
        new CodeElement("MouseEvent", Colors.error, r.nextInt(4)),
        new CodeElement("event", Colors.unused, r.nextInt(4)),
        new CodeElement("boolean", Colors.keyword, r.nextInt(4)),
        new CodeElement("press", Colors.unused, r.nextInt(4)),
        new CodeElement("void", Colors.keyword, r.nextInt(4)),
        new CodeElement("clickCount", Colors.unused, r.nextInt(4)),
    };
  }

  public static CodeLine[] document(int nLines, boolean random) {
    XorShiftRandom r = random ? new XorShiftRandom() : new XorShiftRandom(1,3);
    CodeElement[] words = words(r);
    CodeElement[] spaces = spaces();
    int min = words.length / 5;
    int max = words.length - 1;
    CodeLine[] doc = new CodeLine[nLines];
    for (int i = 0; i < nLines; i++) {
      CodeElement[] nextLine = generateLine(words, spaces, min, max, r);
      doc[i] = new CodeLine(nextLine);
    }

    return doc;
  }

  public static CodeLine generateLine() {
    CodeElement[] words = words(new XorShiftRandom());
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
