package org.sudu.experiments.diff;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.CtrlO;
import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Supplier;

public class FileDiffWindow extends ToolWindow0
    implements Focusable
{
  FileDiffRootView rootView;
  Window window;
  String leftFile, rightFile;
  Focusable focusSave;

  public FileDiffWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts
  ) {
    super(wm, theme, fonts);
    rootView = new FileDiffRootView(windowManager);
    rootView.applyTheme(this.theme);
    window = createWindow(rootView, 30);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);

    rootView.editor1.onKey(this);
    rootView.editor2.onKey(this);
    windowManager.uiContext.setFocus(this);
  }

  private void onBlur() {
    var f = windowManager.uiContext.focused();
    focusSave = isMyFocus(f) ? f : null;
  }

  private boolean isMyFocus(Focusable f) {
    return rootView.editor1 == f || rootView.editor2 == f || this == f;
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    window.setTheme(theme.dialogItem);
    rootView.applyTheme(theme);
  }

  public void open(FileHandle f, boolean left) {
    var ed = left ? rootView.editor1 : rootView.editor2;
    ed.openFile(f, () -> updateTitle(f, left));
  }

  public void open(String source, String name, boolean left) {
    var ed = left ? rootView.editor1 : rootView.editor2;
    ed.openFile(source, name, () -> updateTitle(name, left));
  }

  void updateTitle(FileHandle handle, boolean left) {
    updateTitle(handle.getFullPath(), left);
  }

  void updateTitle(String name, boolean left) {
    if (left) leftFile = name; else rightFile = name;
    if (leftFile != null && rightFile != null) {
      window.setTitle(name);
    } else {
      if (leftFile != null) window.setTitle(leftFile);
      if (rightFile != null) window.setTitle(rightFile);
    }
  }

  protected void dispose() {
    if (isMyFocus(windowManager.uiContext.focused())) {
      windowManager.uiContext.setFocus(null);
    }

    window = null;
    rootView = null;
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    var e1 = rootView.editor1;
    var e2 = rootView.editor2;
    boolean h1 = e1.hitTest(pos);
    boolean h2 = e2.hitTest(pos);

    if (h1 || h2) {
      var t = h1 ? UiText.selectLeftText : UiText.selectRightText;
      return edMenu(pos, h1 ? e1 : e2, opener(h1, t));
    }
    return selectLR();
  }

  private Supplier<ToolbarItem[]> edMenu(
      V2i pos,
      EditorComponent editor,
      ToolbarItem opener) {
    return rootView.ui.builder(
        editor, fonts,
        this,
        rootView.fontApi(),
        windowManager::enableCleartype
    ).build(pos, opener);
  }

  private ToolbarItem opener(boolean left, String t) {
    return new ToolbarItem(() -> selectFile(left), t);
  }

  private Supplier<ToolbarItem[]> selectLR() {
    return ArrayOp.supplier(
        opener(true, UiText.selectLeftText),
        opener(false, UiText.selectRightText));
  }

  private void selectFile(boolean left) {
    windowManager.uiContext.window.showOpenFilePicker(
        windowManager.hidePopupMenuThen(
            file -> open(file, left)
        )
    );
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    if (CtrlO.test(event)) {
      var f = windowManager.uiContext.focused();
      if (rootView.editor1 == f || rootView.editor2 == f) {
        selectFile(rootView.editor1 == f);
      }
      return true;
    }
    if (event.keyCode == KeyCode.ESC) {
      if (event.noMods()) window.close();
      else windowManager.nextWindow();
      return true;
    }
    return false;
  }
}
