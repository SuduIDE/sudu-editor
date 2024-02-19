package org.sudu.experiments;

import java.nio.file.Path;
import java.util.concurrent.Executor;

class JvmFsHandle implements FsItem {
  final Path path;
  final String[] rPath;
  final Executor bgWorker;
  final Executor edt;

  JvmFsHandle(String path, String[] rPath, Executor bgWorker, Executor edt) {
    this(Path.of(path), rPath, bgWorker, edt);
  }

  JvmFsHandle(Path path, String[] rPath, Executor bgWorker, Executor edt) {
    this.path = path;
    this.rPath = rPath;
    this.bgWorker = bgWorker;
    this.edt = edt;
  }

  public String getName() {
    return path.getFileName().toString();
  }

  public String[] getPath() {
    return rPath;
  }
}
