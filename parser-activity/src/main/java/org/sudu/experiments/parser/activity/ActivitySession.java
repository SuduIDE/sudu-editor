package org.sudu.experiments.parser.activity;

import org.sudu.experiments.parser.activity.graph.Path;
import org.sudu.experiments.parser.activity.graph.stat.RandomSrc;

public class ActivitySession {
  String source;
  ActivityFullParser parser;
  int seed = RandomSrc.defaultSeed;
  private Path[][] paths;

  public void setSource(String src) {
    source = src;
    parser = new ActivityFullParser();
    parser.parseActivityServer(source);
    parser.activity.setSeed(seed);
  }

  public String calculatePaths() {
    buildPaths();
    return array2ToJson(new StringBuilder(), paths).toString();
  }

  private void buildPaths() {
    paths = parser.activity.dag2().calculateTestPaths();
  }

  public String dag1() {
    return parser.activity.toDag1();
  }

  public String dag2() {
    return parser.activity.dag2().printRecDag2(null);
  }

  public String source() {
    return source;
  }

  public void setSeed(int seed) {
    this.seed = seed;
    if (parser != null) {
      parser.activity.setSeed(seed);
    }
  }

  public String highlight(int groupIndex, int index) {
    if (paths == null) buildPaths();
    var p = paths[groupIndex][index];
    return parser.activity.dag2().printRecDag2(p);
  }

  static StringBuilder array2ToJson(StringBuilder sb, Path[][] paths) {
    sb.append('[');
    for (int i = 0; i < paths.length; i++) {
      if (i > 0) sb.append(',');
      sb.append('[');
      Path[] path = paths[i];
      for (int j = 0; j < path.length; j++) {
        if (j > 0) sb.append(',');
        sb.append('"');
        sb.append(path[j].toString());
        sb.append('"');
      }
      sb.append(']');
    }
    sb.append(']');
    return sb;
  }
}
