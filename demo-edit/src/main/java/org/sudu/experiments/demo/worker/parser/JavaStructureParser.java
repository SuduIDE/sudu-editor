package org.sudu.experiments.demo.worker.parser;

import org.sudu.experiments.parser.java.parser.JavaFullStructureParser;

import java.util.Arrays;
import java.util.List;

public class JavaStructureParser {

  public static final String PARSE_STRUCTURE_JAVA = "JavaStructureParser.parseBytes";

  public static void parseChars(char[] chars, List<Object> result) {
    String source = new String(chars);

    int[] ints = new JavaFullStructureParser().parse(source);
    result.add(ints);
    result.add(chars);
  }

}
