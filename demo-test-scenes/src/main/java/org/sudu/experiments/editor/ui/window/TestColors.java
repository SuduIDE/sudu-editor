package org.sudu.experiments.editor.ui.window;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.window.ScrollView;

public interface TestColors {
  Color scrollBarLine = new Color(40, 40, 40, 200);
  Color scrollBarBg = new Color(43, 43, 43, 128);

  static ScrollView apply(ScrollView v) {
    v.setScrollColor(scrollBarLine, scrollBarBg);
    return v;
  }

  static void apply(ScrollBar sb) {
    sb.setColor(scrollBarLine, scrollBarBg);
  }
}
