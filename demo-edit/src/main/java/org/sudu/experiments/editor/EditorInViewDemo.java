package org.sudu.experiments.editor;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.window.Window;

import java.util.Objects;

public class EditorInViewDemo extends WindowScene implements
    DprChangeListener,
    EditorTheme,
    EditorUi.CleartypeControl
{
  EditorUi ui;
  EditorComponent editor;
  Window window;
  Disposable inputRegistrations;

  public EditorInViewDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    ui = new EditorUi(windowManager);
    editor = new EditorComponent(uiContext, ui);
    StartFile.apply(editor);
    uiContext.initFocus(editor);

    inputRegistrations = editor.registerInput(api.input, true);

    api.input.onKeyPress.add(new CtrlO(api, editor::openFile));
    api.input.onContextMenu.add(this::onContextMenu);

    window = new Window(uiContext, editor);
    windowManager.addWindow(window);
    toggleDark();
  }

  @Override
  public void dispose() {
    Disposable.assign(inputRegistrations, null);
    ui.dispose();
    editor.dispose();
    super.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    return editor.update(timestamp);
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) layoutWindows();
  }

  private void layoutWindows() {
    V2i newSize = uiContext.windowSize;
    window.setPosition(
        new V2i(newSize.x / 20, newSize.y / 20),
        new V2i(newSize.x * 9 / 10, newSize.y * 9 / 10)
    );
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    Objects.requireNonNull(theme);
    ui.setTheme(theme);
    editor.setTheme(theme);
    window.setTheme(theme.dialogItem);
  }

  @Override
  public void enableCleartype(boolean en) {
    if (uiContext.enableCleartype(en)) {
      windowManager.onTextRenderingSettingsChange();
      editor.onTextRenderingSettingsChange();
    }
  }

  boolean onContextMenu(MouseEvent event) {
    if (uiContext.isFocused(editor)) {
      ui.showContextMenu(event.position, editor,
          this, editor, this,
          () -> Fonts.editorFonts(true));
    }
    return true;
  }

}
