package org.sudu.experiments.demo.worker;

import org.sudu.experiments.parser.java.parser.JavaFullParser;
import org.sudu.experiments.parser.java.parser.JavaViewportIntervalsParser;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JavaParser {

  public static final String PARSE_BYTES_JAVA = "JavaParser.parseBytes";

  public static void parseBytes(byte[] bytes, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");

    int[] ints = new JavaFullParser().parse(source);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
    result.add(new int[]{FileParser.JAVA_FILE});
  }

  public static final String PARSE_BYTES_JAVA_VIEWPORT = "JavaParser.parseViewport";

  public static void parseViewport(byte[] bytes, int[] viewport, int[] intervals, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");

    int[] ints = new JavaViewportIntervalsParser().parseViewport(source, viewport, intervals);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

}
