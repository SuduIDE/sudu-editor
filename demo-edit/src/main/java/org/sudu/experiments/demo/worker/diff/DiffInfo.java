package org.sudu.experiments.demo.worker.diff;

import org.sudu.experiments.diff.LineDiff;

public class DiffInfo {

  public LineDiff[] lineDiffsL;

  public LineDiff[] lineDiffsR;

  public DiffInfo(LineDiff[] lineDiffsL, LineDiff[] lineDiffsR) {
    this.lineDiffsL = lineDiffsL;
    this.lineDiffsR = lineDiffsR;
  }
}
