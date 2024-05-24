package org.sudu.experiments.utils;

import java.util.HashMap;
import java.util.Map;

public class Enumerator<S> {

  public int counter;
  private final Map<S, Integer> objToInt;

  public Enumerator() {
    counter = 0;
    objToInt = new HashMap<>();
  }

  public Enumerator(S[] initialObjects) {
    this();
    enumerate(initialObjects);
  }

  public int[] enumerate(S[] objects) {
    return enumerate(objects, 0, 0);
  }

  public int[] enumerate(S[] objects, int from, int toCut) {
    int len = objects.length - from - toCut;
    int[] res = new int[len];
    for (int i = from; i < objects.length - toCut; i++)
      res[i - from] = enumerate(objects[i]);
    return res;
  }

  public int[][] enumerateWithPositions(S[] objects, int from, int toCut) {
    int len = objects.length - from - toCut;
    int[][] res = new int[len][2];
    for (int i = from; i < objects.length - toCut; i++) {
      res[i - from][0] = enumerate(objects[i]);
      res[i - from][1] = i;
    }
    return res;
  }

  private int enumerate(S obj) {
    Integer number = objToInt.get(obj);
    if (number != null) return number;
    int nextNumber = counter++;
    objToInt.put(obj, nextNumber);
    return nextNumber;
  }
}
