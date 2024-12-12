package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.ReadResource;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.proxy.JavaProxy;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.text.SplitText;

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
    var docL = parse(ReadResource.readFile("classR.java", getClass()));
    var docR = parse(ReadResource.readFile("classL.java", getClass()));

    DiffModel model = new DiffModel();
    char[] charsL = docL.getChars();
    int[] intsL = DiffUtils.makeIntervals(docL, true);
    char[] charsR = docR.getChars();
    int[] intsR = DiffUtils.makeIntervals(docR, true);

    int[] res = model.findDiffs(charsL, intsL, charsR, intsR);

    DiffInfo info = DiffUtils.readDiffInfo(res);
    if (printResults) DiffUtils.printInfo(info, docL, docR);
  }

  private Document parse(String text) {
    List<Object> result = new ArrayList<>();
    char[] chars = text.toCharArray();
    int[] ints;
    new JavaProxy().parseFullFile(chars, result);
    ints = (int[]) result.get(0);
    Document document = new Document(SplitText.split(text));
    ParserUtils.updateDocument(document, ints, chars);
    return document;
  }

}
