package org.sudu.experiments.demo.worker.parser;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.javascript.parser.JavaScriptFirstLinesLexer;
import org.sudu.experiments.parser.javascript.parser.JavaScriptFullParser;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JavaScriptParser {

  public static final String PARSE_BYTES_JAVASCRIPT = "JavaScriptParser.parseBytes";

  public static void parseChars(char[] chars, List<Object> result) {
    String source = new String(chars);

    int[] ints = new JavaScriptFullParser().parse(source);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.JS_FILE});
  }

  public static final String LEXER_FIRST_LINES = "asyncJavaScriptLexerFirstLines.parseFirstLines";

  public static void parseFirstLines(char[] chars, int[] lines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseFirstLines(chars, lines, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFirstLines(char[] chars, int[] lines, List<Object> result) {
    String source = new String(chars);
    JavaScriptFirstLinesLexer parser = new JavaScriptFirstLinesLexer();
    int numOfLines = lines[0];
    int[] ints = parser.parse(source, numOfLines);
    result.add(ints);
    result.add(chars);
  }

}
