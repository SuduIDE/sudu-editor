package org.sudu.experiments.js;

import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.Event;

public interface PointerEvent extends Event {

  @JSProperty
  int getPointerId();

}
