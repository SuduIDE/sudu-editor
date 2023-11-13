package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseProxy {

  public final int languageType;
  public final String languageName;

  public BaseProxy(int languageType, String languageName) {
    this.languageType = languageType;
    this.languageName = languageName;
  }

  public void parseFirstLines(char[] source, int[] lines, List<Object> result) {
    int numOfLines = lines[0];
    int[] ints = getFirstLinesLexer().parse(source, numOfLines);
    result.add(ints);
    result.add(source);
  }

  public void parseFullFile(char[] source, List<Object> result) {
    int[] ints = getFullParser().parse(source);
    result.add(ints);
    result.add(source);
    result.add(new int[]{languageType});
  }

  public void parseFullFileScopes(char[] source, List<Object> result) {
    var parser = getFullScopeParser();
    int[] ints = parser.parse(source);
    result.add(ints);
    result.add(source);
    result.add(new int[]{languageType});
    result.add(parser.getGraphInts());
    result.add(parser.getGraphChars());
  }

  public void parseInterval(char[] source, int[] interval, int[] version, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    int[] ints = getIntervalParser().parseInterval(source, interval);
    list.add(ints);
    list.add(source);
    list.add(version);
    ArrayOp.sendArrayList(list, result);
  }

  public void parseIntervalScope(
      char[] source, int[] interval,
      int[] version,
      int[] graphInts, char[] graphChars,
      Consumer<Object[]> result
  ) {
    ArrayList<Object> list = new ArrayList<>();
    var parser = getIntervalParser();
    int[] ints = parser.parseInterval(source, interval, graphInts, graphChars);
    list.add(ints);
    list.add(source);
    list.add(version);
    list.add(parser.getGraphInts());
    list.add(parser.getGraphChars());
    ArrayOp.sendArrayList(list, result);
  }

  public abstract BaseFirstLinesLexer<?> getFirstLinesLexer();

  public abstract BaseFullParser<?> getFullParser();
  public abstract BaseFullScopeParser<?> getFullScopeParser();
  public abstract BaseIntervalParser<?> getIntervalParser();

}
