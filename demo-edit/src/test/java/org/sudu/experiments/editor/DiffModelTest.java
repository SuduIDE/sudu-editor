package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.ReadResource;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.proxy.JavaProxy;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.text.SplitText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiffModelTest {

  public static void main(String[] args) {
//    new DiffModelTest().compareDocuments(true);
    new DiffModelTest().compareLongSequences(true, 50);
  }

  @Test
  public void compareDocuments() {
    compareDocuments(false);
  }

  @Test
  public void compareLongSequences() {
    compareLongSequences(false, 50);
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

  // 143142, 4893, 20765, 31768
  // Avg time:
  // Math.min(lLen, rLen)                                               | 8891 ms | OK
  // lLen + rLen                                                        | 8179 ms | OK
  // Math.min(20000 + 10 * (int) Math.sqrt(lLen + rLen), lLen + rLen))  | 974  ms | Not OK
  public void compareLongSequences(boolean printResults, int n) {
    boolean check = false;

    var docL = parse(ReadResource.readFile("testSeqL.js", getClass()));
    var docR = parse(ReadResource.readFile("testSeqR.js", getClass()));

    long totalTime = 0L;

    for (int i = 0; i < n; i++) {
      long startTime = System.currentTimeMillis();

      DiffModel model = new DiffModel();
      char[] charsL = docL.getChars();
      int[] intsL = DiffUtils.makeIntervals(docL, false);
      char[] charsR = docR.getChars();
      int[] intsR = DiffUtils.makeIntervals(docR, false);

      int[] res = model.findDiffs(charsL, intsL, charsR, intsR);
      DiffInfo info = DiffUtils.readDiffInfo(res);

      long compareTime = System.currentTimeMillis() - startTime;
      totalTime += compareTime;
      int[] diffs = countDifferences(info);
      if (check) {
        Assertions.assertEquals(143142, diffs[0]);
        Assertions.assertEquals(4893, diffs[1]);
        Assertions.assertEquals(20765, diffs[2]);
        Assertions.assertEquals(31768, diffs[3]);
      }
      if (printResults) {
        System.out.printf("Compared in %d ms: %s\n", compareTime, Arrays.toString(diffs));
      }
    }
    if (n != 0 && printResults) System.out.printf("Avg compare time: %d ms\n", totalTime / n);
  }

  private int[] countDifferences(DiffInfo info) {
    int same = 0,
        deleted = 0,
        inserted = 0,
        edited = 0;

    for (var line: info.lineDiffsL) {
      if (line.elementTypes != null) {
        for (var elem: line.elementTypes)
          switch (elem) {
            case DiffTypes.DEFAULT -> same++;
            case DiffTypes.DELETED -> deleted++;
            case DiffTypes.INSERTED -> inserted++;
            case DiffTypes.EDITED -> edited++;
          }
      }
    }

    for (var line: info.lineDiffsR) {
      if (line.elementTypes != null) {
        for (var elem: line.elementTypes)
          switch (elem) {
            case DiffTypes.DEFAULT -> same++;
            case DiffTypes.DELETED -> deleted++;
            case DiffTypes.INSERTED -> inserted++;
            case DiffTypes.EDITED -> edited++;
          }
      }
    }
    return new int[]{same, deleted, inserted, edited};
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
