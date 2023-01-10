package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;

public interface FileSystemSyncAccessHandle extends JSObject {
  double getSize();
  void close();

  // todo
  void read(ArrayBuffer buffer);
  void write(ArrayBuffer buffer);

  void truncate(double newSize);
}
