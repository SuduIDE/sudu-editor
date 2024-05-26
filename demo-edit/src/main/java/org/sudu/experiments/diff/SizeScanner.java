package org.sudu.experiments.diff;

import org.sudu.experiments.FileHandle;

abstract class SizeScanner {

  int sl, sr, cnt;

  SizeScanner(FileHandle l, FileHandle r) {
    l.getSize(sizeL -> { sl = sizeL; tryFire(); });
    r.getSize(sizeR -> { sr = sizeR; tryFire(); });
  }

  private void tryFire() {
    if (++cnt == 2)
      onComplete(sl, sr);
  }

  protected abstract void onComplete(int sizeL, int sizeR);
}
