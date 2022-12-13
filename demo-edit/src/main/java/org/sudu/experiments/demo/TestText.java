package org.sudu.experiments.demo;

import org.sudu.experiments.math.XorShiftRandom;

public class TestText {
  static CodeElement[] spaces() {
    int keyword = CodeColors.keyword.ordinal();
    int braceMatch = CodeColors.braceMatch.ordinal();

    CodeElement sp = new CodeElement(" ", 0);
    CodeElement br1 = new CodeElement("(", 0);
    CodeElement br2 = new CodeElement(")", 0);
    CodeElement plus = new CodeElement(" + ", 0);
    CodeElement minus = new CodeElement("-", 0);
    CodeElement comma = new CodeElement(",", CodeColors.comma.ordinal());
    CodeElement dot = new CodeElement(".", 0);
    CodeElement br3 = new CodeElement("{", keyword);
    CodeElement br4 = new CodeElement("}", keyword);
    CodeElement br5 = new CodeElement("(", braceMatch, false, true);
    CodeElement br6 = new CodeElement(")", braceMatch, false, true);
    CodeElement sp2 = new CodeElement("  ", 0);
    CodeElement semicolon = new CodeElement(";", keyword);
    return new CodeElement[] {
        sp2,
        sp, sp, sp, sp, sp, sp,
        br1, br2, br1, br2, br3, br4, br5, br6,
        plus, plus, minus, minus,
        comma, comma, comma, dot, dot,
        semicolon, semicolon
    };
  }

  static CodeElement[] words(XorShiftRandom r) {
    int numColors = CodeColors.values().length - 1;
    int string = CodeColors.string.ordinal();
    return new CodeElement[]{
        new CodeElement("return", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("System", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("Arrays", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("InputStream", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("out", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("length", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("println", r.nextInt(numColors),  r.nextInt(4)),
        new CodeElement("\"a string or text\"", string, r.nextInt(4)),
        new CodeElement("textCanvas", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("getFont", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("37", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("\"ggg\"", string, r.nextInt(4)),
        new CodeElement("public", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("static", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("class", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("extends", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("implements", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("interface", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("boolean", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("int", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("double", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("onMousePress", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("MouseEvent", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("event", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("boolean", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("press", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("void", r.nextInt(numColors), r.nextInt(4)),
        new CodeElement("clickCount", r.nextInt(numColors), r.nextInt(4)),
    };
  }

  public static CodeLine[] document(int nLines, boolean random) {
    XorShiftRandom r = random ? new XorShiftRandom() : new XorShiftRandom(1,3);
    CodeElement[] words = words(r);
    CodeElement[] spaces = spaces();
    int min = words.length / 5;
    int max = words.length * 2;
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

  private static CodeElement[] generateLine(
      CodeElement[] words, CodeElement[] spaces,
      int min, int max, XorShiftRandom r
  ) {
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
