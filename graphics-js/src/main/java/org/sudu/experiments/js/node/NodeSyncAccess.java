package org.sudu.experiments.js.node;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.js.JsMemoryAccess;

public class NodeSyncAccess implements FileHandle.SyncAccess {

  NodeFs.Stats stats;
  int handle;

  public NodeSyncAccess(NodeFs.Stats stats, int handle) {
    this.stats = stats;
    this.handle = handle;
  }

  @Override
  public void close() {
    Fs.fs().closeSync(handle);
    handle = -1;
  }

  @Override
  public double getSize() {
    return stats.size();
  }

  @Override
  public double read(byte[] buf, double filePos) {
    return Fs.fs().readSync(handle,
        JsMemoryAccess.uInt8View(buf),
        0, buf.length, filePos);
  }
}
