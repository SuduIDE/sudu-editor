package org.sudu.experiments.swimlane;

import org.sudu.experiments.math.XorShiftRandom;

public class SwimlaneData {

  static float[][] create(
      int lines, int lineSize,
      float timeRange,
      double durationFrequency,
      double gapFrequency
  ) {
    float[][] r = new float[lines][lineSize * 2];
    XorShiftRandom random = new XorShiftRandom();
    for (int i = 0; i < lines; i++) {
      float[] line = new float[lineSize * 2];
      r[i] = line;
      double t = 0;
      for (int j = 0; j < lineSize; j++) {
        double dur = random.poissonTime(durationFrequency);
        double gap = random.poissonTime(gapFrequency);
        line[j * 2] = (float) t;
        line[j * 2 + 1] = (float) (t + dur);
        t += dur + gap;
      }
      float scale = (float) (timeRange / t);
      for (int j = 0; j < lineSize * 2; j++) {
        line[j] *= scale;
      }
    }
    return r;
  }
}
