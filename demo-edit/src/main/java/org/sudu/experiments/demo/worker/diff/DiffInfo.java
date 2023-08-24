package org.sudu.experiments.demo.worker.diff;

import org.sudu.experiments.parser.common.Pos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiffInfo {

  public List<LineDiff> lineDiffs;
  public List<ElemDiff> elemDiffs;

  public Set<Integer> linesDeletions, linesInsertions, linesEditN, linesEditM;
  public Set<Pos> elemsDeletions, elemsInsertions, elemsEditN, elemsEditM;

  public DiffInfo() {
    this.lineDiffs = new ArrayList<>();
    this.elemDiffs = new ArrayList<>();
  }

  public void sort() {
    linesDeletions = new HashSet<>();
    linesInsertions = new HashSet<>();
    linesEditN = new HashSet<>();
    linesEditM = new HashSet<>();
    elemsDeletions = new HashSet<>();
    elemsInsertions = new HashSet<>();
    elemsEditN = new HashSet<>();
    elemsEditM = new HashSet<>();

    for (var lineDiff: lineDiffs) {
      if (lineDiff.diffM.isEmpty()) linesDeletions.addAll(lineDiff.diffN);
      else if (lineDiff.diffN.isEmpty()) linesInsertions.addAll(lineDiff.diffM);
      else {
        linesEditN.addAll(lineDiff.diffN);
        linesEditM.addAll(lineDiff.diffM);
      }
    }
    for (var elemDiff: elemDiffs) {
      if (elemDiff.diffM.isEmpty()) elemsDeletions.addAll(elemDiff.diffN);
      else if (elemDiff.diffN.isEmpty()) elemsInsertions.addAll(elemDiff.diffM);
      else {
        elemsEditN.addAll(elemDiff.diffN);
        elemsEditM.addAll(elemDiff.diffM);
      }
    }
  }
}
