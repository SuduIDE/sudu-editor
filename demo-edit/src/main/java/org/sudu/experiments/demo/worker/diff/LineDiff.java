package org.sudu.experiments.demo.worker.diff;

import java.util.ArrayList;
import java.util.List;

public class LineDiff {

  public List<Integer>
      diffN = new ArrayList<>(),
      diffM = new ArrayList<>();

  @Override
  public String toString() {
    return diffN + " --> " + diffM;
  }

}
