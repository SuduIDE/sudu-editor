package org.sudu.experiments.js.node;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.js.JsMemoryAccess;
import org.teavm.jso.core.JSString;

public class NodeSyncAccess implements FileHandle.SyncAccess {

  NodeFs.Stats stats;
  int handle;
  JSString path;

  public NodeSyncAccess(NodeFs.Stats stats, int handle, JSString path) {
    this.stats = stats;
    this.handle = handle;
    this.path = path;
  }

  @Override
  public boolean close() {
    int r = Fs.fs().closeSync(handle);
    handle = -1;
    return r != -1;
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
