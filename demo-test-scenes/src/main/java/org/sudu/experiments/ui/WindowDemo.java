package org.sudu.experiments.ui;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;

public class WindowDemo extends WindowScene implements DprChangeListener {

  private Window window;

  public WindowDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));
  }

  protected View createContent() { return new View(); }

  protected boolean withTitle() { return true; }

  private void init() {
    window = new Window(uiContext, createContent());
    setWindowTheme(Themes.darculaColorScheme());
    if (withTitle()) {
      window.setTitle(getClass().getSimpleName());
    }
    windowManager.addWindow(window);
    initialWindowLayout(window);
  }

  protected void setWindowTheme(DialogItemColors theme) {
    window.setTheme(theme);
  }

  protected void setWindowTitle(String title) {
    window.setTitle(title);
  }

  protected boolean windowFrameHitTest(V2i position) {
    return window.frameHitTest(position);
  }

  protected void initialWindowLayout(Window window) {
    V2i newSize = uiContext.windowSize;
    int titleHeight = withTitle() ? window.computeTitleHeight() : 0;
    int screenH = newSize.y - titleHeight;
    window.setPosition(
        new V2i(newSize.x / 20, titleHeight + screenH / 20),
        new V2i(newSize.x * 9 / 10, screenH * 9 / 10)
    );
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) init();
  }
}
