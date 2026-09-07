package org.sudu.experiments.swimlane;

import org.sudu.experiments.math.XorShiftRandom;

public class SwimlaneData {

  static float[][] create(
      int lines, int lineSizeMin, int lineSizeMax,
      float timeRange,
      double durationFrequency,
      double gapFrequency
  ) {
    float[][] r = new float[lines][];

    XorShiftRandom random = new XorShiftRandom();
    float minScale = 0;
    for (int i = 0; i < lines; i++) {
      // Test hook: line 3 gets a single isolated event so we can inspect how one event renders.
      boolean single = i == 3;
      int events = single ? 1 : lineSizeMin + random.nextInt(lineSizeMax - lineSizeMin + 1);
      float[] line = new float[events * 2];
      r[i] = line;
      double t = 0;
      for (int j = 0; j < events; j++) {
        double dur = random.poissonTime(durationFrequency);
        double gap = random.poissonTime(gapFrequency);
        line[j * 2] = (float) t;
        line[j * 2 + 1] = (float) (t + dur);
        t += dur + gap;
      }
      float scale = (float) (timeRange / t);
      minScale = i == 0 ? scale : Math.min(scale, minScale);
    }
    for (int i = 0; i < lines; i++) {
      var line = r[i];
      int events = line.length / 2;
      for (int j = 0; j < events * 2; j++) {
        line[j] *= minScale;
      }
    }
    return r;
  }
}
