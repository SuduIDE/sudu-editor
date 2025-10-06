package org.sudu.experiments.diff;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Supplier;

public class BinaryDiffWindow extends ToolWindow0
    implements Focusable
{
  BinaryDiffView rootView;
  Window window;
  Focusable focusSave;
  boolean processEsc = true;

  private Runnable onRefresh;

  public BinaryDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts
  ) {
    super(wm, theme, fonts);
    rootView = new BinaryDiffView(wm.uiContext);
    rootView.setTheme(theme);
    var scrollView = new ScrollView(rootView);
    window = createWindow(scrollView, 30);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);

    windowManager.uiContext.setFocus(this);
  }

  private void onBlur() {
    var f = windowManager.uiContext.focused();
    focusSave = isMyFocus(f) ? f : null;
  }

  private boolean isMyFocus(Focusable f) {
    return this == f;
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
  }

  private boolean isMyFocus() {
    return isMyFocus(windowManager.uiContext.focused());
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
    rootView.setTheme(theme);
  }

  public void open(FileHandle f, boolean left) {
    Debug.consoleInfo("opening file " + f.getName());
    var uiContext = windowManager.uiContext;
    var worker = uiContext.window.worker();

    var source = new BinFileDataSource(f, worker);
    rootView.setData(source, left);
  }

  protected void dispose() {
    if (isMyFocus()) {
      windowManager.uiContext.setFocus(null);
    }

    window = null;
    rootView = null;
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    return ArrayOp.supplier(
        opener("open left", true),
        opener("open right", false)
    );
  }

  private ToolbarItem opener(String t, boolean left) {
    return new ToolbarItem(() -> selectFile(left), t);
  }

  private void selectFile(boolean left) {
    windowManager.uiContext.window.showOpenFilePicker(
        windowManager.hidePopupMenuThen(f -> open(f, left))
    );
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
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

  public int firstLine() {
    return rootView.firstLine();
  }

  public void setOnRefresh(Runnable onRefresh) {
    this.onRefresh = onRefresh;
  }

  public boolean canNavigateDown() {
    return false;
  }

  public void navigateDown() {
    rootView.navigateDown();
  }

  public boolean canNavigateUp() {
    return false;
  }

  public void navigateUp() {
    rootView.navigateUp();
  }

  public void refresh() {
    if (onRefresh != null) {
      onRefresh.run();
      window.context.repaint.run();
    }
  }
}
