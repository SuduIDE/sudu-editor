package org.sudu.experiments.diff;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.EditorComponent;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FileDiffWindow extends DiffWindow0 {

  FileDiffRootView rootView;
  Window window;
  String leftFile, rightFile;

  public FileDiffWindow(EditorColorScheme theme, WindowManager wm) {
    super(wm, theme);
    rootView = new FileDiffRootView(windowManager);
    rootView.applyTheme(this.theme);
    window = createWindow(rootView);
    window.onCopy(this::onCopy);
    window.onPaste(this::onPaste);
    windowManager.addWindow(window);
  }

  private Consumer<String> onPaste() {
    EditorComponent ed = rootView.editor1;
    return ed::handleInsert;
  }

  boolean onCopy(Consumer<String> setText, boolean isCut) {
    EditorComponent ed = rootView.editor1;
    return ed.onCopy(setText, isCut);
  }

  public void open(FileHandle f, boolean left) {
    var ed = left ? rootView.editor1 : rootView.editor2;
    ed.openFile(f, () -> updateTitle(f, left));
  }

  void updateTitle(FileHandle handle, boolean left) {
    String name = handle.getFullPath();
    if (left) leftFile = name; else rightFile = name;
    if (leftFile != null && rightFile != null) {
      window.setTitle(name);
    } else {
      if (leftFile != null) window.setTitle(leftFile);
      if (rightFile != null) window.setTitle(rightFile);
    }
  }

  void dispose() {
    rootView = null;
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    if (rootView.editor1.hitTest(pos)) {
      return select(true, UiText.selectLeftText);
    } else if (rootView.editor2.hitTest(pos)) {
      return select(false, UiText.selectRightText);
    }
    return selectLR();
  }

  private Supplier<ToolbarItem[]> select(boolean left, String t) {
    return ArrayOp.supplier(
        new ToolbarItem(() -> selectFile(left), t));
  }

  private Supplier<ToolbarItem[]> selectLR() {
    return ArrayOp.supplier(
        new ToolbarItem(() ->
            selectFile(true), UiText.selectLeftText),
        new ToolbarItem(() ->
            selectFile(false), UiText.selectRightText));
  }

  private void selectFile(boolean left) {
    windowManager.uiContext.window.showOpenFilePicker(
        windowManager.hidePopupMenuThen(
            file -> open(file, left)
        )
    );
  }
}
