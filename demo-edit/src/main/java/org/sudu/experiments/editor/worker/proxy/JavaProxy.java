package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.java.parser.*;

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

  public void parseViewport(char[] source, int[] viewport, int[] intervals, List<Object> result) {
    int[] ints = new JavaViewportIntervalsParser().parseViewport(source, viewport, intervals);
    result.add(ints);
    result.add(source);
  }

  public static final String PARSE_STRUCTURE = "JavaProxy.parseStructure";
  public void parseStructure(char[] chars, List<Object> result) {
    int[] ints = new JavaFullStructureParser().parse(chars);
    result.add(ints);
    result.add(chars);
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
