package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.common.base.FirstLinesIntLexer;
import org.sudu.experiments.parser.common.base.IntParser;
import org.sudu.experiments.parser.json.parser.JsonFirstLinesLexer;
import org.sudu.experiments.parser.json.parser.JsonFullParser;
import org.sudu.experiments.parser.json.parser.JsonIterativeParser;

public class JsonProxy extends BaseProxy {

  public JsonProxy() {
    super(FileProxy.JSON_FILE, Languages.JSON);
  }

  @Override
  public FirstLinesIntLexer getFirstLinesLexer() {
    return new JsonFirstLinesLexer();
  }

  public static final String PARSE_FULL_FILE = "JsonProxy.parseFullFile";

  @Override
  public IntParser getFullParser() {
    return new JsonFullParser();
  }

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new JsonIterativeParser();
  }
}
