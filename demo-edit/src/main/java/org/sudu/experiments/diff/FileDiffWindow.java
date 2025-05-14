package org.sudu.experiments.diff;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.CtrlO;
import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.Model;
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

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FileDiffWindow extends ToolWindow0
    implements Focusable
{
  FileDiffRootView rootView;
  Window window;
  String leftFile, rightFile;
  Focusable focusSave;
  Consumer<FileDiffWindow> onEvent;
  boolean processEsc = true;
  boolean canSelectFiles = true;

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

  void fireEvent() {
    if (onEvent != null)
      onEvent.accept(this);
  }

  private boolean isMyFocus(Focusable f) {
    return rootView.editor1 == f || rootView.editor2 == f || this == f;
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
    fireIfModelReady();
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
    rootView.applyTheme(theme);
  }

  public void open(FileHandle f, boolean left) {
    Debug.consoleInfo("opening file " + f.getName());
    FileHandle.readTextFile(f,
        (source, encoding) ->
            open(source, encoding, f.getFullPath(), left),
        System.err::println
    );
  }

  public void open(String source, String encoding, String name, boolean left) {
    var ed = left ? rootView.editor1 : rootView.editor2;
    ed.openFile(source, name, encoding);
    updateTitle(name, left);
    if (isMyFocus())
      fireIfModelReady();
  }

  private void fireIfModelReady() {
    if (leftFile != null && rightFile != null) {
      rootView.sendToDiff(true);
      fireEvent();
    }
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
    if (isMyFocus()) {
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
      var opener = canSelectFiles ? opener(h1,
          h1 ? UiText.selectLeftText : UiText.selectRightText) : null;
      return edMenu(pos, h1 ? e1 : e2, opener);
    }
    return canSelectFiles ? selectLR() : null;
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
    if (canSelectFiles && CtrlO.test(event)) {
      var f = windowManager.uiContext.focused();
      if (rootView.editor1 == f || rootView.editor2 == f) {
        selectFile(rootView.editor1 == f);
      }
      return true;
    }
    if (processEsc && event.keyCode == KeyCode.ESC) {
      if (event.noMods()) window.close();
      else windowManager.nextWindow();
      return true;
    }
    if (event.keyCode == KeyCode.F5 && event.isPressed && !event.isRepeated) {
      if () {
        rootView.
      }
      rootView.setCompactView();
    }
    return false;
  }

  public void setOnDiffMade(
      Consumer<Model> onLeftDiffMade, Consumer<Model> onRightDiffMade
  ) {
    rootView.setOnDiffMade(onLeftDiffMade, onRightDiffMade);
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
    var f = windowManager.uiContext.focused();
    if (f == rootView.editor1)
      return rootView.editor1;
    if (f == rootView.editor2)
      return rootView.editor2;
    return focusSave == rootView.editor2
        ? rootView.editor2 : rootView.editor1;
  }
}
