package org.sudu.experiments.editor.worker.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.fs.FolderDiffHandler;
import org.sudu.experiments.ui.fs.TreeS;

import java.util.Arrays;
import java.util.Comparator;

public class DiffReader implements DirectoryHandle.Reader {

  FolderDiffHandler handler;
  boolean left;
  TreeS[] dirs = new TreeS[1], files = new TreeS[1];
  int dP = 0, fP = 0;

  public DiffReader(FolderDiffHandler handler, boolean left) {
    this.handler = handler;
    this.left = left;
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
    if (left) handler.sendLeft(children);
    else handler.sendRight(children);
  }
}
