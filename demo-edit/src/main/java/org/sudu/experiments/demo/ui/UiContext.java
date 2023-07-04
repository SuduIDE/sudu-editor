package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.Subscribers;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.math.V2i;

public class UiContext {

  public final WglGraphics graphics;
  public final V2i windowSize = new V2i();
  public final SetCursor windowCursor;
  public float dpr;

  public final Subscribers<DprChangeListener> dprListeners
      = new Subscribers<>(new DprChangeListener[0]);

  public UiContext(SceneApi api) {
    this.graphics = api.graphics;
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
