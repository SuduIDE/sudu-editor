package org.sudu.experiments.editor;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.WindowDemo;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;

import java.util.Objects;

public class EditorInViewDemo extends WindowDemo implements
    DprChangeListener,
    ThemeControl,
    EditorUi.CleartypeControl
{
  EditorUi ui;
  EditorComponent editor;

  public EditorInViewDemo(SceneApi api) {
    super(api);
    clearColor.set(new Color(43));
    ui = new EditorUi(windowManager);

    api.input.onKeyPress.add(new CtrlO(api, this::openFile));
    api.input.onContextMenu.add(this::onContextMenu);
  }

  private void openFile(FileHandle f) {
    if (editor != null) {
      editor.openFile(f,
          () -> api.window.setTitle(f.getFullPath())
      );
    }
  }

  @Override
  public void dispose() {
    ui.dispose();
    super.dispose();
  }

  @Override
  protected View createContent() {
    editor = new EditorComponent(ui);
    StartFile.apply(editor);
    uiContext.initFocus(editor);
    return editor;
  }

  @Override
  protected boolean withTitle() {
    return false;
  }

  @Override
  protected void initialWindowLayout(Window window) {
    super.initialWindowLayout(window);
    toggleDark();
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    Objects.requireNonNull(theme);
    ui.setTheme(theme);
    editor.setTheme(theme);
    setWindowTheme(theme.dialogItem);
  }

  @Override
  public void enableCleartype(boolean en) {
    windowManager.enableCleartype(en);
  }

  boolean onContextMenu(MouseEvent event) {
    if (uiContext.isFocused(editor)) {
      ui.showEditorMenu(event.position, editor,
          this, editor, this,
          () -> Fonts.editorFonts(true));
    }
    return true;
  }

}
