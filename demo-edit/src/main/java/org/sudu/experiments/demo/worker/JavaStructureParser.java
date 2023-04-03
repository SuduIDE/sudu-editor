package org.sudu.experiments.demo.worker;

import org.sudu.experiments.parser.java.parser.JavaFullStructureParser;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JavaStructureParser {

  public static final String PARSE_STRUCTURE_JAVA = "JavaStructureParser.parseBytes";

  public static void parseBytes(byte[] bytes, List<Object> result) {
    String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");

    int[] ints = new JavaFullStructureParser().parse(source);
    char[] chars = source.toCharArray();
    result.add(ints);
    result.add(chars);
  }

}
