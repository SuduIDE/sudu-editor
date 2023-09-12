package org.sudu.experiments.demo;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.demo.worker.diff.DiffInfo;
import org.sudu.experiments.demo.worker.diff.DiffUtils;
import org.sudu.experiments.demo.worker.parser.JavaParser;
import org.sudu.experiments.demo.worker.parser.ParserUtils;
import org.sudu.experiments.diff.DiffModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DiffModelTest {

  public static void main(String[] args) {
    new DiffModelTest().compareDocuments(true);
  }

  @Test
  public void compareDocuments() {
    compareDocuments(false);
  }

  public void compareDocuments(boolean printResults) {
    var docN = parse(readFile("ClassN.java"));
    var docM = parse(readFile("ClassM.java"));

    DiffModel model = new DiffModel();
    char[] charsN = docN.getChars();
    int[] intsN = DiffUtils.makeIntervals(docN);
    char[] charsM = docM.getChars();
    int[] intsM = DiffUtils.makeIntervals(docM);

    int[] res = model.findDiffs(charsN, intsN, charsM, intsM);

    DiffInfo info = DiffUtils.readDiffInfo(res);
    if (printResults) DiffUtils.printInfo(info, docN, docM);
  }

  private Document parse(String text) {
    List<Object> result = new ArrayList<>();
    char[] chars = text.toCharArray();
    int[] ints;
    JavaParser.parse(chars, result);
    ints = (int[]) result.get(0);
    return ParserUtils.makeDocument(ints, chars);
  }

  private String readFile(String filename) {
    try {
      var url = getClass().getClassLoader().getResource(filename);
      if (url == null) throw new IllegalArgumentException("Illegal resource name: " + filename);
      return Files.readString(Path.of(url.toURI()));
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

}
