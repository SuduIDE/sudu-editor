package org.sudu.experiments.demo.worker.parser;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.cpp.parser.CppFirstLinesLexer;
import org.sudu.experiments.parser.cpp.parser.CppFullParser;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CppParser {

  public static final String PARSE_BYTES_CPP = "CppParser.parseBytes";

  public static void parseChars(char[] chars, List<Object> result) {
    String source = new String(chars);

    int[] ints = new CppFullParser().parse(source);
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
    String source = new String(chars);
    CppFirstLinesLexer parser = new CppFirstLinesLexer();
    int numOfLines = lines[0];
    int[] ints = parser.parse(source, numOfLines);
    result.add(ints);
    result.add(chars);
  }

}
