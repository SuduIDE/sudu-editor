package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.Debug;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.editor.worker.proxy.FileProxy;
import org.sudu.experiments.math.ArrayOp;

import java.util.*;
import java.util.function.Consumer;

public class ElementParser {

  public static final String PARSE = "ElementParser.parse";

  public static void parse(char[] chars, List<Object> result) {
    String source = new String(chars);
    ElementParser parser = new ElementParser();
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
    ElementParser parser = new ElementParser();
    int numOfLines = lines[0];
    int[] ints = parser.parseIntArray(source, numOfLines);
    result.add(ints);
    result.add(chars);
  }

  public static void addToLast(List<List<Integer>> list, Integer... elems) {
    var last = list.get(list.size() - 1);
    last.addAll(Arrays.asList(elems));
  }

  int[] parseIntArray(String source, int numOfLines) {
    List<List<Integer>> lines = new ArrayList<>();
    int wordStart = 0;

    lines.add(new ArrayList<>());
    for (int i = 0; i < source.length(); i++) {
      var c = source.charAt(i);
      if (c == ' ' || c == '\t') {
        if (wordStart != i) addToLast(lines, wordStart, i, 0, 0);
        wordStart = i;
        while (i + 1 < source.length()
            && ((c = source.charAt(i + 1)) == ' ' || c == '\t')
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
        if (i + 1 < source.length() && source.charAt(i + 1) == '\n') {
          wordStart++;
          i++;
        }
        if (lines.size() >= numOfLines) break;
        lines.add(new ArrayList<>());
      }
    }

    ArrayWriter writer = new ArrayWriter();
    writer.write(lines.size(), 0, 0);
    for (var line : lines) {
      writer.write(line.size() / 4);
      for (var elem: line) writer.write(elem);
    }
    Debug.consoleInfo("Elements parsing complete");
    return writer.getInts();
  }

  public static final BitSet DELIMS = initDelimBitSet();

  public static BitSet initDelimBitSet() {
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
