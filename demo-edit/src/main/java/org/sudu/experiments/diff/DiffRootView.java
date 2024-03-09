package org.sudu.experiments.diff;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.MiddleLine;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.ViewArray;

abstract class DiffRootView extends ViewArray {
  final MiddleLine middleLine;

  DiffRootView(UiContext uiContext) {
    middleLine = new MiddleLine(uiContext);
  }

  @Override
  protected void layoutViews() {
    int px = DprUtil.toPx(MiddleLine.middleLineThicknessDp, dpr);
    int edSize = (size.x - px) / 2;
    V2i chPos = new V2i(pos);
    V2i chSize = new V2i(edSize, size.y);
    // edLeft
    views[0].setPosition(chPos, chSize, dpr);
    chPos.x = pos.x + size.x - edSize;
    // edRight
    views[1].setPosition(chPos, chSize, dpr);
    chSize.x = size.x - edSize - edSize;
    chPos.x = pos.x + edSize;
    // middle
    views[2].setPosition(chPos, chSize, dpr);
  }
}
