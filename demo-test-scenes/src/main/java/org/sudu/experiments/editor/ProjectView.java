package org.sudu.experiments.editor;

import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.ViewArray;
import org.sudu.experiments.ui.window.ViewFill;
import org.sudu.experiments.ui.window.WindowManager;

public class ProjectView extends ViewArray implements
    EditorUi.CleartypeControl
{

  String[] fonts;
  ThemeControl themeControl;
  EditorUi ui;
  ViewFill split;
  ScrollView treeScrollView;
  FileTreeView treeView;
  EditorComponent editor;

  public ProjectView(WindowManager wm, ThemeControl tc, boolean jbFonts) {
    ui = new EditorUi(wm);
    treeView = new FileTreeView(wm.uiContext);
    treeScrollView = new ScrollView(treeView, wm.uiContext);
    split = new ViewFill();
    editor = new EditorComponent(wm.uiContext, ui);
    themeControl = tc;
    setViews(treeScrollView, split, editor);
//    ui.windowManager.uiContext.initFocus(editor);
    fonts = Fonts.editorFonts(jbFonts);
  }

  @Override
  protected void layoutViews() {
    int x1 = size.x / 4;
    int x2 = x1 + toPx(1);
    V2i chPos = new V2i(pos);
    V2i chSize = new V2i(x1, size.y);
    views[0].setPosition(chPos, chSize, dpr);
    chPos.x = pos.x + x1;
    chSize.x = x2 - x1;
    views[1].setPosition(chPos, chSize, dpr);
    chPos.x = pos.x + x2;
    chSize.x = size.x - x2;
    views[2].setPosition(chPos, chSize, dpr);
  }

  public void applyTheme(EditorColorScheme theme) {
    treeView.setTheme(theme);
    treeView.applyTheme(treeScrollView);
    ui.setTheme(theme);
    editor.setTheme(theme);
  }

  @Override
  public void enableCleartype(boolean en) {
    ui.windowManager.enableCleartype(en);
  }

  public void showContextMenu(V2i position) {
    if (editor.hitTest(position)) {
      ui.showEditorMenu(
          position, editor, themeControl,
          editor,
          ui.windowManager::enableCleartype,
          () -> fonts);
    }
  }

  @Override
  public V2i minimalSize() {
    V2i editorMin = editor.minimalSize();
    V2i scrollMin = treeScrollView.minimalSize();
    return new V2i(editorMin.x + split.size.x + scrollMin.x, editorMin.y);
  }

  public void setEditorFocus() {
    ui.windowManager.uiContext.setFocus(editor);
  }
}
