package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.parser.activity.ActivityFullParser;
import org.sudu.experiments.parser.activity.graph.Path;
import org.sudu.experiments.parser.activity.graph.stat.Random;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.base.BaseFullScopeParser;
import org.sudu.experiments.parser.common.base.BaseIntervalParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityProxy extends BaseProxy {

  private ActivityFullParser parser;
  private Path[][] paths;

  public ActivityProxy() {
    super(FileProxy.ACTIVITY_FILE, Languages.ACTIVITY);
  }

  @Override
  public void parseFullFile(char[] source, List<Object> result) {
    var p = new ActivityFullParser();

    List<Object> res = p.parseActivity(source);
    result.add(res.get(0));
    result.add(source);
    result.add(new int[]{FileProxy.ACTIVITY_FILE});
    result.add(res.get(1).toString().toCharArray());
    result.add(res.get(2).toString().toCharArray());

    parser = p;
  }

  public static final String COMPUTE = "ActivityParser.compute";

  public void compute(Object[] args, ArrayList<Object> result) {
    if (parser == null) {
      result.add("Parser not ready");
      return;
    }
    if (args.length >= 1) {
      if (args[0].equals("calculate")) {
        paths = parser.activity.dag2(false).calculateTestPaths();

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
          System.out.println("Not enough args for 'highlight' command");
          return;
        }
        int i = Integer.parseInt(args[1].toString());
        int j = Integer.parseInt(args[2].toString());


        if (paths == null) {
          System.out.println("ERROR: paths not calculated");
          return;
        }
        var p = paths[i][j];

        var mermaid = parser.activity.dag2(false).printRecDag2(p);
        result.add(mermaid);

      } else if (args[0].equals("dag2")) {
        var mermaid = parser.activity.dag2(false).printRecDag2(null);
        result.add(mermaid);

      } else if (args[0].equals("seed")) {
        int seed = Integer.parseInt(args[1].toString());
        Random.setGlobalSeedAndInitiateRandom(seed);
        System.out.println("Seed=" + seed);
        parser.activity.dag2(true);

      } else { //DEFAULT
        System.out.println("Unknown action: " + args[0]);
      }
    }
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
