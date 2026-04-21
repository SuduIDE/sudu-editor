package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface ImageBitmap extends JSObject {
  @JSProperty
  int getWidth();
  @JSProperty
  int getHeight();

  void close();
}
