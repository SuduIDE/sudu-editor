package org.sudu.experiments.js;

import org.sudu.experiments.FileHandle;
import org.teavm.jso.typedarrays.Int8Array;

import org.sudu.experiments.js.FileSystemSyncAccessHandle.Js;

import java.io.IOException;

public class JsSyncAccess implements FileHandle.SyncAccess {
  FileSystemSyncAccessHandle handle;

  public JsSyncAccess(FileSystemSyncAccessHandle handle) {
    this.handle = handle;
  }

  @Override
  public boolean close() {
    handle.close();
    handle = null;
    return true;
  }

  @Override
  public double getSize() {
    return handle.getSize();
  }

  @Override
  public int read(byte[] buf, double filePos) throws IOException {
    Int8Array buffer = JsMemoryAccess.bufferView(buf);
    try {
      return filePos == 0 ? handle.read(buffer)
          : handle.read(buffer, Js.options(filePos));
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  @Override
  public int write(byte[] buf, double filePos) throws IOException {
    Int8Array buffer = JsMemoryAccess.bufferView(buf);
    try {
      return filePos == 0 ? handle.write(buffer)
          : handle.write(buffer, Js.options(filePos));
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
