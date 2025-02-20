package org.sudu.experiments.parser.json.parser;

import org.sudu.experiments.parser.common.base.FullIntervalParser;
import org.sudu.experiments.parser.json.gen.JsonParser;

public class JsonIterativeParser extends FullIntervalParser<JsonParser, JsonFullParser> {
  public JsonIterativeParser() {
    super(new JsonFullParser());
  }
}
