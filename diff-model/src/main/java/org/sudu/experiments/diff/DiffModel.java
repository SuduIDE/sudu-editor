package org.sudu.experiments.diff;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DiffModel {

  public LineDiff[] lineDiffsN, lineDiffsM;
  public List<BaseRange<CodeLineS>> linesRanges;

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
    lineDiffsN = new LineDiff[docN.length];
    lineDiffsM = new LineDiff[docM.length];

    prepare(docN);
    prepare(docM);

    LCS<CodeLineS> lcs = new LCS<>(docN, docM);
    int[][] lcsMatrix = lcs.countLCSMatrix();
    List<CodeLineS> common = lcs.findCommon(lcsMatrix);
    lcs.countDiffs(common);
    this.linesRanges = lcs.ranges;
    for (var range: lcs.ranges) {
      if (!(range instanceof Diff<CodeLineS> diff)) continue;
      if (diff.isDeletion()) handleDeletion(diff);
      else if (diff.isInsertion()) handleInsertion(diff);
      else if (diff.isEdition()) handleEdition(diff);
    }
  }

  private List<BaseRange<CodeElementS>> findElementsDiff(CodeElementS[] linesN, CodeElementS[] linesM) {
    LCS<CodeElementS> lcs = new LCS<>(linesN, linesM);
    int[][] lcsMatrix = lcs.countLCSMatrix();
    List<CodeElementS> common = lcs.findCommon(lcsMatrix);
    lcs.countDiffs(common);
    return lcs.ranges;
  }

  private void handleDeletion(Diff<CodeLineS> diff) {
    diff.diffN.forEach(line -> lineDiffsN[line.lineNum] = new LineDiff(LineDiff.DELETED));
  }

  private void handleElemDeletion(Diff<CodeElementS> diff) {
    diff.diffN.forEach(elem -> lineDiffsN[elem.lineNum].elementTypes[elem.elemNum] = LineDiff.DELETED);
  }

  private void handleInsertion(Diff<CodeLineS> diff) {
    diff.diffM.forEach(line -> lineDiffsM[line.lineNum] = new LineDiff(LineDiff.INSERTED));
  }

  private void handleElemInsertion(Diff<CodeElementS> diff) {
    diff.diffM.forEach(elem -> lineDiffsM[elem.lineNum].elementTypes[elem.elemNum] = LineDiff.INSERTED);
  }

  private void handleEdition(Diff<CodeLineS> diff) {
    diff.diffN.forEach(line -> lineDiffsN[line.lineNum] = new LineDiff(LineDiff.EDITED, line.len()));
    diff.diffM.forEach(line -> lineDiffsM[line.lineNum] = new LineDiff(LineDiff.EDITED, line.len()));

    var elementsDiffs = findElementsDiff(flatElements(diff.diffN), flatElements(diff.diffM));
    elementsDiffs.forEach(elRange -> {
      if (!(elRange instanceof Diff<CodeElementS> elDiff)) return;
      if (elDiff.isDeletion()) handleElemDeletion(elDiff);
      else if (elDiff.isInsertion()) handleElemInsertion(elDiff);
      else if (elDiff.isEdition()) handleElemEdition(elDiff);
    });
  }

  private void handleElemEdition(Diff<CodeElementS> diff) {
    diff.diffN.forEach(elem -> lineDiffsN[elem.lineNum].elementTypes[elem.elemNum] = LineDiff.EDITED);
    diff.diffM.forEach(elem -> lineDiffsM[elem.lineNum].elementTypes[elem.elemNum] = LineDiff.EDITED);
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
    writeResults(writer, lineDiffsN);
    writeResults(writer, lineDiffsM);
    writeRanges(writer);
    return writer.getInts();
  }

  private void writeResults(ArrayWriter writer, LineDiff[] diff) {
    writer.write(diff.length);
    for (int i = 0; i < diff.length; i++) {
      if (diff[i] == null) {
        writer.write(-1);
        continue;
      }
      writer.write(i, diff[i].type);
      if (diff[i].elementTypes == null) writer.write(-1);
      else {
        writer.write(diff[i].elementTypes.length);
        for(int type: diff[i].elementTypes) writer.write(type);
      }
    }
  }

  private void writeRanges(ArrayWriter writer) {
    writer.write(linesRanges.size());
    for (var range: linesRanges) {
      writer.write(range.fromL);
      writer.write(range.lengthL());
      writer.write(range.fromR);
      writer.write(range.lengthR());
      if (range instanceof Diff<CodeLineS> diff)
        writer.write(diff.getType());
      else writer.write(0);
    }
  }
}
