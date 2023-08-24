package org.sudu.experiments.demo.worker.diff;

import java.util.ArrayList;
import java.util.List;

public class LineDiff {

  public List<Integer> diffN, diffM;

  public LineDiff() {
    this.diffN = new ArrayList<>();
    this.diffM = new ArrayList<>();
  }

  public boolean isNotEmpty() {
    return !diffN.isEmpty() && !diffM.isEmpty();
  }

  @Override
  public String toString() {
    return diffN + " --> " + diffM;
  }

}
