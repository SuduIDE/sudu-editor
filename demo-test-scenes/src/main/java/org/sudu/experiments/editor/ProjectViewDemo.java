package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.UiText;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.ToolbarItem;

public class ProjectViewDemo extends WindowScene implements DprChangeListener {
  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  public ProjectViewDemo(SceneApi api) {
    super(api, true);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));
    api.input.onContextMenu.add(this::onContextMenu);
  }

  private boolean onContextMenu(MouseEvent event) {
    var actions = ArrayOp.supplier(
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newProjectView),
            UiText.newProjectView)
    );
    windowManager.showPopup(
        theme.dialogItem, theme.popupMenuFont,
        event.position, actions);
    return true;
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) newProjectView();
  }

  private void newProjectView() {
    new ProjectViewWindow(windowManager, theme, ProjectViewDemo::menuFonts);
  }

  static String[] menuFonts() { return Fonts.editorFonts(true); }
}
