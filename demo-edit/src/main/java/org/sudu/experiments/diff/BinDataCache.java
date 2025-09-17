package org.sudu.experiments.diff;

public class BinDataCache {

  public interface DataSource {
    interface Result {
      void onData(double address, byte[] data);
      void onError(String e);
    }
    void fetch(double address, int length, Result handler);
  }

  double[] addr = new double[0];
  byte[][] data = new byte[0][];

  public BinDataCache(double[] addr) {

  }
}
