package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.DiffTypes;
import java.util.Arrays;

public class FileDiffHandler {

  FileNode left, right;
  byte[] leftText, rightText;

  public FileDiffHandler(FileNode left, FileNode right) {
    this.left = left;
    this.right = right;
  }

  public void sendLeft(byte[] left) {
    this.leftText = left;
    if (this.rightText != null) compare();
  }

  public void sendRight(byte[] right) {
    this.rightText = right;
    if (this.leftText != null) compare();
  }

  public void compare() {
    if (!Arrays.equals(leftText, rightText)) {
      left.status.markUp(DiffTypes.EDITED);
      right.status.markUp(DiffTypes.EDITED);
    }
  }
}
