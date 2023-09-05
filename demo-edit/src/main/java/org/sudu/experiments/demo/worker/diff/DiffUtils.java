package org.sudu.experiments.demo.worker.diff;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.demo.CodeLine;
import org.sudu.experiments.demo.Document;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.DiffModel;
import org.sudu.experiments.parser.common.Pos;

import java.util.List;
import java.util.stream.Collectors;

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
    DiffInfo info = new DiffInfo();
    ArrayReader reader = new ArrayReader(ints);
    int linesDiffLen = reader.next();
    for (int i = 0; i < linesDiffLen; i++) {
      LineDiff lineDiff = new LineDiff();
      int lenN = reader.next();
      for (int j = 0; j < lenN; j++) lineDiff.diffN.add(reader.next());
      int lenM = reader.next();
      for (int j = 0; j < lenM; j++) lineDiff.diffM.add(reader.next());
      info.lineDiffs.add(lineDiff);
    }
    int elemsDiffLen = reader.next();
    for (int i = 0; i < elemsDiffLen; i++) {
      ElemDiff elemDiff = new ElemDiff();
      int lenN = reader.next();
      for (int j = 0; j < lenN; j++) elemDiff.diffN.add(new Pos(reader.next(), reader.next()));
      int lenM = reader.next();
      for (int j = 0; j < lenM; j++) elemDiff.diffM.add(new Pos(reader.next(), reader.next()));
      info.elemDiffs.add(elemDiff);
    }
    return info;
  }

  public static void printInfo(DiffInfo info) {
    for (var lineDiff: info.lineDiffs) System.out.println(lineDiff);
    System.out.println();
    for (var elemDiff: info.elemDiffs) System.out.println(elemDiff);
  }

  public static void printInfo(DiffInfo info, Document docN, Document docM) {
    for (var lineDiff: info.lineDiffs) printInfo(lineDiff, docN, docM);
    System.out.println();
    for (var elemDiff: info.elemDiffs) printInfo(elemDiff, docN, docM);
  }

  public static void printInfo(LineDiff lineDiff, Document docN, Document docM) {
    System.out.print(lineDiff.diffN.stream()
        .map(docN::line)
        .map(CodeLine::makeString)
        .toList()
    );
    System.out.print(" --> ");
    System.out.println(lineDiff.diffM.stream()
        .map(docM::line)
        .map(CodeLine::makeString)
        .toList()
    );
  }

  public static void printInfo(ElemDiff elemDiff, Document docN, Document docM) {
    System.out.print(elemDiff.diffN.stream()
        .map(elem -> docN.line(elem.line).get(elem.pos))
        .map(it -> it.s)
        .collect(Collectors.joining("", "[", "]"))
    );
    System.out.print(" --> ");
    System.out.println(elemDiff.diffM.stream()
        .map(elem -> docM.line(elem.line).get(elem.pos))
        .map(it -> it.s)
        .collect(Collectors.joining("", "[", "]"))
    );
  }
}
