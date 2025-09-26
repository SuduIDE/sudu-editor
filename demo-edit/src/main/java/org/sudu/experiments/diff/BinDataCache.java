package org.sudu.experiments.diff;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class BinDataCache {


  public interface DataSource {
    interface Result {
      void onData(double address, byte[] data);
      void onError(double address, String e);
    }
    void fetchSize(DoubleConsumer result, Consumer<String> onError);
    void fetch(double address, int chinkSize, Result handler);
  }

  private final Runnable repaint;
  private final DataSource source;
  private double[] addr = new double[0];
  private byte[][] data = new byte[0][];
  private int[] useTime = new int[0];
  private final int chunkSize;
  private int frameNo, memory;
  private double eofAddress = -1;
  private final TreeSet<Double> requestMap = new TreeSet<>();
  private final DataSource.Result onData = onData();
  private Consumer<String> onError;

  // repaint is triggered when fetch is completed
  public BinDataCache(
      DataSource source, int chunkSize, Runnable repaint
  ) {
    this.source = source;
    this.chunkSize = chunkSize;
    this.repaint = repaint;
  }

  public void setOnError(Consumer<String> onError) {
    this.onError = onError;
  }

  public void fetchSize(DoubleConsumer result, Consumer<String> onError) {
    source.fetchSize(size -> {
      eofAddress = size;
      result.accept(size);
    }, onError);
  }

  public void pruneData(int maxMemory) {
    frameNo++;
    while (useTime.length > 0 && memory > maxMemory) {
      int length = useTime.length;
      int oldestIndex = length - 1;
      int oldestTime = useTime[oldestIndex];
      for (int i = 0; i < length - 1; i++) {
        int time =  useTime[i];
        if (time < oldestTime) {
          oldestIndex = i;
          oldestTime = time;
        }
      }
      int removedBytes = data[oldestIndex].length;
      memory -= removedBytes;
      addr = ArrayOp.removeAt(addr, oldestIndex);
      data = ArrayOp.removeAt(data, oldestIndex);
      useTime = ArrayOp.removeAt(useTime, oldestIndex);
      if (false)
        System.out.println("remove data: tTime = " + oldestTime +
            ", l = " + removedBytes + ", memory  = " + memory);
    }
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
    if (eofAddress >= 0 && address >= eofAddress) {
      result.data = null;
      result.offset = chunkSize;
      return false;
    }

    int s = Arrays.binarySearch(addr, address);
    if (s >= 0) {
      result.data = data[s];
      result.offset = (int) (addr[s] - address);
      useTime[s] = frameNo;
      return true;
    } else {
      s =  -s - 1;

      double requestAddress = address - address % chunkSize;

      if (s > 0 && s - 1 < addr.length) {
        double cAddress = addr[s - 1];
        byte[] cData = data[s - 1];
        if (cAddress < address && address < cAddress + cData.length) {
          result.data = cData;
          result.offset = (int) (address - cAddress);
          useTime[s - 1] = frameNo;
          return true;
        }
        // when we get less than chunkSize of data for current chunk
        // return an empty block up to the end of the chunk
        if (cAddress == requestAddress) {
          result.data = null;
          result.offset = (int) (requestAddress + chunkSize - address);
          return false;
        }
      }

      result.data = null;
      if (addr.length > s) {
        result.offset = (int) (addr[s] - address);
      } else {
        result.offset = chunkSize;
      }
      Double key = requestAddress;
      if (!requestMap.contains(key)) {
        requestMap.add(key);
        source.fetch(requestAddress, chunkSize, onData);
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
          useTime = ArrayOp.insertAt(frameNo, useTime, s);
          memory += values.length;
          if (!requestMap.remove(address))
            System.err.println("requestMap.remove(address) failed");
          repaint.run();
        }
      }

      @Override
      public void onError(double address, String e) {
        if (FileHandle.eof(e))
          if (eofAddress < 0) eofAddress = address;
        else if (onError == null)
          System.err.println("BinDataCache: error fetching data at " + address + ": " + e);
        else
          onError.accept(e);
      }
    };
  }
}
