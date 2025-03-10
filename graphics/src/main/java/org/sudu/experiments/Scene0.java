package org.sudu.experiments;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class Scene0 extends Scene {
  public final V4f clearColor = Color.Cvt.fromRGB(0, 0, 64);
  public final V2i screen = new V2i();
  protected float dpr;

  public Scene0(SceneApi api) {
    this(api, true);
  }

  public Scene0(SceneApi api, boolean setTitle) {
    super(api);
    if (setTitle) api.window.setTitle(getClass().getName());
  }

  public void paint() {
    api.graphics.clear(clearColor);
  }

  public void onResize(V2i newSize, float dpr) {
    screen.set(newSize);
    this.dpr = dpr;
  }

  public void dispose() {}
}
