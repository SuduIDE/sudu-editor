package org.sudu.experiments.diff;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

record DataChunk(
    byte[] data, double address,
    BinDataCache.DataSource.Result handler) {}

class TestDataSource implements BinDataCache.DataSource {

  final Deque<DataChunk> chunks = new LinkedList<>();
  final XorShiftRandom random = new XorShiftRandom();

  @Override
  public void fetch(double address, int length, Result handler) {
    byte[] data = new byte[length];
    random.fill(data);
    chunks.addLast(new DataChunk(data, address,  handler));
  }

  void step() {
    DataChunk first = chunks.pollFirst();
    if (first != null)
      first.handler().onData(first.address(), first.data());
  }
}

class TestRepaint implements Runnable {
  boolean value;

  @Override
  public void run() {
    value = true;
  }

  boolean getValueAndClear() {
    boolean result = value;
    value = false;
    return result;
  }
}

class BinDataCacheTest {

  static final int maxMemory = 1024 * 1024;
  static final int chunkSize = 1024;

  @Test
  void testGetOrFetch() {
    var data = new TestDataSource();
    var cache = new BinDataCache(data);
    var result = new BinDataCache.GetResult();

    boolean r = cache.getOrFetch(0, chunkSize, result);

    assertTrue(r);
    assertEquals(1, data.chunks.size());
  }
}
