package org.sudu.experiments.diff;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.editor.MiddleLine;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.ViewArray;

class DiffRootView extends ViewArray {

  MiddleLine middleLine;
  DiffSidePane left, right;
  DiffSync diffSync;

  DiffRootView(UiContext uiContext) {
    this(
        new DiffSidePane(uiContext),
        new MiddleLine(uiContext),
        new DiffSidePane(uiContext));
  }

  DiffRootView(DiffSidePane left, MiddleLine middleView, DiffSidePane right) {
    super(left, middleView, right);
    this.middleLine = middleView;
    this.left = left;
    this.right = right;
    middleView.setLeftRight(left.diffRef, right.diffRef);
    diffSync = new DiffSync(left.diffRef, right.diffRef);
  }

  public void setTheme(EditorColorScheme theme) {
    middleLine.setTheme(theme);
    left.setTheme(theme);
    right.setTheme(theme);
  }

  public void setModel(DiffInfo diffInfo) {
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
    views[0].setPosition(chPos, chSize, dpr);
    chPos.x = pos.x + x2;
    views[2].setPosition(chPos, chSize, dpr);
    chPos.x = pos.x + x1;
    chSize.x = x2 - x1;
    views[1].setPosition(chPos, chSize, dpr);
  }
}
