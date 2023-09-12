package org.sudu.experiments.diff;

import java.util.ArrayList;
import java.util.List;

public class Diff<S> {

  public List<S> diffM = new ArrayList<>();
  public List<S> diffN = new ArrayList<>();

  public boolean isDeletion() {
    return !diffN.isEmpty() && diffM.isEmpty();
  }

  public boolean isInsertion() {
    return diffN.isEmpty() && !diffM.isEmpty();
  }

  public boolean isEdition() {
    return !diffN.isEmpty() && !diffM.isEmpty();
  }

  public boolean isNotEmpty() {
    return !(diffN.isEmpty() && diffM.isEmpty());
  }

  @Override
  public String toString() {
    return diffN.toString() + " -> " + diffM.toString();
  }

}
