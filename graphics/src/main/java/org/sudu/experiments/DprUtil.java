package org.sudu.experiments;

import org.sudu.experiments.math.Numbers;

public class DprUtil {
  public static int toPx(float value, float dpr) {
    return Numbers.iRnd(value * dpr);
  }
}
