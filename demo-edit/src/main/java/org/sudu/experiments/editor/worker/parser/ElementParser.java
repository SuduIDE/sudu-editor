package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.parser.common.base.FirstLinesIntLexer;
import org.sudu.experiments.parser.common.base.IntParser;

import java.util.*;

public class ElementParser implements IntParser, FirstLinesIntLexer {

  public static ElementParser instance = new ElementParser();

  private ElementParser() {}

  @Override
  public int[] parse(char[] source) {
    return parseIntArray(source, Integer.MAX_VALUE);
  }

  @Override
  public int[] parse(char[] source, int numOfLines) {
    return parseIntArray(source, numOfLines);
  }

  private int[] parseIntArray(char[] source, int numOfLines) {
    List<List<Integer>> lines = new ArrayList<>();
    int wordStart = 0;

    lines.add(new ArrayList<>());
    for (int i = 0; i < source.length; i++) {
      var c = source[i];
      if (c == ' ' || c == '\t') {
        if (wordStart != i) addToLast(lines, wordStart, i, 0, 0);
        wordStart = i;
        while (i + 1 < source.length
            && ((c = source[i + 1]) == ' ' || c == '\t')
        ) i++;
        addToLast(lines, wordStart, i + 1, 0, 0);
        wordStart = i + 1;
      }
      else if (DELIMS.get(c)) {
        if (wordStart != i) addToLast(lines, wordStart, i, 0, 0);
        addToLast(lines, i, i + 1, 0, 0);
        wordStart = i + 1;
      } else if (c == '\n') {
        if (wordStart != i) addToLast(lines, wordStart, i, 0, 0);
        wordStart = i + 1;
        if (lines.size() >= numOfLines) break;
        lines.add(new ArrayList<>());
      } else if (c == '\r') {
        if (wordStart != i) addToLast(lines, wordStart, i, 0, 0);
        wordStart = i + 1;
        if (i + 1 < source.length && source[i + 1] == '\n') {
          wordStart++;
          i++;
        }
        if (lines.size() >= numOfLines) break;
        lines.add(new ArrayList<>());
      }
    }
    if (wordStart != source.length) addToLast(lines, wordStart, source.length, 0, 0);

    ArrayWriter writer = new ArrayWriter();
    writer.write(lines.size(), 0, 0);
    for (var line : lines) {
      writer.write(line.size() / 4);
      for (var elem: line) writer.write(elem);
    }
//    Debug.consoleInfo("Elements parsing complete");
    return writer.getInts();
  }

  private static void addToLast(List<List<Integer>> list, Integer... elems) {
    var last = list.get(list.size() - 1);
    last.addAll(Arrays.asList(elems));
  }

  private static final BitSet DELIMS = initDelimBitSet();

  private static BitSet initDelimBitSet() {
    BitSet result = new BitSet();
    result.set(',');
    result.set('.');
    result.set(';');
    result.set(':');
    result.set('+');
    result.set('-');
    result.set('*');
    result.set('/');
    result.set('%');
    result.set('^');
    result.set('(');
    result.set(')');
    result.set('[');
    result.set(']');
    result.set('<');
    result.set('>');
    result.set('\\');
    result.set('\'');
    result.set('\"');
    return result;
  }
}
