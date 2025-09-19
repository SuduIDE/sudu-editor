package org.sudu.experiments.diff;

import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;
import java.util.TreeSet;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class BinDataCache {

  public interface DataSource {
    interface Result {
      void onData(double address, byte[] data);
      void onError(double address, String e);
    }
    void fetch(double address, int length, Result handler);
  }

  private Runnable repaint;
  private final DataSource source;
  private double[] addr = new double[0];
  private byte[][] data = new byte[0][];
  private final int chunkSize;
  private int frameNo, memory;
  private final TreeSet<Double> requestMap = new TreeSet<>();

  private final DataSource.Result onData = onData();

  // repaint is triggered when fetch is completed
  public BinDataCache(
      DataSource source, int chunkSize, Runnable repaint
  ) {
    this.source = source;
    this.chunkSize = chunkSize;
    this.repaint = repaint;
  }

  public void pruneData(int maxMemory) {
    frameNo++;
  }

  public int memory() {
    return memory;
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
    public byte[] data;
    public int offset;
  }

  // we assume that fetch chunkSize == length
  public boolean getOrFetch(double address, GetResult result) {
    int s = Arrays.binarySearch(addr, address);
    if (s >= 0) {
      result.data =  data[s];
      result.offset = (int) (addr[s] - address);
      return true;
    } else {
      s =  -s - 1;

      if (s > 0 && s - 1 < addr.length) {
        double cAddress = addr[s - 1];
        byte[] cData = data[s - 1];
        if (cAddress < address && address < cAddress + cData.length) {
          result.data = cData;
          result.offset = (int) (address - cAddress);
          return true;
        }
      }

      result.data = null;
      if (addr.length > s) {
        result.offset = (int) (addr[s] - address);
      } else {
        result.offset = chunkSize;
      }
      double requestAddress = address - address % chunkSize;
      Double key = requestAddress;
      if (!requestMap.contains(key)) {
        source.fetch(requestAddress, chunkSize, onData);
        requestMap.add(key);
      }
      return false;
    }
  }

  private DataSource.Result onData() {
    return new BinDataCache.DataSource.Result() {
      @Override
      public void onData(double address, byte[] values) {
        int s = -Arrays.binarySearch(addr, address) - 1;
        if (s < 0) {
          System.err.println("BinDataCache: double fetch at address " + address);
        } else {
          addr = ArrayOp.insertAt(address, addr, s);
          data = ArrayOp.insertAt(values, data, s);
          memory += values.length;
          repaint.run();
        }
      }

      @Override
      public void onError(double address, String e) {
        System.err.println("BinDataCache: error fetching data at " + address + ": " + e);
      }
    };
  }
}
