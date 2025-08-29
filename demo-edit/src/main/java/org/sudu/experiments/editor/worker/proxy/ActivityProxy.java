package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.activity.parser.ActivityFullParser;
import org.sudu.experiments.parser.activity.parser.ActivityIterativeParser;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;

import java.util.List;

public class ActivityProxy extends BaseProxy {

  public ActivityProxy() {
    super(FileProxy.ACTIVITY_FILE, Languages.ACTIVITY);
  }

  @Override
  public void parseFullFile(char[] source, int version, List<Object> result) {
    var p = new ActivityFullParser();

    int[] res = p.parseActivity(source);
    result.add(res);
    result.add(source);
    result.add(new int[]{languageType, version});
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
    return new ActivityIterativeParser();
  }
}
