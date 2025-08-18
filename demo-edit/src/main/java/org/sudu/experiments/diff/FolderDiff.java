package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.editor.ProjectViewWindow;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.ToolbarItem;

public class FolderDiff extends WindowScene implements DprChangeListener {

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  public FolderDiff(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    api.input.onContextMenu.add(this::onContextMenu);
  }

  static String[] menuFonts() { return Fonts.editorFonts(true); }

  private boolean onContextMenu(MouseEvent event) {
    var actions = ArrayOp.supplier(
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newFolderWindow),
            UiText.newFolderWindow),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newFileWindow),
            UiText.newFileWindow),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newProjectView),
            UiText.newProjectView),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newEditorWindow),
            UiText.newEditorWindow)
    );
    windowManager.showPopup(
        theme.dialogItem, theme.popupMenuFont,
        event.position, actions);
    return true;
  }

  private void newFolderWindow() {
    new FolderDiffWindow(theme, windowManager, FolderDiff::menuFonts);
  }

  private void newFileWindow() {
    new FileDiffWindow(windowManager, theme, FolderDiff::menuFonts, EditorConst.DEFAULT_DISABLE_PARSER, false);
  }

  private void newProjectView() {
    new ProjectViewWindow(windowManager, theme, FolderDiff::menuFonts);
  }

  private void newEditorWindow() {
    new EditorWindow(windowManager, theme, FolderDiff::menuFonts).focus();
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) newFolderWindow();
  }
}
