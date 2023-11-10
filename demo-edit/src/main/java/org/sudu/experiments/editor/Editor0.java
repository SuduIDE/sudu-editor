// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;

import java.util.Objects;

public class Editor0 extends Scene1 implements EditorTheme, EditorUi.CleartypeControl {

  final EditorComponent editor;
  final EditorUi ui;

  public Editor0(SceneApi api) {
    super(api);

    ui = new EditorUi(uiContext);
    editor = new EditorComponent(uiContext, ui);
    uiContext.initFocus(editor);

    api.input.onMouse.add(ui);
    api.input.onScroll.add(ui);
    api.input.onMouse.add(editor);

    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onKeyPress.add(new CtrlO(api, editor::openFile));

    api.input.onCopy.add(editor::onCopy);
    api.input.onPaste.add(() -> editor::handleInsert);
    api.input.onScroll.add((e, dX, dY) -> editor.onScroll(dX, dY));
    api.input.onContextMenu.add(this::onContextMenu);

    toggleDark();
  }

  @Override
  public void enableCleartype(boolean en) {
    if (uiContext.enableCleartype(en)) {
      ui.onTextRenderingSettingsChange();
      editor.enableCleartype(en);
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
  }

  @Override
  public boolean update(double timestamp) {
    return editor.update(timestamp);
  }

  @Override
  public void paint() {
    super.paint();
    editor.paint();
    ui.paint();
  }

  protected String[] menuFonts() { return Fonts.editorFonts(false); }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
    layout(newSize, newDpr);
  }

  protected void layout(V2i newSize, float dpr) {
    editor.setPos(new V2i(), newSize, dpr);
  }

  public void applyTheme(EditorColorScheme theme) {
    Objects.requireNonNull(theme);
    ui.setTheme(theme);
    editor.setTheme(theme);
  }

  public void setTheme(String theme) {
    switch (theme) {
      case "light" -> toggleLight();
      case "darcula" -> toggleDarcula();
      case "dark" -> toggleDark();
      default -> Debug.consoleInfo("unknown theme: " + theme);
    }
  }

  boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.F10) {
      api.window.addChild("child", Editor0::new);
      return true;
    }
    return false;
  }

  boolean onContextMenu(MouseEvent event) {
    if (uiContext.isFocused(editor)) {
      ui.showContextMenu(event, editor,
          Editor0.this, editor, this,
          Editor0.this::menuFonts);
    }
    return true;
  }
}
