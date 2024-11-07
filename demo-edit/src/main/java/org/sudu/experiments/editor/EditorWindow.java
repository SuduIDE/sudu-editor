package org.sudu.experiments.editor;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EditorWindow extends ToolWindow0 implements InputListeners.KeyHandler {
  Window window;
  EditorUi ui;
  EditorComponent editor;
  Focusable focusSave;
  Consumer<EditorWindow> onControllerEvent;
  Consumer<Model> onDiffMade;

  public EditorWindow(
      WindowManager wm,
      EditorColorScheme theme,
      Supplier<String[]> fonts
  ) {
    super(wm, theme, fonts);
    ui = new EditorUi(wm);
    editor = new EditorComponent(ui);
    editor.setIterativeParseFileListener((_1, _2, _3) -> onDiffMade());
    window = createWindow(editor, 25);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    editor.onKey(this);
    applyTheme(theme);
    windowManager.addWindow(window);
  }

  public void onControllerEvent(Consumer<EditorWindow> handler) {
    onControllerEvent = handler;
  }

  void fireControllerEvent() {
    if (onControllerEvent != null)
      onControllerEvent.accept(this);
  }

  private boolean isMyFocus() {
    return editor == focused();
  }

  public void focus() {
    System.out.println("EditorWindow.focus");
    windowManager.uiContext.setFocus(editor);
  }

  private void onBlur() {
    focusSave = isMyFocus() ? editor : null;
  }

  private Focusable focused() {
    return windowManager.uiContext.focused();
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
    fireControllerEvent();
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
    ui.setTheme(theme);
    editor.setTheme(theme);
  }

  public void open(FileHandle f) {
    editor.context.setFocus(editor);
    editor.openFile(f, () -> updateTitle(f));
  }

  public void open(String source, String encoding, String name) {
    editor.context.setFocus(editor);
    editor.openFile(source, name, encoding);
    updateTitle(name);
    System.out.println("open file: name = " + name + ", isMyFocus = " + isMyFocus());
    if (isMyFocus())
      fireControllerEvent();
  }

  void updateTitle(FileHandle handle) {
    updateTitle(handle.getFullPath());
  }

  void updateTitle(String name) {
    window.setTitle(name);
  }

  protected void dispose() {
    if (focused() == editor)
      windowManager.uiContext.setFocus(null);
    window = null;
    editor = null;
    ui = null;
    focusSave = null;
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    ToolbarItem opener = new ToolbarItem(this::selectFile, "Open ...");
    return ui.builder(editor, fonts,
        this, editor,
        windowManager::enableCleartype
    ).build(pos, opener);
  }

  public void selectFile() {
    windowManager.uiContext.window.showOpenFilePicker(
        windowManager.hidePopupMenuThen(this::open)
    );
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    if (CtrlO.test(event)) {
      selectFile();
      return true;
    }
    if (event.keyCode == KeyCode.ESC) {
      if (event.noMods()) window.close();
      else windowManager.nextWindow();
      return true;
    }
    return false;
  }

  public void maximize() {
    window.maximize();
  }

  public void setDiffMade(Consumer<Model> onDiffMade) {
    this.onDiffMade = onDiffMade;
  }

  public void setReadonly(boolean readonly) {
    editor.readonly = readonly;
  }

  void onDiffMade() {
    if (onDiffMade != null)
      onDiffMade.accept(editor.model);
  }
}
