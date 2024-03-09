package org.sudu.experiments.diff;

import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Supplier;

abstract class DiffWindow0 {
  final WindowManager windowManager;
  EditorColorScheme theme;

  DiffWindow0(WindowManager windowManager, EditorColorScheme theme) {
    this.windowManager = windowManager;
    this.theme = theme;
  }

  Window createWindow(View view) {
    Window window = new Window(windowManager.uiContext, view);
    window.setTheme(theme.dialogItem);
    window.setTitle(getClass().getSimpleName());
    WindowLayouts.largeWindowLayout(window);
    window.setOnClose(() -> destroyWindow(window));
    window.setContextMenu(this::onContextMenu);
    return window;
  }

  abstract void dispose();

  private void destroyWindow(Window window) {
    windowManager.removeWindow(window);
    window.dispose();
    dispose();
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    return null;
  }

  boolean onContextMenu(V2i pos) {
    var actions = popupActions(pos);
    if (actions != null)
      windowManager.showPopup(
          theme.dialogItem, theme.popupMenuFont,
          pos, actions);
    return actions != null;
  }
}
