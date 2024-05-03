package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.editor.worker.parser.ElementParser;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.common.base.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TextProxy extends BaseProxy {

  public static final String PARSE_FULL_FILE = "TextProxy.parseFullFile";

  public TextProxy() {
    super(FileProxy.TEXT_FILE, Languages.TEXT);
  }

  public void parseInterval(char[] source, int[] interval, int[] version, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    int[] ints = getFullParser().parse(source);
    list.add(ints);
    list.add(source);
    list.add(version);
    ArrayOp.sendArrayList(list, result);
  }

  @Override
  public FirstLinesIntLexer getFirstLinesLexer() {
    return ElementParser.instance;
  }

  @Override
  public IntParser getFullParser() {
    return ElementParser.instance;
  }

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    throw new UnsupportedOperationException();
  }
}
