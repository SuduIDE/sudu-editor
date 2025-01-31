package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.base.FirstLinesIntLexer;
import org.sudu.experiments.parser.common.base.IntParser;
import org.sudu.experiments.parser.typescript.parser.TypeScriptFirstLinesLexer;
import org.sudu.experiments.parser.typescript.parser.TypeScriptLightParser;
import org.sudu.experiments.parser.typescript.parser.highlighting.TypeScriptIntervalParser;

public class TypeScriptProxy extends BaseProxy {

  public TypeScriptProxy() {
    super(FileProxy.TS_FILE, Languages.TS);
  }

  @Override
  public FirstLinesIntLexer getFirstLinesLexer() {
    return new TypeScriptFirstLinesLexer();
  }

  public static final String PARSE_FULL_FILE = "TypeScriptProxy.parseFullFile";

  @Override
  public IntParser getFullParser() {
    return new TypeScriptLightParser();
  }

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    return null;
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new TypeScriptIntervalParser();
  }
}
