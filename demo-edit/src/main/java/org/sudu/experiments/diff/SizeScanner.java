package org.sudu.experiments.diff;

import org.sudu.experiments.FileHandle;

import java.util.function.Consumer;

public abstract class SizeScanner {

  int sl, sr, cnt;

  public SizeScanner(FileHandle l, FileHandle r) {
    Consumer<String> onError = this::onError;
    l.getSize(sizeL -> { sl = sizeL; tryFire(); }, onError);
    r.getSize(sizeR -> { sr = sizeR; tryFire(); }, onError);
  }

  private void tryFire() {
    if (++cnt == 2)
      onComplete(sl, sr);
  }

  protected abstract void onComplete(int sizeL, int sizeR);
  protected abstract void onError(String error);
}
