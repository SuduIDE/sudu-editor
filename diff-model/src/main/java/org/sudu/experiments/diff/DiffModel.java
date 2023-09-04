package org.sudu.experiments.diff;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DiffModel {

  public List<Diff<CodeLineS>> linesDiffs = new ArrayList<>();
  public List<Diff<CodeElementS>> elementsDiffs = new ArrayList<>();

  public int[] findDiffs(
      char[] charsN, int[] intsN,
      char[] charsM, int[] intsM
  ) {
    var docN = readLines(charsN, intsN);
    var docM = readLines(charsM, intsM);
    findLinesDiff(docN, docM);
    return writeResults();
  }

  void findLinesDiff(CodeLineS[] docN, CodeLineS[] docM) {
    prepare(docN);
    prepare(docM);

    LCS<CodeLineS> lcs = new LCS<>(docN, docM);
    int[][] lcsMatrix = lcs.countLCSMatrix();
    List<CodeLineS> common = lcs.findCommon(lcsMatrix);
    lcs.countDiffs(common);
    for (var diff: lcs.diffs) {
      linesDiffs.add(diff);
      if (!diff.diffN.isEmpty() && !diff.diffM.isEmpty()) {
        findElementsDiff(flatElements(diff.diffN), flatElements(diff.diffM));
      }
    }
  }

  private void findElementsDiff(CodeElementS[] linesN, CodeElementS[] linesM) {
    LCS<CodeElementS> lcs = new LCS<>(linesN, linesM);
    int[][] lcsMatrix = lcs.countLCSMatrix();
    List<CodeElementS> common = lcs.findCommon(lcsMatrix);
    lcs.countDiffs(common);
    elementsDiffs.addAll(lcs.diffs);
  }

  private CodeElementS[] flatElements(CodeLineS[] lines) {
    return flatElements(Arrays.stream(lines));
  }

  private CodeElementS[] flatElements(List<CodeLineS> lines) {
    return flatElements(lines.stream());
  }

  private CodeElementS[] flatElements(Stream<CodeLineS> stream) {
    return stream.flatMap(it -> Arrays.stream(it.elements)).toArray(CodeElementS[]::new);
  }

  private void prepare(CodeLineS[] doc) {
    for (int i = 0; i < doc.length; i++) {
      doc[i].lineNum = i;
      for (int j = 0; j < doc[i].elements.length; j++) {
        doc[i].elements[j].lineNum = i;
        doc[i].elements[j].elemNum = j;
      }
    }
  }

  private CodeLineS[] readLines(char[] chars, int[] ints) {
    ArrayReader reader = new ArrayReader(ints);
    int N = reader.next();
    CodeLineS[] lines = new CodeLineS[N];
    for (int i = 0; i < N; i++) {
      int Mi = reader.next();
      CodeElementS[] elements = new CodeElementS[Mi];
      for (int j = 0; j < Mi; j++) {
        int offset = reader.next(), count = reader.next();
        String s = new String(chars, offset, count);
        elements[j] = new CodeElementS(s);
      }
      lines[i] = new CodeLineS(elements);
    }
    return lines;
  }

  private int[] writeResults() {
    ArrayWriter writer = new ArrayWriter();
    writer.write(linesDiffs.size());
    for (var diff: linesDiffs) {
      writer.write(diff.diffN.size());
      for (var diffN: diff.diffN) writer.write(diffN.lineNum);
      writer.write(diff.diffM.size());
      for (var diffM: diff.diffM) writer.write(diffM.lineNum);
    }
    writer.write(elementsDiffs.size());
    for (var diff: elementsDiffs) {
      writer.write(diff.diffN.size());
      for (var diffN: diff.diffN) writer.write(diffN.lineNum, diffN.elemNum);
      writer.write(diff.diffM.size());
      for (var diffM: diff.diffM) writer.write(diffM.lineNum, diffM.elemNum);
    }
    return writer.getInts();
  }

  void printResults() {
    System.out.println("Lines Edits: ");
    for (var diff: linesDiffs) {
      System.out.println(diff);
    }
    System.out.println();
    System.out.println("Elements Edits: ");
    for (var diff: elementsDiffs) {
      System.out.println(diff);
    }
  }
}
