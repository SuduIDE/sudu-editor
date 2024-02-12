package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.window.Window;

public class DiffMiddleDemo extends WindowScene implements DprChangeListener {

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  DiffRootView rootView = new DiffRootView(uiContext);
  Window window = new Window(uiContext);

  public DiffMiddleDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    api.input.onContextMenu.add(this::onContextMenu);
    api.input.onKeyPress.add(this::onKeyPress);

    setWindowTheme(window);
    window.setContent(rootView);
    windowManager.addWindow(window);
    rootView.setTheme(theme);
  }

  private boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.SPACE) {
      rootView.setModel(testModel());
      return true;
    }
    return false;
  }

  private DiffInfo testModel() {
    DiffRange[] ranges = new DiffRange[4];
    ranges[0] = new DiffRange(1, 3, 4, 5, 1);
    ranges[1] = new DiffRange(5, 6, 10, 2, 2);
    ranges[2] = new DiffRange(12, 2, 13, 3, 3);
    ranges[3] = new DiffRange(15, 3, 17, 2, 4);
    return new DiffInfo(new LineDiff[0], new LineDiff[0], ranges);
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) {
      layoutWindow(window);
    }
  }

  private boolean onContextMenu(MouseEvent event) {
    return true;
  }

  @SuppressWarnings("unused")
  private void disposeWindow(Window w) {
    if (w != null) {
      windowManager.removeWindow(w);
      w.dispose();
    }
  }

  private void setWindowTheme(Window window) {
    window.setTheme(theme.dialogItem);
    window.setTitle("DiffMiddleLine", theme.windowTitleFont, 2);
  }

  private void layoutWindow(Window window) {
    V2i newSize = uiContext.windowSize;
    int titleHeight = window.computeTitleHeight();
    int screenH = newSize.y - titleHeight;
    window.setPosition(
        new V2i(newSize.x / 20, titleHeight + screenH / 20),
        new V2i(newSize.x * 9 / 10, screenH * 9 / 10)
    );
  }
}
