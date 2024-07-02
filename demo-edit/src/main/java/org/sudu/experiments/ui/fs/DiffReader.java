package org.sudu.experiments.ui.fs;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

public class DiffReader implements DirectoryHandle.Reader {

  TreeS[] dirs = new TreeS[1], files = new TreeS[1];
  int dP = 0, fP = 0;
  final Consumer<TreeS[]> onComplete;

  public DiffReader(Consumer<TreeS[]> onComplete) {
    this.onComplete = onComplete;
  }

  @Override
  public void onDirectory(DirectoryHandle dir) {
    var d = new TreeS(dir, true);
    dirs = ArrayOp.addAt(d, dirs, dP++);
  }

  @Override
  public void onFile(FileHandle file) {
    var f = new TreeS(file, false);
    files = ArrayOp.addAt(f, files, fP++);
  }

  @Override
  public void onComplete() {
    Arrays.sort(dirs, 0, dP, Comparator.comparing(a -> a.name));
    Arrays.sort(files, 0, fP, Comparator.comparing(a -> a.name));
    TreeS[] children = ArrayOp.add(dirs, dP, files, fP, new TreeS[fP + dP]);
    onComplete.accept(children);
  }
}
