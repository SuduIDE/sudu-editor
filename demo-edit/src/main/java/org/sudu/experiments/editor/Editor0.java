// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.editor;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;

import java.util.Objects;

public class Editor0 extends WindowScene implements ThemeControl, EditorUi.CleartypeControl {

  final EditorComponent editor;
  final EditorUi ui;

  public Editor0(SceneApi api) {
    super(api);
    windowManager.setDesktopMousePointer(false);
    ui = new EditorUi(windowManager);
    editor = new EditorComponent(ui);
    uiContext.initFocus(editor);

    api.input.onKeyPress.add(new CtrlO(api, this::openFile));

    editor.registerMouse(api.input);
    api.input.onContextMenu.add(this::onContextMenu);

    toggleDark();
  }

  void openFile(FileHandle f) {
    editor.openFile(f, () -> api.window.setTitle(f.getFullPath()));
  }

  @Override
  public void enableCleartype(boolean en) {
    if (windowManager.enableCleartype(en)) {
      editor.onTextRenderingSettingsChange();
    }
  }

  public Document document() {
    return editor.model.document;
  }

  public EditorComponent editor() {
    return editor;
  }

  @Override
  public void dispose() {
    ui.dispose();
    editor.dispose();
    super.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    boolean wmUpdate = super.update(timestamp);
    return editor.update(timestamp) | wmUpdate;
  }

  @Override
  public void paint() {
    clear();
    editor.paint();
    windowManager.draw();
  }

  protected String[] menuFonts() { return Fonts.editorFonts(false); }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
    editor.setPosition(editor.pos, newSize, newDpr);
  }

  public void applyTheme(EditorColorScheme theme) {
    Objects.requireNonNull(theme);
    ui.setTheme(theme);
    editor.setTheme(theme);
  }

  boolean onContextMenu(MouseEvent event) {
    if (uiContext.isFocused(editor)) {
      ui.showEditorMenu(event.position, editor,
          this, editor, this,
          this::menuFonts);
    }
    return true;
  }
}
