package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.CodeLine;
import org.sudu.experiments.editor.Document;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.List;
import java.util.function.Consumer;

public class DiffUtils {

  public static final String FIND_DIFFS = "DiffUtils.findDiffs";

  public static void findDiffs(
      char[] charsN, int[] intsN,
      char[] charsM, int[] intsM,
      List<Object> result
  ) {
    DiffModel model = new DiffModel();
    int[] ints = model.findDiffs(charsN, intsN, charsM, intsM);
    result.add(ints);
  }

  public static int[] makeIntervals(Document document) {
    ArrayWriter writer = new ArrayWriter();
    int N = document.length();
    writer.write(N);
    int offset = 0;
    for (int i = 0; i < N; i++) {
      CodeLine line = document.line(i);
      int Mi = line.length();
      writer.write(Mi);
      for (int j = 0; j < Mi; j++) {
        var elem = line.get(j);
        writer.write(offset, elem.length());
        offset += elem.length();
      }
      offset++;
    }
    return writer.getInts();
  }

  public static DiffInfo readDiffInfo(int[] ints) {
    ArrayReader reader = new ArrayReader(ints);
    return new DiffInfo(
        readLineDiffs(reader),
        readLineDiffs(reader),
        readRanges(reader)
    );
  }

  public static LineDiff[] readLineDiffs(ArrayReader reader) {
    int len = reader.next();
    LineDiff[] lineDiff = new LineDiff[len];
    for (int i = 0; i < len; i++) {
      int line = reader.next();
      if (line == -1) continue;
      int type = reader.next();
      int lineLen = reader.next();
      if (lineLen == -1) lineDiff[line] = new LineDiff(type);
      else {
        lineDiff[line] = new LineDiff(type, lineLen);
        for (int j = 0; j < lineLen; j++) {
          lineDiff[line].elementTypes[j] = reader.next();
        }
      }
    }
    return lineDiff;
  }

  public static DiffRange[] readRanges(ArrayReader reader) {
    int len = reader.next();
    DiffRange[] ranges = new DiffRange[len];
    for (int i = 0; i < len; i++) {
      int fromL = reader.next();
      int lengthL = reader.next();
      int fromR = reader.next();
      int lengthR = reader.next();
      int type = reader.next();
      ranges[i] = new DiffRange(fromL, lengthL, fromR, lengthR, type);
    }
    return ranges;
  }

  public static void printInfo(DiffInfo info, Document docN, Document docM) {
    int i = 0, j = 0;
    while (i < info.lineDiffsL.length
        && j < info.lineDiffsR.length) {
      var diffN = info.lineDiffsL[i];
      var diffM = info.lineDiffsR[j];
      System.out.println(formatStr(docN, diffN, i) + "\t\t" + formatStr(docM, diffM, j));
      i++;
      j++;
    }
    for (; i < info.lineDiffsL.length; i++) {
      System.out.println(formatStr(docN, info.lineDiffsL[i], i));
    }
    for (; j < info.lineDiffsR.length; j++) {
      System.out.println(" ".repeat(46) + "\t\t" + formatStr(docM, info.lineDiffsR[j], j));
    }
  }

  private static String formatStr(Document doc, LineDiff diff, int ind) {
    String line = doc.line(ind).makeString();
    if (line.length() < 40) line = line + " ".repeat(40 - line.length());

    if (diff == null) return String.format("%4d  %.40s", ind + 1, line);
    else if (diff.type == LineDiff.DELETED) return String.format("%4d- %.40s", ind + 1, line);
    else if (diff.type == LineDiff.INSERTED) return String.format("%4d+ %.40s", ind + 1, line);
    else if (diff.type == LineDiff.EDITED) return String.format("%4d# %.40s", ind + 1, line);
    return String.format("%4d  %.40s", ind + 1, line);
  }

  public static void findDiffs(
      Document document1,
      Document document2,
      Consumer<DiffInfo> result,
      WorkerJobExecutor window
  ) {
    char[] chars1 = document1.getChars();
    char[] chars2 = document2.getChars();
    int[] intervals1 = makeIntervals(document1);
    int[] intervals2 = makeIntervals(document2);

    window.sendToWorker(
        r -> {
          int[] reply = ((ArrayView) r[0]).ints();
          DiffInfo model = readDiffInfo(reply);
          result.accept(model);
        }, FIND_DIFFS,
        chars1, intervals1, chars2, intervals2);
  }
}
