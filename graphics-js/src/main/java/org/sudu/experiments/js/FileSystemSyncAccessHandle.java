package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBufferView;

public interface FileSystemSyncAccessHandle extends JSObject {
  void close();
  void flush();
  double getSize();

  int read(ArrayBufferView buffer);
  int read(ArrayBufferView buffer, JSObject options);

  void truncate(double newSize);

  int write(ArrayBufferView buffer);
  int write(ArrayBufferView buffer, JSObject options);

  class Js {
    @JSBody(params = "pos", script = "return { at: pos };")
    public static native JSObject options(double pos);
  }
}
