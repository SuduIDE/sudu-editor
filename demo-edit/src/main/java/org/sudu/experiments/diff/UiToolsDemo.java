package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.editor.ProjectViewWindow;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.ToolbarItem;

public class UiToolsDemo extends WindowScene implements DprChangeListener {

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  public UiToolsDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    api.input.onContextMenu.add(e -> onContextMenu(e.position));
  }

  static String[] menuFonts() { return Fonts.editorFonts(true); }

  private boolean onContextMenu(V2i position) {
    var actions = ArrayOp.supplier(
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newFolderWindow),
            UiText.newFolderWindow),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newFileWindow),
            UiText.newFileWindow),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newCodeReview),
            UiText.newCodeReviewWindow),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newProjectView),
            UiText.newProjectView),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newEditorWindow),
            UiText.newEditorWindow),
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newBinaryDiff),
            UiText.newBinaryDiffWindow)
    );
    windowManager.showPopup(
        theme.dialogItem, theme.popupMenuFont,
        position, actions);
    return true;
  }

  private void newFolderWindow() {
    new FolderDiffWindow(theme, windowManager, UiToolsDemo::menuFonts);
  }

  private void newFileWindow() {
    new FileDiffWindow(windowManager, theme, UiToolsDemo::menuFonts, EditorConst.DEFAULT_DISABLE_PARSER, false);
  }

  private void newCodeReview() {
    new FileDiffWindow(windowManager, theme, UiToolsDemo::menuFonts, EditorConst.DEFAULT_DISABLE_PARSER, true);
  }

  private void newBinaryDiff() {
    new BinaryDiffWindow(windowManager, theme, UiToolsDemo::menuFonts);
  }

  private void newProjectView() {
    new ProjectViewWindow(windowManager, theme, UiToolsDemo::menuFonts);
  }

  private void newEditorWindow() {
    new EditorWindow(windowManager, theme, UiToolsDemo::menuFonts).focus();
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) {
      var w = windowManager.uiContext.windowSize;
      onContextMenu(new V2i(w.x / 3, w.y / 3));
    }
  }
}
