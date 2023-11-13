package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.activity.ActivityFullParser;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;

public class ActivityProxy extends BaseProxy {

  public ActivityProxy() {
    super(FileProxy.ACTIVITY_FILE, Languages.ACTIVITY);
  }

  @Override
  public BaseFirstLinesLexer<?> getFirstLinesLexer() {
    throw new UnsupportedOperationException();
  }

  public static final String PARSE_FULL_FILE = "ActivityParser.parseFullFile";
  @Override
  public BaseFullParser<?> getFullParser() {
    return new ActivityFullParser();
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
