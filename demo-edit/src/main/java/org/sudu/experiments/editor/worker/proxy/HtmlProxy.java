package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;
import org.sudu.experiments.parser.html.parser.HtmlFirstLineLexer;
import org.sudu.experiments.parser.html.parser.HtmlFullParser;
import org.sudu.experiments.parser.html.parser.HtmlIterativeParser;

public class HtmlProxy extends BaseProxy {

  public HtmlProxy() {
    super(FileProxy.HTML_FILE, Languages.HTML);
  }

  @Override
  public BaseFirstLinesLexer<?> getFirstLinesLexer() {
    return new HtmlFirstLineLexer();
  }

  public static final String PARSE_FULL_FILE = "HtmlProxy.parseFullFile";

  @Override
  public BaseFullParser<?> getFullParser() {
    return new HtmlFullParser();
  }

  @Override
  public BaseFullScopeParser<?> getFullScopeParser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public BaseIntervalParser<?> getIntervalParser() {
    return new HtmlIterativeParser();
  }
}
