package org.sudu.experiments.demo.worker;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.demo.CodeElement;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Consumer;

public class LineParser {

  StringTokenizer lineTokenizer;

  private static final String LINE_DELIM = "\r\n";

  public static final String PARSE_LINES = "LineParser.parseBytes";

  public static void parseBytes(byte[] bytes, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8);
    LineParser parser = new LineParser();
    int[] ints = parser.parseIntArray(source, Integer.MAX_VALUE);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

  public static final String PARSE_FIRST_LINES = "asyncLineParser.parseFirstLines";

  public static void parseFirstLines(FileHandle f, int[] lines, Consumer<Object[]> r) {
    f.readAsBytes(
        bytes -> parseFirstLines(bytes, lines, r),
        String::toString);
  }

  private static void parseFirstLines(byte[] bytes, int[] lines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseFirstLines(bytes, lines, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFirstLines(byte[] bytes, int[] lines, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8);
    LineParser parser = new LineParser();
    int numOfLines = lines[0];
    int[] ints = parser.parseIntArray(source, numOfLines);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

  // result[0] = N - number of lines
  // result[1..N] - number of elements on line
  // result[N+1..] - end index of word
  int[] parseIntArray(String source, int numOfLines) {
    List<Integer> lines = new ArrayList<>();
    boolean prevLine = false;
    int lineEnd = 0;

    lineTokenizer = new StringTokenizer(source, LINE_DELIM, true);
    while (lineTokenizer.hasMoreTokens() && lines.size() < numOfLines) {
      String line = lineTokenizer.nextToken();
      if (isCarriage(line)) {
        lineEnd++;
        continue;
      }
      if (isNewLine(line)) {
        lineEnd++;
        if (prevLine) lines.add(lineEnd);
        prevLine = true;
        continue;
      }
      lineEnd += line.length();
      prevLine = false;
      lines.add(lineEnd);
    }
    int lineNumber = lines.size();
    int[] result = new int[1 + lineNumber];
    result[0] = lineNumber;
    for (int i = 0; i < lineNumber; i++)
      result[1 + i] = lines.get(i);
    Debug.consoleInfo("Parsing complete");
    return result;
  }

  private static boolean isNewLine(String str) {
    return str.length() == 1 && str.charAt(0) == '\n';
  }

  private static boolean isCarriage(String str) {
    return str.length() == 1 && str.charAt(0) == '\r';
  }

  public static Document makeDocument(int[] ints, char[] chars) {
    int numLines = ints[0];
    int lineStart = 0;
    CodeLine[] newDoc = new CodeLine[numLines];
    for (int i = 0; i < numLines; i++) {
      CodeElement[] elements = new CodeElement[1];
      int lineEnd = ints[1 + i];
      String line = makeWord(lineStart, lineEnd, chars);
      elements[0] = new CodeElement(line);
      lineStart = lineEnd;
      newDoc[i] = new CodeLine(elements);
    }
    return new Document(newDoc);
  }

  private static String makeWord(int from, int to, char[] chars) {
    while (from < chars.length && from < to && (chars[from] == '\n' || chars[from] == '\r')) from++;
    while (to - 1 > 0 && to > from && (chars[to - 1] == '\n' || chars[to - 1] == '\r')) to--;
    return new String(chars, from, to - from);
  }

}