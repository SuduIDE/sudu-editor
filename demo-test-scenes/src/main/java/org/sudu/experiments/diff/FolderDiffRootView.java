package org.sudu.experiments.diff;

import org.sudu.experiments.editor.MiddleLine;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.ui.FileTreeDiffRef;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.ScrollView;

class FolderDiffRootView extends DiffRootView implements ThemeControl {

  FileTreeView left, right;
  ScrollView leftScrollView, rightScrollView;
  MiddleLine middleLine;
  DiffSync diffSync;

  FolderDiffRootView(UiContext uiContext) {
    left = new FileTreeView(uiContext);
    right = new FileTreeView(uiContext);
    leftScrollView = new ScrollView(left, uiContext);
    rightScrollView = new ScrollView(right, uiContext);
    middleLine = new MiddleLine(uiContext);
    var leftDiffRef = new FileTreeDiffRef(leftScrollView, left);
    var rightDiffRef = new FileTreeDiffRef(rightScrollView, right);
    middleLine.setLeftRight(leftDiffRef, rightDiffRef);
    diffSync = new DiffSync(leftDiffRef, rightDiffRef);
    setViews(leftScrollView, middleLine, rightScrollView);
  }

  public void applyTheme(EditorColorScheme theme) {
    middleLine.setTheme(theme);
    left.setTheme(theme);
    left.applyTheme(leftScrollView);
    right.setTheme(theme);
    right.applyTheme(rightScrollView);
  }

  public void setDiffModel(DiffInfo diffInfo) {
    diffSync.setModel(diffInfo);
    middleLine.setModel(diffInfo);
  }
}
