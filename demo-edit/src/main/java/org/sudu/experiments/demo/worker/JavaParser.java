package org.sudu.experiments.demo.worker;

import org.sudu.experiments.parser.java.parser.JavaFullParser;
import org.sudu.experiments.parser.java.parser.JavaViewportIntervalsParser;

import java.util.List;

public class JavaParser {

  public static final String PARSE_BYTES_JAVA = "JavaParser.parseBytes";

  public static void parseChars(char[] chars, List<Object> result) {
    String source = new String(chars);

    int[] ints = new JavaFullParser().parse(source);
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.JAVA_FILE});
  }

  public static final String PARSE_BYTES_JAVA_VIEWPORT = "JavaParser.parseViewport";

  public static void parseViewport(char[] chars, int[] viewport, int[] intervals, List<Object> result) {
    String source = new String(chars);

    int[] ints = new JavaViewportIntervalsParser().parseViewport(source, viewport, intervals);
    result.add(ints);
    result.add(chars);
  }

}
