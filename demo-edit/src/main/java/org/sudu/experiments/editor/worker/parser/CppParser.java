package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.cpp.parser.CppFirstLinesLexer;
import org.sudu.experiments.parser.cpp.parser.CppFullParser;
import org.sudu.experiments.parser.cpp.parser.CppIntervalParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CppParser {

  public static final String PARSE = "CppParser.parse";

  public static void parse(char[] chars, List<Object> result) {
    int[] ints = new CppFullParser().parse(chars);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.CPP_FILE});
  }

  public static final String LEXER_FIRST_LINES = "asyncCppLexerFirstLines.parseFirstLines";

  public static void parseFirstLines(char[] chars, int[] lines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseFirstLines(chars, lines, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFirstLines(char[] chars, int[] lines, List<Object> result) {
    CppFirstLinesLexer parser = new CppFirstLinesLexer();
    int numOfLines = lines[0];
    int[] ints = parser.parse(chars, numOfLines);
    result.add(ints);
    result.add(chars);
  }

  public static void parseInterval(char[] chars, int[] interval, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseInterval(chars, interval, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseInterval(char[] chars, int[] interval, List<Object> result) {
    CppIntervalParser parser = new CppIntervalParser();
    int[] ints = parser.parseInterval(chars, interval);
    result.add(ints);
    result.add(chars);
  }

}
