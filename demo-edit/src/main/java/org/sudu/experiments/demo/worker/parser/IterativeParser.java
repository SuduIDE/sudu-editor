package org.sudu.experiments.demo.worker.parser;

import org.sudu.experiments.parser.java.parser.JavaIntervalParser;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class IterativeParser {

  public static final String PARSE_BYTES_JAVA = "IterativeParser.parseBytes";

  public static void parseBytes(byte[] bytes, int[] interval, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");

    int[] ints = new JavaIntervalParser().parseInterval(source, interval);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

}
