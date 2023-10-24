package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.activity.ActivityFullParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ActivityParser {

  public static final String PARSE = "ActivityParser.parse";

  public static void parse(char[] chars, List<Object> result) {
    int[] ints = new ActivityFullParser().parse(chars);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.ACTIVITY_FILE});
  }

  public static void parseInterval(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parse(chars, list);
    ArrayOp.sendArrayList(list, result);
  }

}
