package org.sudu.experiments.editor;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.WindowManager;

public abstract class WindowScene extends Scene1 {

  protected final WindowManager windowManager = new WindowManager(uiContext);

  public WindowScene(SceneApi api) {
    this(api, true);
  }

  public WindowScene(SceneApi api, boolean desktopMouse) {
    super(api);
    uiContext.dprListeners.add(windowManager);
    api.input.onMouse.add(windowManager);
    api.input.onScroll.add(windowManager::onScroll);
    if (desktopMouse) {
      api.input.onMouse.add(uiContext.desktopMouse());
    }
  }

  @Override
  public void dispose() {
    windowManager.dispose();
  }

  @Override
  public void paint() {
    super.paint();
    windowManager.draw(api.graphics);
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
    windowManager.onResize(newSize, newDpr);
  }

  @Override
  public boolean update(double timestamp) {
    return windowManager.update(timestamp);
  }
}
