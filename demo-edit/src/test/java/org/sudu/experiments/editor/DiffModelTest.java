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
    new DiffModelTest().compareDocumentsWithSyncPoints(true);
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

  public void compareDocumentsWithSyncPoints(boolean printResults) {
    var docL = parse(ReadResource.readFile("ClassL1.java", getClass()));
    var docR = parse(ReadResource.readFile("ClassR1.java", getClass()));

    DiffModel model = new DiffModel();
    model.syncL = new int[]{0, 8, 12, 16, 16};
    model.syncR = new int[]{6, 8, 14, 16, 18};
    char[] charsL = docL.getChars();
    int[] intsL = DiffUtils.makeIntervals(docL, true);
    char[] charsR = docR.getChars();
    int[] intsR = DiffUtils.makeIntervals(docR, true);

    int[] res = model.findDiffs(charsL, intsL, charsR, intsR);

    DiffInfo info = DiffUtils.readDiffInfo(res);
    if (printResults) DiffUtils.printInfo(info, docL, docR, model.syncL, model.syncR);
  }

  private Document parse(String text) {
    List<Object> result = new ArrayList<>();
    new JavaProxy().parseFullFile(text.toCharArray(), 0, result);
    int[] ints = (int[]) result.get(0);
    char[] source = (char[]) result.get(1);
    Document document = new Document(SplitText.split(text));
    ParserUtils.updateDocument(document, ints, source, null, null, false);
    return document;
  }

}
