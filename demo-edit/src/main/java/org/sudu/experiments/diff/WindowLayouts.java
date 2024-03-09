package org.sudu.experiments.diff;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.Window;

public class WindowLayouts {

  public static void largeWindowLayout(Window window) {
    V2i newSize = window.context.windowSize;
    int titleHeight = window.computeTitleHeight();
    int screenH = newSize.y - titleHeight;
    int px5dp = window.context.toPx(5);
    window.setPosition(
        new V2i(px5dp, px5dp + titleHeight),
        new V2i(newSize.x - px5dp * 2,
            screenH - px5dp * 2)
    );
  }
}
