package org.sudu.experiments.diff;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.editor.MiddleLine;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.FileTreeDiffRef;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.ViewArray;

class DiffRootView extends ViewArray {

  FileTreeView left, right;
  ScrollView leftScrollView, rightScrollView;
  MiddleLine middleLine;
  DiffSync diffSync;

  DiffRootView(UiContext uiContext) {
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

  public void setTheme(EditorColorScheme theme) {
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

  @Override
  protected void layoutViews() {
    int px = DprUtil.toPx(MiddleLine.middleLineThicknessDp, dpr);
    int x1 = size.x / 2 - px / 2;
    int x2 = size.x - x1;
    V2i chPos = new V2i(pos);
    V2i chSize = new V2i(x1, size.y);
    leftScrollView.setPosition(chPos, chSize, dpr);
    chPos.x = pos.x + x2;
    rightScrollView.setPosition(chPos, chSize, dpr);
    chPos.x = pos.x + x1;
    chSize.x = x2 - x1;
    middleLine.setPosition(chPos, chSize, dpr);
  }
}
