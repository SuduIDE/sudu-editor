package org.sudu.experiments.diff.utils;

import java.util.*;

public class Utils {

  public static int[] toIntArray(List<Integer> list) {
    int[] result = new int[list.size()];
    int ptr = 0;
    for (var i: list) result[ptr++] = i;
    return result;
  }

  public static int[][] toIntIntArray(List<int[]> list) {
    int[][] result = new int[list.size()][2];
    int ptr = 0;
    for (var i: list) result[ptr++] = i;
    return result;
  }

  public static int[][][] dropUnique(int[][] intsL, int[][] intsR, int maxEnum) {
    int[][] preparedL = removeUnique(intsR, intsL, maxEnum);
    int[][] preparedR = removeUnique(preparedL, intsR, maxEnum);
    return new int[][][] {preparedL, preparedR};
  }

  private static int[][] removeUnique(int[][] needed, int[][] toRemove, int maxEnum) {
    BitSet presented = new BitSet(maxEnum);
    for (var need: needed) presented.set(need[0]);
    List<int[]> notUnique = new ArrayList<>();
    for (int[] elem : toRemove) if (presented.get(elem[0])) notUnique.add(elem);
    return Utils.toIntIntArray(notUnique);
  }
}
