package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.parser.java.parser.JavaFullStructureParser;

import java.util.List;

public class JavaStructureParser {

  public static final String PARSE_STRUCTURE_JAVA = "JavaStructureParser.parseBytes";

  public static void parseChars(char[] chars, List<Object> result) {
    int[] ints = new JavaFullStructureParser().parse(chars);
    result.add(ints);
    result.add(chars);
  }
}
