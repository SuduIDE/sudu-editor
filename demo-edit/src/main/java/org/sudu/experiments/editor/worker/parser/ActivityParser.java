package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.activity.ActivityFullParser;
import org.sudu.experiments.parser.activity.graph.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ActivityParser {

  public static final String PARSE = "ActivityParser.parse";
  public static final String COMPUTE = "ActivityParser.compute";

  private static ActivityFullParser parser;
  private static Path[][] paths;

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
    if (args.length >= 1) {
      if (args[0].equals("calculate")) {
        paths = parser.dag2.input.calculateTestPaths();

      } else if (args[0].equals("get")) {
        if (args.length < 2) {
          System.out.println("Not enough args for 'get' command");
          return;
        }
        if (paths == null) {
          System.out.println("paths not calculated");
          return;
        }
        int idx = Integer.parseInt(args[1].toString());
        result.addAll(Arrays.stream(paths[idx]).map(Path::toString).toList());

      } else if (args[0].equals("highlight")) {
        if (args.length < 3) {
          System.out.println("Not enough args for 'get' command");
          return;
        }
        if (paths == null) {
          System.out.println("paths not calculated");
          return;
        }
        int i = Integer.parseInt(args[1].toString());
        int j = Integer.parseInt(args[2].toString());

        var mermaid = parser.dag2.input.printRecDag2(paths[i][j]);
        result.add(mermaid);
      }
    }
  }
}
