package org.sudu.experiments.diff;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.editor.MiddleLine;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.ViewArray;

public abstract class DiffRootView extends ViewArray {

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
