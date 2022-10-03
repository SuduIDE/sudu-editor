package org.sudu.experiments;

import org.sudu.experiments.math.V2i;

public interface Window {
  void setCursor(String cursor);
  V2i getClientRect();
  V2i getScreenRect();
  double timeNow();
  double devicePixelRatio();
}
