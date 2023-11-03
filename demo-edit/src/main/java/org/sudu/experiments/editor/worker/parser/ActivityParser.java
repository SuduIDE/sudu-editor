package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.activity.ActivityFullParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ActivityParser {

  public static final String PARSE = "ActivityParser.parse";
  public static final String COMPUTE = "ActivityParser.compute";
  private static ActivityFullParser parser;

  public static void parse(char[] chars, List<Object> result) {
    var p = new ActivityFullParser();

    List<Object> res = p.parse(chars);
    result.add(res.get(0));
    result.add(chars);
    result.add(new int[]{FileParser.ACTIVITY_FILE});
    result.add(res.get(1).toString().toCharArray());
    result.add(res.get(2).toString().toCharArray());

    parser = p;
  }


  public static void parseInterval(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    parse(chars, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static void compute(Object[] args, ArrayList<Object> result) {
    if (parser == null) {
      result.add("Parser not ready");
      return;
    }
    result.add(parser.mermaid2);
  }
}
