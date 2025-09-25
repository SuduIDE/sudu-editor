package org.sudu.experiments.diff;

import org.sudu.experiments.math.XorShiftRandom;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class TestDiffContent implements BinDataCache.DataSource {
  final double fileSize;
  XorShiftRandom rDiff;

  public TestDiffContent(double len, int rng) {
    fileSize = len;
    rDiff = new XorShiftRandom(rng, 1-rng);
  }

  @Override
  public void fetchSize(DoubleConsumer result, Consumer<String> onError) {
    result.accept(fileSize);
  }

  @Override
  public void fetch(double address, int chinkSize, Result handler) {
    int chunkSize = BinaryDiffView.chunkSize;
    int hi = (int) (address / chunkSize) + 7;
    int lo = (int) (address % chunkSize) + 17;
    if (address < fileSize) {
      XorShiftRandom r = new XorShiftRandom(hi, lo);
      int size = (int) Math.min(chinkSize, fileSize - address);
      byte[] data = new byte[size];
      r.fill(data);
      for (int i = 0; i < data.length; i++) {
        if (rDiff.nextFloat() < 1.f / 32.f) {
          data[i] = (byte) rDiff.nextInt(255);
        }
      }
      handler.onData(address, data);
    } else {
      handler.onError(address, Result.eof);
    }
  }
}
