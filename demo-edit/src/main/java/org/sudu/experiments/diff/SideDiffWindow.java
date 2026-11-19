package org.sudu.experiments.diff;

import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Supplier;

public class SideDiffWindow extends ToolWindow0 implements Focusable {
  SideDiffRootView rootView;
  Window window;
  Focusable focusSave;
  boolean processEsc = true;

  public SideDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts,
      boolean disableParser,
      boolean isCodeReview,
      boolean left
  ) {
    super(wm, theme, fonts);
    rootView = new SideDiffRootView(windowManager, disableParser, isCodeReview, left);
    rootView.applyTheme(this.theme);
    window = createWindow(rootView, 30);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);

    rootView.editor.onKey(this);
    windowManager.uiContext.setFocus(this);
  }

  @Override
  protected String defaultTitle() {
    return rootView.left ? "Side diff (left)" : "Side diff (right)";
  }

  private boolean isMyFocus(Focusable f) {
    return rootView.editor == f || this == f;
  }

  private boolean isMyFocus() {
    return isMyFocus(windowManager.uiContext.focused());
  }

  private void onBlur() {
    var f = windowManager.uiContext.focused();
    focusSave = isMyFocus(f) ? f : null;
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
    rootView.applyTheme(theme);
  }

  @Override
  protected void dispose() {
    if (isMyFocus()) {
      windowManager.uiContext.setFocus(null);
    }
    window = null;
    rootView = null;
  }

  @Override
  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    var focus = focused();
    if (focus == null) return null;
    return rootView.ui.builder(
        focus, fonts,
        this,
        rootView.fontApi(),
        windowManager::enableCleartype
    ).build(pos, null);
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    if (event.ctrl && event.keyCode == KeyCode.Z) {
      rootView.undoLastDiff(event.shift);
      return true;
    }
    if (processEsc && event.keyCode == KeyCode.ESC) {
      if (event.noMods()) window.close();
      else windowManager.nextWindow();
      return true;
    }
    if (event.keyCode == KeyCode.F7 && event.singlePress()) {
      if (event.shift) {
        navigateUp();
      } else {
        navigateDown();
      }
    }
    return false;
  }

  public boolean canNavigateDown() {
    return rootView.canNavigateDown(focused());
  }

  public void navigateDown() {
    rootView.navigateDown(focused());
  }

  public boolean canNavigateUp() {
    return rootView.canNavigateUp(focused());
  }

  public void navigateUp() {
    rootView.navigateUp(focused());
  }

  EditorComponent focused() {
    return rootView.editor;
  }
}
