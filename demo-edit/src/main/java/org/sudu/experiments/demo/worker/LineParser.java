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

  public static void parseFirstLines(byte[] bytes, int[] lines, Consumer<Object[]> result) {
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

  int[] parseIntArray(String source, int numOfLines) {
    List<Integer> lines = new ArrayList<>();
    boolean prevLine = false;
    int wordStart = 0;
    int N = 0;
    int M = 0;

    lineTokenizer = new StringTokenizer(source, LINE_DELIM, true);
    while (lineTokenizer.hasMoreTokens() && N < numOfLines) {
      String line = lineTokenizer.nextToken();
      if (isCarriage(line)) {
        wordStart++;
        continue;
      }
      if (isNewLine(line)) {
        wordStart++;
        N++;
        if (prevLine) {
          lines.add(-1);
          lines.add(-1);
        }
        prevLine = true;
        continue;
      }
      lines.add(wordStart);
      wordStart += line.length();
      lines.add(wordStart);
      prevLine = false;
      M++;
    }
    if (!lineTokenizer.hasMoreTokens()) N++;

    int[] result = new int[3 + N + 4 * M];
    result[0] = N;
    result[1] = M;
    result[2] = 0;
    for (int wordInd = 0, lineInd = 0; 2 * lineInd < lines.size(); lineInd++) {
      int start = lines.get(2 * lineInd);
      int stop = lines.get(2 * lineInd + 1);

      if (start == stop && start == -1) {
        result[3 + lineInd] = 0;
        continue;
      }
      result[3 + lineInd] = 1;
      result[3 + N + 4 * wordInd] = start;
      result[3 + N + 4 * wordInd + 1] = stop;
      result[3 + N + 4 * wordInd + 2] = 0;
      result[3 + N + 4 * wordInd + 3] = 0;
      wordInd++;
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

}