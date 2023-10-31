package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.javascript.parser.JavaScriptFirstLinesLexer;
import org.sudu.experiments.parser.javascript.parser.JavaScriptFullParser;
import org.sudu.experiments.parser.javascript.parser.JavaScriptIntervalParser;

public class JavaScriptProxy extends BaseProxy {


  public JavaScriptProxy() {
    super(FileProxy.JS_FILE, Languages.JS);
  }

  @Override
  public BaseFirstLinesLexer<?> getFirstLinesLexer() {
    return new JavaScriptFirstLinesLexer();
  }

  public static final String PARSE_FULL_FILE = "JavaScriptProxy.parseFullFile";

  @Override
  public BaseFullParser<?> getFullParser() {
    return new JavaScriptFullParser();
  }

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new JavaScriptIntervalParser();
  }

}
