package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.java.parser.*;

import java.util.ArrayList;
import java.util.List;

public class JavaProxy extends BaseProxy {

  public JavaProxy() {
    super(FileProxy.JAVA_FILE, Languages.JAVA);
  }

  @Override
  public BaseFirstLinesLexer<?> getFirstLinesLexer() {
    return new JavaFirstLinesLexer();
  }

  public static final String PARSE_VIEWPORT = "JavaProxy.parseViewport";

  public void parseViewport(Object[] a, ArrayList<Object> result) {
    char[] source = ArgsCast.array(a, 0).chars();
    int[] viewport = ArgsCast.array(a, 1).ints();
    int[] intervals = ArgsCast.array(a, 2).ints();
    int version = ArgsCast.array(a, 3).ints()[1];
    parseViewport(source, viewport, intervals, version, result);
  }

  public void parseViewport(
      char[] source,
      int[] viewport,
      int[] intervals,
      int version,
      List<Object> result
  ) {
    int[] ints = new JavaViewportIntervalsParser().parseViewport(source, viewport, intervals);
    result.add(ints);
    result.add(source);
    result.add(new int[]{languageType, version});
  }

  public static final String PARSE_STRUCTURE = "JavaProxy.parseStructure";

  public void parseStructure(Object[] a, ArrayList<Object> result) {
    char[] source = ArgsCast.array(a, 0).chars();
    int version = ArgsCast.array(a, 1).ints()[1];
    parseStructure(source, version, result);
  }

  public void parseStructure(char[] chars, int version, List<Object> result) {
    int[] ints = new JavaFullStructureParser().parse(chars);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{languageType, version});
  }

  public static final String PARSE_FULL_FILE = "JavaProxy.parseFullFile";

  @Override
  public BaseFullParser<?> getFullParser() {
    return new JavaFullParser();
  }

  public static final String PARSE_FULL_FILE_SCOPES = "JavaProxy.parseFullFileScopes";

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    return new JavaFullScopesParser();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new JavaIntervalParser();
  }

}
