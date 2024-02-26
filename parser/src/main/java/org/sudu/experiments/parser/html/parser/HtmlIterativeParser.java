package org.sudu.experiments.parser.html.parser;

import org.sudu.experiments.parser.common.base.FullIntervalParser;
import org.sudu.experiments.parser.html.gen.HTMLParser;

public class HtmlIterativeParser extends FullIntervalParser<HTMLParser, HtmlFullParser> {
  public HtmlIterativeParser() {
    super(new HtmlFullParser());
  }
}
