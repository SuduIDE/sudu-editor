package org.sudu.experiments.demo.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.math.V2i;

public class UiContext {

  public final WglGraphics graphics;
  public final Window window;
  public final V2i windowSize = new V2i();
  public final SetCursor windowCursor;
  public float dpr;

  public final Subscribers<DprChangeListener> dprListeners
      = new Subscribers<>(new DprChangeListener[0]);

  public UiContext(SceneApi api) {
    this.graphics = api.graphics;
    this.window = api.window;
    windowCursor = SetCursor.wrap(api.window);
  }

  public void onResize(V2i newSize, float newDpr) {
    windowSize.set(newSize);
    if (dpr != newDpr) {
      float oldDpr = dpr;
      dpr = newDpr;
      for (DprChangeListener listener : dprListeners.array()) {
        if (listener != null) listener.onDprChanged(oldDpr, newDpr);
      }
    }
  }

  public Canvas mCanvas() { return graphics.mCanvas; }

  public void requireWindowVisible() {
    if (windowSize.x * windowSize.y == 0 || dpr == 0) {
      throw new IllegalStateException(
          "trying to display with unknown screen size and dpr");
    }
  }

}
