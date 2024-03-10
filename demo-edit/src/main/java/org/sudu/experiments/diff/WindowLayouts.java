package org.sudu.experiments.diff;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.Window;

public class WindowLayouts {

  public static void largeWindowLayout(Window window, float dpOff) {
    V2i newSize = window.context.windowSize;
    int titleHeight = window.computeTitleHeight();
    int px5dp = window.context.toPx(5);
    int pxOff = window.context.toPx(dpOff);
    int dY = pxOff == 0 ? 0  : pxOff + titleHeight;
    window.setPosition(
        new V2i(px5dp + pxOff, px5dp + dY + titleHeight),
        new V2i(newSize.x - px5dp * 2 - pxOff,
            newSize.y - titleHeight - px5dp * 2 - dY)
    );
  }
}
