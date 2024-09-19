package org.sudu.experiments.editor;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.diff.UiText;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.ToolbarItem;

public class EditorWindowDemo extends WindowScene implements DprChangeListener {
  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  public EditorWindowDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));
    api.input.onKeyPress.add(new CtrlO(api, this::openFile));
    api.input.onContextMenu.add(this::onContextMenu);
  }

  private void openFile(FileHandle fileHandle) {
    EditorWindow w = newEditorWindow();
    w.open(fileHandle);
  }

  private boolean onContextMenu(MouseEvent event) {
    var actions = ArrayOp.supplier(
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newEditorWindow),
            UiText.newEditorWindow)
    );
    windowManager.showPopup(
        theme.dialogItem, theme.popupMenuFont,
        event.position, actions);
    return true;
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) newEditorWindow();
  }

  private EditorWindow newEditorWindow() {
    var w = new EditorWindow(windowManager, theme, EditorWindowDemo::menuFonts);
    w.focus();
    return w;
  }

  static String[] menuFonts() { return Fonts.editorFonts(true); }
}
