package org.sudu.experiments.demo.worker.diff;

import org.sudu.experiments.parser.common.Pos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiffInfo {

  public List<LineDiff> lineDiffs = new ArrayList<>();
  public List<ElemDiff> elemDiffs = new ArrayList<>();

  public Set<Integer> linesDeletions = new HashSet<>();
  public Set<Integer> linesInsertions = new HashSet<>();
  public Set<Integer> linesEditN = new HashSet<>();
  public Set<Integer> linesEditM = new HashSet<>();
  public Set<Pos> elemsDeletions = new HashSet<>();
  public Set<Pos> elemsInsertions = new HashSet<>();
  public Set<Pos> elemsEditN = new HashSet<>();
  public Set<Pos> elemsEditM = new HashSet<>();

  public void sort() {
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
