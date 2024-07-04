package org.sudu.experiments;

import java.nio.file.Path;
import java.util.concurrent.Executor;

public abstract class JvmFsHandle implements FsItem {
  final Path path, root;
  final Executor bgWorkerHi;
  final Executor edt;
  private String[] rPath;

  JvmFsHandle(String path, Path root, Executor bgWorkerHi, Executor edt) {
    this(Path.of(path), root, bgWorkerHi, edt);
  }

  JvmFsHandle(Path path, Path root, Executor bgWorkerHi, Executor edt) {
    this.path = path;
    this.root = root;
    this.bgWorkerHi = bgWorkerHi;
    this.edt = edt;
  }

  // Returns a handle to same fs reference,
  // but with specified event thread
  // If edt argument is null then the background worker's bus is used
  public final JvmFsHandle withEdt(Executor edt) {
    return this.edt == edt ? this :
        ctor(edt != null ? edt : bgWorkerHi);
  }

  protected final boolean isOnWorker() {
    return edt == bgWorkerHi;
  }

  protected abstract JvmFsHandle ctor(Executor edt);

  public String getName() {
    return path.getFileName().toString();
  }

  public String[] getPath() {
    if (rPath == null)
      rPath = toRPath(path, root);
    return rPath;
  }

  @Override
  public String toString() {
    return FsItem.toString(getClass().getSimpleName(),
        getPath(), getName(), isOnWorker());
  }

  static String[] toRPath(Path file, Path start) {
    if (file == start) return s0;
    Path relativize = start.relativize(file);
    int nameCount = relativize.getNameCount();
    if (nameCount < 2) return s0;
    String[] rPath = new String[nameCount - 1];
    for (int i = 0; i < rPath.length; i++)
      rPath[i] = relativize.getName(i).toString();
    return rPath;
  }

  static final String[] s0 = new String[0];
}
