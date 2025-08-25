package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.cpp.parser.CppFirstLinesLexer;
import org.sudu.experiments.parser.cpp.parser.CppFullParser;
import org.sudu.experiments.parser.cpp.parser.CppFullScopesParser;
import org.sudu.experiments.parser.cpp.parser.CppIntervalParser;

public class CppProxy extends BaseProxy {

  public CppProxy() {
    super(FileProxy.CPP_FILE, Languages.CPP);
  }

  @Override
  public BaseFirstLinesLexer<?> getFirstLinesLexer() {
    return new CppFirstLinesLexer();
  }

  public static final String PARSE_FULL_FILE = "CppProxy.parseFullFile";
  @Override
  public BaseFullParser<?> getFullParser() {
    return new CppFullParser();
  }

  public static final String PARSE_FULL_FILE_SCOPES = "CppProxy.parseFullFileScopes";

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    return new CppFullScopesParser();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new CppIntervalParser();
  }
}
