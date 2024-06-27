package org.sudu.experiments.js;

import org.sudu.experiments.FileHandle;
import org.teavm.jso.typedarrays.Int8Array;

import org.sudu.experiments.js.FileSystemSyncAccessHandle.Js;

public class FileSyncAccess implements FileHandle.SyncAccess {
  final FileSystemSyncAccessHandle handle;

  public FileSyncAccess(FileSystemSyncAccessHandle handle) {
    this.handle = handle;
  }

  @Override
  public void close() {
    handle.close();
  }

  @Override
  public double getSize() {
    return handle.getSize();
  }

  @Override
  public double read(byte[] buf, double filePos) {
    Int8Array buffer = JsMemoryAccess.bufferView(buf);
    return filePos == 0 ? handle.read(buffer)
        : handle.read(buffer, Js.options(filePos));
  }
}
