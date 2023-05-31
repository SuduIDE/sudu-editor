package org.sudu.experiments.demo.worker;

import org.sudu.experiments.demo.CodeElement;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.parser.JavaFirstLinesLexer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class JavaLexerFirstLines {

  public static final String LEXER_FIRST_LINES = "asyncJavaLexerFirstLines.parseFirstLines";

  public static void parseFirstLines(char[] chars, int[] lines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parseFirstLines(chars, lines, list);
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFirstLines(char[] chars, int[] lines, List<Object> result) {
    String source = new String(chars);
    JavaFirstLinesLexer parser = new JavaFirstLinesLexer();
    int numOfLines = lines[0];
    int[] ints = parser.parse(source, numOfLines);
    result.add(ints);
    result.add(chars);
  }

  public static Document makeDocument(Document document, int[] ints, char[] chars, boolean isFirstLinesParsed) {
    int N = ints[0];
    int M = ints[1];
    int K = ints[2];
    CodeLine[] newDoc = new CodeLine[N];

    List<Interval> intervalList = BaseParser.getIntervalList(ints, 3 + N + 4 * M, K);

    for (int i = 0, wordInd = 0; i < N; i++) {
      int len = ints[3 + i];
      if (isFirstLinesParsed && i < document.length()) {
        newDoc[i] = document.line(i);
        wordInd += len;
        continue;
      }
      CodeElement[] elements = new CodeElement[len];
      for (int j = 0; j < len; j++, wordInd++) {
        int wordStart = ints[3 + N + 4 * wordInd];
        int wordStop = ints[3 + N + 4 * wordInd + 1];
        int wordType = ints[3 + N + 4 * wordInd + 2];
        int wordStyle = ints[3 + N + 4 * wordInd + 3];
        String word = new String(chars, wordStart, wordStop - wordStart);
        elements[j] = new CodeElement(word, wordType, wordStyle);
      }
      newDoc[i] = new CodeLine(elements);
    }
    Document updDocument = new Document(newDoc);
    updDocument.tree = new IntervalTree(intervalList);
    return updDocument;
  }
}
