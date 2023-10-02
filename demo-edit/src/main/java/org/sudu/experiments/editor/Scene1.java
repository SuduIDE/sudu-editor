package org.sudu.experiments.editor;

import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.UiContext;

public abstract class Scene1 extends Scene {
  public final V4f clearColor = Color.Cvt.fromRGB(0, 0, 64);
  public final UiContext uiContext;

  public Scene1(SceneApi api) {
    super(api);
    uiContext = new UiContext(api);
    api.input.onKeyPress.add(KeyEvent::handleSpecialKey);
    api.input.onKeyPress.add(uiContext::onKeyPress);
  }

  @Override
  public void paint() {
    api.graphics.clear(clearColor);
  }

  @Override
  public void onResize(V2i newSize, float dpr) {
    uiContext.onResize(newSize, dpr);
  }
}
