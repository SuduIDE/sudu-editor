package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.Debug;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.editor.worker.proxy.FileProxy;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;

public class LineParser {

  StringTokenizer lineTokenizer;

  private static final String LINE_DELIM = "\r\n";

  public static final String PARSE = "LineParser.parse";

  public static void parse(char[] chars, List<Object> result) {
    String source = new String(chars);
    LineParser parser = new LineParser();
    int[] ints = parser.parseIntArray(source, Integer.MAX_VALUE);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileProxy.TEXT_FILE});
  }

  public static void parseFirstLines(char[] chars, int[] lines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseFirstLines(chars, lines, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFirstLines(char[] chars, int[] lines, List<Object> result) {
    String source = new String(chars);
    LineParser parser = new LineParser();
    int numOfLines = lines[0];
    int[] ints = parser.parseIntArray(source, numOfLines);
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
        if (prevLine) {
          lines.add(-1);
          lines.add(-1);
          N++;
        }
        prevLine = true;
        continue;
      }
      lines.add(wordStart);
      wordStart += line.length();
      lines.add(wordStart);
      prevLine = false;
      M++;
      N++;
    }

    ArrayWriter writer = new ArrayWriter(3 + N + 4 * M);
    writer.write(N, 0, 0);
    for (int lineInd = 0; 2 * lineInd < lines.size(); lineInd++) {
      int start = lines.get(2 * lineInd);
      int stop = lines.get(2 * lineInd + 1);

      if (start == stop && start == -1) {
        writer.write(0);
      } else {
        writer.write(1);
        writer.write(start, stop, 0, 0);
      }
    }
    Debug.consoleInfo("Parsing complete");
    return writer.getInts();
  }

  private static boolean isNewLine(String str) {
    return str.length() == 1 && str.charAt(0) == '\n';
  }

  private static boolean isCarriage(String str) {
    return str.length() == 1 && str.charAt(0) == '\r';
  }

}
