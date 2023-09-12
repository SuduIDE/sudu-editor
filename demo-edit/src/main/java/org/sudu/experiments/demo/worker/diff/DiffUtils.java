package org.sudu.experiments.demo.worker.diff;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.diff.LineDiff;

import java.util.List;

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
    return new DiffInfo(readLineDiffs(reader), readLineDiffs(reader));
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

  public static void printInfo(DiffInfo info, Document docN, Document docM) {
    int i = 0, j = 0;
    while (i < info.lineDiffsN.length
        && j < info.lineDiffsM.length) {
      var diffN = info.lineDiffsN[i];
      var diffM = info.lineDiffsM[j];
      System.out.println(formatStr(docN, diffN, i) + "\t\t" + formatStr(docM, diffM, j));
      i++;
      j++;
    }
    for (; i < info.lineDiffsN.length; i++) {
      System.out.println(formatStr(docN, info.lineDiffsN[i], i));
    }
    for (; j < info.lineDiffsM.length; j++) {
      System.out.println(" ".repeat(46) + "\t\t" + formatStr(docM, info.lineDiffsM[j], j));
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
}
