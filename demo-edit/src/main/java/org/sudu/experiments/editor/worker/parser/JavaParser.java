package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.java.parser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JavaParser {

  public static final String PARSE_SCOPES = "JavaParser.parseScopes";

  public static void parseScopes(char[] chars, List<Object> result) {
    var parser = new JavaFullScopesParser();
    int[] ints = parser.parse(chars);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.JAVA_FILE});
    result.add(parser.getGraphInts());
    result.add(parser.getGraphChars());
  }

  public static final String PARSE = "JavaParser.parse";

  public static void parse(char[] chars, List<Object> result) {
    int[] ints = new JavaFullParser().parse(chars);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.JAVA_FILE});
  }

  public static final String PARSE_BYTES_JAVA_VIEWPORT = "JavaParser.parseViewport";

  public static void parseViewport(char[] chars, int[] viewport, int[] intervals, List<Object> result) {
    String source = new String(chars);

    int[] ints = new JavaViewportIntervalsParser().parseViewport(source, viewport, intervals);
    result.add(ints);
    result.add(chars);
  }

  public static final String LEXER_FIRST_LINES = "asyncJavaLexerFirstLines.parseFirstLines";

  public static void parseFirstLines(char[] chars, int[] lines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseFirstLines(chars, lines, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFirstLines(char[] chars, int[] lines, List<Object> result) {
    JavaFirstLinesLexer parser = new JavaFirstLinesLexer();
    int numOfLines = lines[0];
    int[] ints = parser.parse(chars, numOfLines);
    result.add(ints);
    result.add(chars);
  }

  public static void parseInterval(char[] chars, int[] interval, int[] graphInts, char[] graphChars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseInterval(chars, interval, graphInts, graphChars, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseInterval(
      char[] chars, int[] interval,
      int[] graphInts, char[] graphChars,
      List<Object> result
  ) {
    String source = new String(chars);
    JavaIntervalParser parser = new JavaIntervalParser();
    int[] ints = parser.parseInterval(source, interval, graphInts, graphChars);
    result.add(ints);
    result.add(chars);
    result.add(parser.getGraphInts());
    result.add(parser.getGraphChars());
  }

}
