package org.sudu.experiments.diff;

import java.util.ArrayList;
import java.util.List;

public class Diff<S> {

  public List<S> diffM = new ArrayList<>();
  public List<S> diffN = new ArrayList<>();

  public boolean isNotEmpty() {
    return !(diffN.isEmpty() && diffM.isEmpty());
  }

  @Override
  public String toString() {
    return diffN.toString() + " -> " + diffM.toString();
  }

}
