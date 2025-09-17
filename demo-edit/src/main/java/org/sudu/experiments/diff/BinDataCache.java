package org.sudu.experiments.diff;

@SuppressWarnings({"FieldCanBeLocal", "unused", "FieldMayBeFinal"})
public class BinDataCache {

  public interface DataSource {
    interface Result {
      void onData(double address, byte[] data);
      void onError(String e);
    }
    void fetch(double address, int length, Result handler);
  }

  private Runnable repaint;
  private final DataSource source;
  private double[] addr = new double[0];
  private byte[][] data = new byte[0][];
  private int frameNo, memory;

  public BinDataCache(DataSource source) {
    this.source = source;
  }

  // repaint is triggered when fetch is completed
  public void setRepaint(Runnable repaint) {
    this.repaint = repaint;
  }

  public void pruneData(int maxMemory) {
    frameNo++;
  }

  // possible results:
  //   - false: data at the address is not present
  //        result.offset is amount of data that not present,
  //        data is fetched from the source
  //   - true: data is present,
  //        result.data is the data
  //        result.offset is offset of data from data[0]
  //        data is fetched from the source if
  //          request length more than available data

  public static class GetResult {
    byte[] data;
    int offset;
  }

  public boolean getOrFetch(double address, int length, GetResult result) {
    return false;

  }
}
