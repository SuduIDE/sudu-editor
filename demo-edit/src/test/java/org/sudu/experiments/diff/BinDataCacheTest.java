package org.sudu.experiments.diff;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.XorShiftRandom;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import static org.junit.jupiter.api.Assertions.*;

record DataChunk(
    byte[] data, double address, String error,
    BinDataCache.DataSource.Result handler) {}

class TestDataSource implements BinDataCache.DataSource {

  final Deque<DataChunk> chunks = new LinkedList<>();
  final XorShiftRandom random = new XorShiftRandom();
  final int fileSize;

  public TestDataSource(int fileSize) {
    this.fileSize = fileSize;
  }

  @Override
  public void fetchSize(DoubleConsumer result, Consumer<String> onError) {
    result.accept(fileSize);
  }

  @Override
  public void fetch(double address, int chinkSize, Result handler) {
    int size = (int) Math.min(chinkSize, fileSize - address);
    byte[] data = address >= fileSize ? null : new byte[size];
    if (data != null) random.fill(data);
    chunks.addLast(new DataChunk(
        data, address,
        data == null ? Result.eof : null,
        handler));
  }

  boolean fireFetchComplete() {
    DataChunk first = chunks.pollFirst();
    if (first != null) {
      if (first.error() != null)
        first.handler().onError(first.address(), first.error());
      else
        first.handler().onData(first.address(), first.data());
      return true;
    }
    return false;
  }

  void fireFetchAll() {
    while (fireFetchComplete());
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
  static final int fileTail = 12345;
  static final int fileChunks = maxMemory * 2 / chunkSize;
  static final int fileSize = fileChunks * chunkSize + fileTail;

  @Test
  void testGetOrFetch() {
    var data = new TestDataSource(fileSize);
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

    data.fireFetchComplete();
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
    assertEquals(1, data.chunks.size());

    assertEquals(chunkSize, cache.memory());
    data.fireFetchComplete();
    assertTrue(repaint.getValueAndClear());
    assertEquals(chunkSize * 2, cache.memory());

    // [chunk0] ... [chunk3]

    int addr2 = chunkSize * 3 - offset;
    boolean r6 = cache.getOrFetch(addr2, result);
    assertFalse(r6);
    assertNull(result.data);
    assertEquals(offset, result.offset);
    assertEquals(1, data.chunks.size());
    assertEquals(chunkSize * 2, data.chunks.getFirst().address());
    assertFalse(repaint.getValueAndClear());

    data.fireFetchComplete();
    boolean r7 = repaint.getValueAndClear();
    assertTrue(r7);
    assertEquals(chunkSize * 3, cache.memory());

    // read end of file
    // [chunk0] ... [chunk2][chunk3]  [tail]
    int addr3 = fileChunks * chunkSize + fileTail / 2;
    boolean r8 = cache.getOrFetch(addr3, result);
    assertFalse(r8);
    assertEquals(chunkSize, result.offset);

    assertEquals(1, data.chunks.size());
    data.fireFetchComplete();
    assertTrue(repaint.getValueAndClear());

    assertEquals(chunkSize * 3 + fileTail, cache.memory());

    boolean r9 = cache.getOrFetch(addr3, result);
    assertTrue(r9);
    assertEquals(fileTail / 2, result.offset);
    assertEquals(fileTail, result.data.length);

    // request beyond eof but before chunk border
    //    -> get empty space up to chunk border
    int addrEof = fileChunks * chunkSize + fileTail;
    boolean r10 = cache.getOrFetch(addrEof, result);
    assertFalse(r10);
    assertNull(result.data);
    assertEquals(chunkSize - fileTail, result.offset);
    assertEquals(0, data.chunks.size());

    int addrBeyondEof = fileChunks * chunkSize + chunkSize;
    boolean r11 = cache.getOrFetch(addrBeyondEof, result);
    assertFalse(r11);
    assertEquals(chunkSize, result.offset);
    assertEquals(1, data.chunks.size());
    data.fireFetchComplete();
    assertEquals(chunkSize * 3 + fileTail, cache.memory());
    assertEquals(0, data.chunks.size());
  }

  @Test
  void testGetOrFetchFilsWithChunkSize() {
    var data = new TestDataSource(2 * chunkSize);
    var repaint = new TestRepaint();
    var cache = new BinDataCache(data, chunkSize, repaint);
    var result = new BinDataCache.GetResult();

    int offset = chunkSize / 2;
    boolean r1 = cache.getOrFetch(chunkSize + offset, result);
    assertFalse(r1);
    assertNull(result.data);
    assertEquals(chunkSize, result.offset);
    data.fireFetchComplete();

    boolean r2 = cache.getOrFetch(chunkSize, result);
    assertTrue(r2);
    assertEquals(chunkSize, result.data.length);
    assertEquals(0, result.offset);

    boolean r3 = cache.getOrFetch(chunkSize * 2, result);
    assertFalse(r3);
    assertNull(result.data);
    assertEquals(chunkSize, result.offset);
    assertEquals(1, data.chunks.size());

    data.fireFetchComplete();

    boolean r4 = cache.getOrFetch(chunkSize * 2, result);
    assertFalse(r4);
    assertNull(result.data);
    assertEquals(chunkSize, result.offset);
    assertEquals(0, data.chunks.size());
  }

  @Test
  void testMaxMemory() {
    final int numChunks = 5;
    int maxMemory = chunkSize * 2;
    var data = new TestDataSource(numChunks * chunkSize);
    var repaint = new TestRepaint();
    var cache = new BinDataCache(data, chunkSize, repaint);
    var result = new BinDataCache.GetResult();

    XorShiftRandom r = new XorShiftRandom();

    int[] ad = new int[4];
    int[] addr = {0, 1, 2, 3, 4};
    for (int i = 0; i < ad.length; i++) {
      int idx = r.nextInt(addr.length);
      ad[i] = addr[idx] * chunkSize;
      addr = ArrayOp.removeAt(addr, idx);
    }

    cache.getOrFetch(ad[0], result);
    data.fireFetchAll();
    cache.pruneData(maxMemory);
    assertTrue(cache.memory() <=  maxMemory);

    cache.getOrFetch(ad[1], result);
    data.fireFetchAll();
    cache.pruneData(maxMemory);
    assertTrue(cache.memory() <= maxMemory);

    cache.getOrFetch(ad[2], result);
    cache.getOrFetch(ad[3], result);
    data.fireFetchAll();
    cache.pruneData(maxMemory);
    assertTrue(cache.memory() <= maxMemory);

    assertFalse(cache.getOrFetch(ad[0], result));
    assertFalse(cache.getOrFetch(ad[1], result));
    assertEquals(2, data.chunks.size());

    data.fireFetchAll();
    assertEquals(chunkSize * 4, cache.memory());
    cache.pruneData(maxMemory);
    assertTrue(cache.memory() <= maxMemory);

    assertFalse(cache.getOrFetch(ad[2], result));
    assertEquals(1, data.chunks.size());
    data.fireFetchAll();
    cache.pruneData(maxMemory);
    assertTrue(cache.memory() <= maxMemory);
  }

  @Test
  void testMaxManyTimes() {
    for (int i = 0; i < 120; i++) {
      testMaxMemory();
    }
  }
}
