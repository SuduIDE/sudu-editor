package org.sudu.experiments.editor;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EditorWindow extends ToolWindow0 {
  Window window;
  EditorUi ui;
  EditorComponent editor;
  Focusable focusSave;
  Consumer<EditorWindow> onDestroy;

  public EditorWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts
  ) {
    super(wm, theme, fonts);
    ui = new EditorUi(wm);
    editor = new EditorComponent(ui);
    window = createWindow(editor, 25);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    applyTheme(theme);
    windowManager.addWindow(window);
  }

  public void focus() {
    windowManager.uiContext.setFocus(editor);
  }

  private void onBlur() {
    var f = windowManager.uiContext.focused();
    focusSave = (editor == f) ? editor : null;
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    window.setTheme(theme.dialogItem);
    ui.setTheme(theme);
    editor.setTheme(theme);
  }

  public void open(FileHandle f) {
    editor.context.setFocus(editor);
    editor.openFile(f, () -> updateTitle(f));
  }

  void updateTitle(FileHandle handle) {
    String name = handle.getFullPath();
    window.setTitle(name);
  }

  protected void dispose() {
    if (onDestroy != null)
      onDestroy.accept(this);
    window = null;
    editor = null;
    ui = null;
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    ToolbarItem opener = new ToolbarItem(this::selectFile, "Open ...");
    return ui.builder(editor, fonts,
        this, editor,
        windowManager::enableCleartype
    ).build(pos, opener);
  }

  private void selectFile() {
    windowManager.uiContext.window.showOpenFilePicker(
        windowManager.hidePopupMenuThen(this::open)
    );
  }
}
