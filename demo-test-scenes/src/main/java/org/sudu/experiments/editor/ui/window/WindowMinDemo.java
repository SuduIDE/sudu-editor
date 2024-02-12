package org.sudu.experiments.editor.ui.window;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.UiFont;
import org.sudu.experiments.ui.window.Window;

public class WindowMinDemo extends WindowScene implements DprChangeListener {

  static final boolean withTitle = true;
  static final int titleMargin = 3;

  Window window;

  public WindowMinDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    window = new Window(uiContext);
    window.setTheme(Themes.darculaColorScheme());
    if (withTitle) {
      window.setTitle("WindowMinDemo",
          new UiFont("Consolas", 15), titleMargin);
    }
    windowManager.addWindow(window);
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) layoutWindows();
  }

  private void layoutWindows() {
    V2i newSize = uiContext.windowSize;
    window.setPosition(
        new V2i(newSize.x / 10, newSize.y / 10),
        new V2i(newSize.x * 6 / 10, newSize.y * 6 / 10)
    );
  }
}
