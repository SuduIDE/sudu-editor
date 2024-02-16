package org.sudu.experiments.parser.activity.parser;

import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.common.base.FullIntervalParser;

public class ActivityIterativeParser extends FullIntervalParser<ActivityParser> {

  public ActivityIterativeParser() {
    super(new ActivityFullParser());
  }

  @Override
  public int[] parseInterval(char[] source, int[] interval, int[] graphInts, char[] graphChars) {
    return ((ActivityFullParser) fullParser).parseActivity(source);
  }

}
