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
  static final int chunkSize = 1024 * 64;

  @Test
  void testGetOrFetch() {
    var data = new TestDataSource();
    var repaint = new TestRepaint();
    var cache = new BinDataCache(data, chunkSize, repaint);
    var result = new BinDataCache.GetResult();

    boolean r1 = cache.getOrFetch(0, result);
    assertFalse(r1);

    assertNull(result.data);
    assertEquals(chunkSize, result.offset);
    assertEquals(1, data.chunks.size());
    assertFalse(repaint.getValueAndClear());

    // perform double fetch
    boolean r2 = cache.getOrFetch(0, result);

    assertFalse(r2);
    assertNull(result.data);
    assertEquals(chunkSize, result.offset);
    assertEquals(1, data.chunks.size());
    assertFalse(repaint.getValueAndClear());

    data.step();
    assertTrue(repaint.getValueAndClear());

    boolean r3 = cache.getOrFetch(0, result);

    assertTrue(r3);
    assertEquals(0, result.offset);
    assertEquals(chunkSize, result.data.length);
    assertEquals(0, data.chunks.size());
    assertFalse(repaint.getValueAndClear());

    int offset = chunkSize / 10;
    boolean r4 = cache.getOrFetch(offset, result);
    assertTrue(r4);
    assertEquals(offset, result.offset);
    assertEquals(chunkSize, result.data.length);
    assertFalse(repaint.getValueAndClear());

    // [chunk0] -> request chunk3
    boolean r5 = cache.getOrFetch(chunkSize * 3, result);
    assertFalse(r5);
    assertNull(result.data);
    assertEquals(chunkSize, result.offset);
    assertFalse(repaint.getValueAndClear());

    data.step();
    assertTrue(repaint.getValueAndClear());
    // [chunk0] ... [chunk3]

    int addr2 = chunkSize * 3 - offset;
    boolean r6 = cache.getOrFetch(addr2, result);
    assertFalse(r6);
    assertNull(result.data);
    assertEquals(offset, result.offset);
    assertEquals(1, data.chunks.size());
    assertEquals(chunkSize * 2, data.chunks.getFirst().address());
    assertFalse(repaint.getValueAndClear());

    data.step();
    assertTrue(repaint.getValueAndClear());

  }
}
