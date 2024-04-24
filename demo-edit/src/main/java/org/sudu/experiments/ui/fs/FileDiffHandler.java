package org.sudu.experiments.ui.fs;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class FileDiffHandler {

  private static final int LENGTH = 32 * 1024;

  byte[] leftText, rightText;
  Consumer<Object[]> r;
  FileHandle left, right;
  int start = 0;

  public FileDiffHandler(Consumer<Object[]> r, FileHandle left, FileHandle right) {
    this.r = r;
    this.left = left;
    this.right = right;
  }

  public void beginCompare() {
    this.leftText = null;
    this.rightText = null;
    left.readAsBytes(this::sendLeft, System.err::println, start, LENGTH);
    right.readAsBytes(this::sendRight, System.err::println, start, LENGTH);
  }

  public void sendLeft(byte[] left) {
    this.leftText = left;
    if (this.rightText != null) compare();
  }

  public void sendRight(byte[] right) {
    this.rightText = right;
    if (this.leftText != null) compare();
  }

  private void compare() {
    boolean equals = Arrays.equals(leftText, rightText);
    if (!equals) {
      ArrayList<Object> result = new ArrayList<>();
      result.add(new int[]{0});
      ArrayOp.sendArrayList(result, r);
    } else if (leftText.length < LENGTH) {
      ArrayList<Object> result = new ArrayList<>();
      result.add(new int[]{1});
      ArrayOp.sendArrayList(result, r);
    } else {
      start += LENGTH;
      beginCompare();
    }
  }
}
