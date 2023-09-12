package org.sudu.experiments.demo.worker.diff;

import org.sudu.experiments.diff.LineDiff;

public class DiffInfo {

  public LineDiff[] lineDiffsN;

  public LineDiff[] lineDiffsM;

  public DiffInfo(LineDiff[] lineDiffsN, LineDiff[] lineDiffsM) {
    this.lineDiffsN = lineDiffsN;
    this.lineDiffsM = lineDiffsM;
  }
}
