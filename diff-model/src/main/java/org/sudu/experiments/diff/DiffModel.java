package org.sudu.experiments.diff;

import org.sudu.experiments.arrays.ArrayReader;
import org.sudu.experiments.arrays.ArrayWriter;
import org.sudu.experiments.diff.lcs.DummyLCS;
import org.sudu.experiments.diff.lcs.LCS;
import org.sudu.experiments.diff.lcs.MyersLCS;
import org.sudu.experiments.diff.ranges.BaseRange;
import org.sudu.experiments.diff.ranges.CommonRange;
import org.sudu.experiments.diff.ranges.Diff;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.utils.Enumerator;
import org.sudu.experiments.utils.Utils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DiffModel {

  public LineDiff[] lineDiffsN, lineDiffsM;
  public List<BaseRange<CodeLineS>> linesRanges;
  private static final boolean PRINT_LCS_TIME = false;

  public int[] findDiffs(
      char[] charsN, int[] intsN,
      char[] charsM, int[] intsM
  ) {
    long time = System.currentTimeMillis();
    var docN = readLines(charsN, intsN);
    var docM = readLines(charsM, intsM);
    findLinesDiff(docN, docM);
    var ints = writeResults();
    if (PRINT_LCS_TIME) System.out.println("Counted document diff in " + (System.currentTimeMillis() - time) + " ms");
    return ints;
  }

  void findLinesDiff(CodeLineS[] docN, CodeLineS[] docM) {
    lineDiffsN = new LineDiff[docN.length];
    lineDiffsM = new LineDiff[docM.length];

    prepare(docN);
    prepare(docM);

    linesRanges = countRanges(docN, docM);
    for (var range: linesRanges) {
      if (!(range instanceof Diff<CodeLineS> diff)) continue;
      if (diff.isDeletion()) handleDeletion(diff);
      else if (diff.isInsertion()) handleInsertion(diff);
      else if (diff.isEdition()) handleEdition(diff);
    }
  }

  private List<BaseRange<CodeElementS>> findElementsDiff(CodeElementS[] elemsN, CodeElementS[] elemsM) {
    return countRanges(elemsN, elemsM);
  }

  private void handleDeletion(Diff<CodeLineS> diff) {
    diff.diffN.forEach(line -> lineDiffsN[line.lineNum] = new LineDiff(DiffTypes.DELETED));
  }

  private void handleElemDeletion(Diff<CodeElementS> diff) {
    diff.diffN.forEach(elem -> lineDiffsN[elem.lineNum].elementTypes[elem.elemNum] = DiffTypes.DELETED);
  }

  private void handleInsertion(Diff<CodeLineS> diff) {
    diff.diffM.forEach(line -> lineDiffsM[line.lineNum] = new LineDiff(DiffTypes.INSERTED));
  }

  private void handleElemInsertion(Diff<CodeElementS> diff) {
    diff.diffM.forEach(elem -> lineDiffsM[elem.lineNum].elementTypes[elem.elemNum] = DiffTypes.INSERTED);
  }

  private void handleEdition(Diff<CodeLineS> diff) {
    diff.diffN.forEach(line -> lineDiffsN[line.lineNum] = new LineDiff(DiffTypes.EDITED, line.len()));
    diff.diffM.forEach(line -> lineDiffsM[line.lineNum] = new LineDiff(DiffTypes.EDITED, line.len()));

    var elementsDiffs = findElementsDiff(flatElements(diff.diffN), flatElements(diff.diffM));
    elementsDiffs.forEach(elRange -> {
      if (!(elRange instanceof Diff<CodeElementS> elDiff)) return;
      if (elDiff.isDeletion()) handleElemDeletion(elDiff);
      else if (elDiff.isInsertion()) handleElemInsertion(elDiff);
      else if (elDiff.isEdition()) handleElemEdition(elDiff);
    });
  }

  private void handleElemEdition(Diff<CodeElementS> diff) {
    diff.diffN.forEach(elem -> lineDiffsN[elem.lineNum].elementTypes[elem.elemNum] = DiffTypes.EDITED);
    diff.diffM.forEach(elem -> lineDiffsM[elem.lineNum].elementTypes[elem.elemNum] = DiffTypes.EDITED);
  }

  private static CodeElementS[] flatElements(CodeLineS[] lines) {
    return flatElements(Arrays.stream(lines));
  }

  public static CodeElementS[] flatElements(List<CodeLineS> lines) {
    return flatElements(lines.stream());
  }

  private static CodeElementS[] flatElements(Stream<CodeLineS> stream) {
    return stream.flatMap(it -> Arrays.stream(it.elements)).toArray(CodeElementS[]::new);
  }

  public static void prepare(CodeLineS[] doc) {
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
        for (int type: diff[i].elementTypes) writer.write(type);
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

  public static <S> List<BaseRange<S>> countRanges(S[] L, S[] R) {
    return countRanges(L, R, DiffModel::getMyersLCS);
  }

  public static <S> Pair<Integer, BitSet[]> countFolderCommon(S[] L, S[] R) {
    Enumerator<S> enumerator = new Enumerator<>(L);
    int[] rightEnum = enumerator.enumerate(R);

    BitSet leftCommon = new BitSet();
    BitSet rightCommon = new BitSet();
    int commonLen = 0;
    for (int i = 0; i < rightEnum.length; i++) {
      int rightNode = rightEnum[i];
      if (rightNode >= L.length) continue;
      leftCommon.set(rightNode);
      rightCommon.set(i);
      commonLen++;
    }
    return new Pair<>(commonLen, new BitSet[]{leftCommon, rightCommon});
  }

  public static <S> List<BaseRange<S>> countRanges(S[] L, S[] R, BiFunction<int[][], int[][], LCS> getLCS) {
    long time = System.currentTimeMillis();
    int lLen = L.length, rLen = R.length;
    int minLen = Math.min(lLen, rLen);

    int start = 0, endCut = 0;
    for (; start < minLen && L[start].equals(R[start]); start++) ;
    for (; endCut < minLen - start && L[lLen - endCut - 1].equals(R[rLen - endCut - 1]); endCut++) ;
    if (lLen == rLen && start == minLen) return singleCommon(minLen);

    var enumerator = new Enumerator<S>();
    var prepL = enumerator.enumerateWithPositions(L, start, endCut);
    var prepR = enumerator.enumerateWithPositions(R, start, endCut);
    var discardedLR = Utils.dropUnique(prepL, prepR, enumerator.counter);
    if (discardedLR[0].length == 0 && discardedLR[1].length == 0) return fastDiff(L, R, start, endCut);

    LCS lcs = getLCS.apply(discardedLR[0], discardedLR[1]);
    var ranges = lcs.countRanges(L, R, start, endCut);
    if (PRINT_LCS_TIME) System.out.println("Counted in " + (System.currentTimeMillis() - time) + " ms\n");
    return ranges;
  }

  public static DummyLCS getDummyLCS(int[][] L, int[][] R) {
    if (PRINT_LCS_TIME) System.out.println("Dummy LCS for L.len = " + L.length + ", R.len = " + R.length);
    return new DummyLCS(L, R);
  }

  public static MyersLCS getMyersLCS(int[][] L, int[][] R) {
    if (PRINT_LCS_TIME) System.out.println("Myers LCS for L.len = " + L.length + ", R.len = " + R.length);
    return new MyersLCS(L, R);
  }

  private static <S> List<BaseRange<S>> singleCommon(int len) {
    return Collections.singletonList(new CommonRange<>(0, 0, len));
  }

  private static <S> List<BaseRange<S>> fastDiff(S[] L, S[] R, int start, int endCut) {
    var ranges = new ArrayList<BaseRange<S>>();
    if (start != 0) ranges.add(new CommonRange<>(0, 0, start));
    Diff<S> diff = new Diff<>(start, start);
    diff.diffN.addAll(Arrays.asList(Arrays.copyOfRange(L, start, L.length - endCut)));
    diff.diffM.addAll(Arrays.asList(Arrays.copyOfRange(R, start, R.length - endCut)));
    ranges.add(diff);
    if (endCut != 0) ranges.add(new CommonRange<>(L.length - endCut, R.length - endCut, endCut));
    return ranges;
  }
}
