package org.sudu.experiments;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class Scene0 extends Scene {
  protected final V4f clearColor = Color.Cvt.fromRGB(0,0, 64);
  protected final V2i size = new V2i();
  protected double dpr;

  public Scene0(SceneApi api) {
    this(api, true);
  }

  public Scene0(SceneApi api, boolean setTitle) {
    super(api);
    if (setTitle) api.window.setTitle(getClass().getName());
  }

  public boolean update(double timestamp) {
    return false;
  }

  public void paint() {
    api.graphics.clear(clearColor);
  }

  public void onResize(V2i newSize, double dpr) {
    size.set(newSize);
    this.dpr = dpr;
  }

  public void dispose() {}
}
