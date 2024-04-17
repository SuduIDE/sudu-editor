package org.sudu.experiments.diff.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Utils {

  public static Comparator<int[]> CMP = Comparator.comparing(a -> a[0]);

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

  public static int[][][] dropUnique(int[][] intsL, int[][] intsR) {
    int[][] preparedL = removeUnique(intsR, intsL);
    int[][] preparedR = removeUnique(preparedL, intsR);
    return new int[][][] {preparedL, preparedR};
  }

  private static int[][] removeUnique(int[][] needed, int[][] toRemove) {
    int[][] sortedNeeded = createSorted(needed);
    List<int[]> notUnique = new ArrayList<>();
    for (int[] elem : toRemove) {
      if (Arrays.binarySearch(sortedNeeded, elem, CMP) >= 0) {
        notUnique.add(elem);
      }
    }
    return Utils.toIntIntArray(notUnique);
  }

  private static int[][] createSorted(int[][] ints) {
    int[][] sorted = Arrays.copyOf(ints, ints.length);
    Arrays.sort(sorted, CMP);
    return sorted;
  }
}
