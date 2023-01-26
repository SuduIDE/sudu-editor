package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class LineParser {

  StringTokenizer lineTokenizer;
  StringTokenizer wordTokenizer;

  private static final String LINE_DELIM = System.lineSeparator();
  private static final String WORD_DELIM = " \t()[]<>,.\";";

  public static final String PARSE_BYTES = "parseBytes";

  public static void parseBytes(byte[] bytes, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8);
    LineParser parser = new LineParser();
    int[] ints = parser.parseIntArray(source);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

  Document parse(String source) {
    List<CodeLine> lines = new ArrayList<>();
    List<CodeElement> words = new ArrayList<>();

    lineTokenizer = new StringTokenizer(source, LINE_DELIM);
    while (lineTokenizer.hasMoreTokens()) {
      String token = lineTokenizer.nextToken();
      String line = trim(token);
      wordTokenizer = new StringTokenizer(line, WORD_DELIM, true);
      words.clear();
      while (wordTokenizer.hasMoreTokens()) {
        String word = wordTokenizer.nextToken();
        words.add(plainText(word));
      }
      lines.add(new CodeLine(words.toArray(CodeElement[]::new)));
    }
    return new Document(lines.toArray(CodeLine[]::new));
  }

  // result[0] = N - number of lines
  // result[1..N] - number of elements on line
  // result[N+1..] - end index of word
  int[] parseIntArray(String source) {
    List<Integer> lines = new ArrayList<>();
    List<Integer> words = new ArrayList<>();
    int wordEnd = 0;
    boolean prevLine = false;

    lineTokenizer = new StringTokenizer(source, LINE_DELIM, true);
    while (lineTokenizer.hasMoreTokens()) {
      String line = lineTokenizer.nextToken();
      if (isCarriage(line)) {
        wordEnd++;
        continue;
      }
      if (isNewLine(line)) {
        wordEnd++;
        if (prevLine) lines.add(0);
        prevLine = true;
        continue;
      }
      prevLine = false;
      int wordCnt = 0;
      wordTokenizer = new StringTokenizer(line, WORD_DELIM, true);
      while (wordTokenizer.hasMoreTokens()) {
        String word = wordTokenizer.nextToken();
        wordEnd += word.length();
        wordCnt++;
        words.add(wordEnd);
      }
      lines.add(wordCnt);
    }
    int lineNumber = lines.size();
    int wordNumber = words.size();
    int[] result = new int[1 + lineNumber + wordNumber];
    result[0] = lineNumber;
    for (int i = 0; i < lineNumber; i++) {
      result[1 + i] = lines.get(i);
    }
    for (int i = 0; i < wordNumber; i++) {
      result[1 + lineNumber + i] = words.get(i);
    }
    Debug.consoleInfo("Parsing complete");
    return result;
  }

  private static boolean isNewLine(String str) {
    return str.length() == 1 && str.charAt(0) == '\n';
  }

  private static boolean isCarriage(String str) {
    return str.length() == 1 && str.charAt(0) == '\r';
  }

  private static boolean isQuote(String str) {
    return str.length() == 1 && str.charAt(0) == '\"';
  }

  private CodeElement plainText(String text) {
    return new CodeElement(text);
  }

  private String trim(String str) {
    int from = 0;
    int to = str.length();
    if (str.charAt(str.length() - 1) == '\r') to--;
    return str.substring(from, to);
  }

  public static Document makeDocument(int[] ints, char[] chars) {
    int numLines = ints[0];
    int wordStart = 0;
    boolean hasQuote = false;
    CodeLine[] newDoc = new CodeLine[numLines];
    for (int i = 0, wordInd = 1 + numLines; i < numLines; i++) {
      int len = ints[1 + i];
      CodeElement[] elements = new CodeElement[len];
      for (int j = 0; j < len; j++, wordInd++) {
        int wordEnd = ints[wordInd];
        String word = makeWord(wordStart, wordEnd, chars);
        if (isQuote(word)) {
          elements[j] = makeCodeElement(word, true);
          hasQuote = !hasQuote;
        } else {
          elements[j] = makeCodeElement(word, hasQuote);
        }
        wordStart = wordEnd;
      }
      newDoc[i] = new CodeLine(elements);
      wordStart++;
    }
    return new Document(newDoc);
  }

  private static CodeElement makeCodeElement(String word, boolean hasQuote) {
    if (hasQuote) return new CodeElement(word, 3);
    if (isNumeric(word)) return new CodeElement(word, 7);
    if (javaKeyWords.contains(word)) return new CodeElement(word, 1);
    return new CodeElement(word);
  }

  private static boolean isNumeric(String str) {
    if (str.isEmpty()) return false;
    if (str.charAt(0) == '-' && str.length() == 1) return false;
    if (!Character.isDigit(str.charAt(0))) return false;
    boolean hasDot = false;
    for (int i = 1; i < str.length(); i++) {
      if (str.charAt(i) == '.') {
        if (hasDot) return false;
        else hasDot = true;
      } else if (!Character.isDigit(str.charAt(i))) return false;
    }
    return true;
  }

  private static String makeWord(int from, int to, char[] chars) {
    while (from < chars.length && from < to && (chars[from] == '\n' || chars[from] == '\r')) from++;
    while (to - 1 > 0 && to > from && (chars[to - 1] == '\n' || chars[to - 1] == '\r')) to--;
    return new String(chars, from, to - from);
  }

  private static final Set<String> javaKeyWords = Set.of(
      "byte",
      "short",
      "int",
      "long",
      "float",
      "double",
      "if",
      "else",
      "switch",
      "case",
      "default",
      "do",
      "while",
      "for",
      "break",
      "continue",
      "return",
      "static",
      "final",
      "abstract",
      "native",
      "transient",
      "volatile",
      "synchronized",
      "strictfp",
      "private",
      "protected",
      "public",
      "class",
      "interface",
      "enum",
      "extends",
      "implements",
      "import",
      "package",
      "this",
      "super",
      "instanceof",
      "new",
      "try",
      "catch",
      "finally",
      "throw",
      "throws",
      "assert",
      "goto",
      "const",
      "var",
      "true",
      "false",
      "null",

      ";", ","
  );

}