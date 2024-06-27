package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBufferView;

public interface FileSystemSyncAccessHandle extends JSObject {
  void close();
  void flush();
  double getSize();

  double read(ArrayBufferView buffer);
  double read(ArrayBufferView buffer, JSObject options);

  void truncate(double newSize);

  double write(ArrayBufferView buffer);
  double write(ArrayBufferView buffer, JSObject options);

  class Js {
    @JSBody(params = "pos", script = "return { at: pos };")
    public static native  JSObject options(double pos);
  }
}
