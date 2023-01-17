package org.sudu.experiments.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LineParser {

  StringTokenizer lineTokenizer;
  StringTokenizer wordTokenizer;

  private static final String LINE_DELIM = "\n";
  private static final String WORD_DELIM = " \n\r\t()[]<>,.:;-_\"'`/";

  Document parse(String source) {
    List<CodeLine> lines = new ArrayList<>();
    List<CodeElement> words = new ArrayList<>();

    lineTokenizer = new StringTokenizer(source, LINE_DELIM);
    while (lineTokenizer.hasMoreTokens()) {
      String token = lineTokenizer.nextToken();
      String line = trim(token);
      wordTokenizer = new StringTokenizer(line, WORD_DELIM, true);
      words.clear();
      while (wordTokenizer.hasMoreTokens()) {
        String word = wordTokenizer.nextToken();
        words.add(plainText(word));
      }
      lines.add(new CodeLine(words.toArray(CodeElement[]::new)));
    }
    return new Document(lines.toArray(CodeLine[]::new));
  }

  private CodeElement plainText(String text) {
    return new CodeElement(text);
  }

  private String trim(String str) {
    int from = 0;
    int to = str.length();
    if (str.charAt(str.length() - 1) == '\r') to--;
    return str.substring(from, to);
  }

}