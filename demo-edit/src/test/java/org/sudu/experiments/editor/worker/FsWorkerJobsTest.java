package org.sudu.experiments.editor.worker;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.XorShiftRandom;

import static org.junit.jupiter.api.Assertions.*;

class FsWorkerJobsTest {

  @Test
  void packStats() {
    XorShiftRandom r = new XorShiftRandom();
    for (int i = 0; i < 1000; i++) {
      double size = r.nextInt(1024*1024)
          + 1024.*1024.* r.nextInt(1024*1024);
      FileHandle.Stats stats = new FileHandle.Stats(
          r.nextDouble() < 0.5f,
          r.nextDouble() < 0.5f,
          r.nextDouble() < 0.5f,
          size);

      double[] packStats = FsWorkerJobs.packStats(stats);
      assertEquals(2, packStats.length);
      FileHandle.Stats st = FsWorkerJobs.unpackStats(packStats);
      assertEquals(stats.size, st.size);
      assertEquals(stats.isDirectory, st.isDirectory);
      assertEquals(stats.isFile, st.isFile);
      assertEquals(stats.isSymbolicLink, st.isSymbolicLink);
    }
  }
}
