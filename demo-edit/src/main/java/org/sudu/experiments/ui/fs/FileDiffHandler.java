package org.sudu.experiments.ui.fs;

import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class FileDiffHandler {

  byte[] leftText, rightText;
  Consumer<Object[]> r;

  public FileDiffHandler(Consumer<Object[]> r) {
    this.r = r;
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
    ArrayList<Object> result = new ArrayList<>();
    if (Arrays.equals(leftText, rightText)) result.add(new int[]{1});
    else result.add(new int[]{0});
    ArrayOp.sendArrayList(result, r);
  }
}
