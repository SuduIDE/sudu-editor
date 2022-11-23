package org.sudu.experiments;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class Scene0 extends Scene {
  final V4f clearColor = Color.Cvt.fromRGB(0,0, 64);
  protected V2i size;

  public Scene0(SceneApi api) {
    super(api);
    api.window.setTitle(getClass().getName());
  }

  public boolean update(double timestamp) {
    return false;
  }

  public void paint() {
    api.graphics.clear(clearColor);
  }

  public void onResize(V2i size) {
    api.window.setTitle(getClass().getName() + ", size: " + size);

    this.size = size;
  }

  public void dispose() {}
}
