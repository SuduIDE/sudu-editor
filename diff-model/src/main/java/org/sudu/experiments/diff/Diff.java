package org.sudu.experiments.diff;

import java.util.ArrayList;
import java.util.List;

public class Diff<S> extends BaseRange<S> {

  public List<S> diffM = new ArrayList<>();
  public List<S> diffN = new ArrayList<>();

  public Diff(int fromL, int fromR) {
    super(fromL, fromR);
  }

  public int getType() {
    if (isDeletion()) return LineDiff.DELETED;
    else if (isInsertion()) return LineDiff.INSERTED;
    else if (isEdition()) return LineDiff.EDITED;
    else return LineDiff.DEFAULT;
  }

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
  public int lengthL() {
    return diffN.size();
  }

  @Override
  public int lengthR() {
    return diffM.size();
  }

  @Override
  public String toString() {
    return diffN.toString() + " -> " + diffM.toString();
  }
}
