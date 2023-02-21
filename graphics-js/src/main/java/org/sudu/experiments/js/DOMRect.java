package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface DOMRect extends JSObject {
  @JSProperty double getLeft();
  @JSProperty double getRight();
  @JSProperty double getTop();
  @JSProperty double getBottom();
  @JSProperty double getWidth();
  @JSProperty double getHeight();
}
