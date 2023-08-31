package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

public interface DialogColors {

  interface Darcula {
    V4f findUsagesBg = new Color("#3C3F41");
    V4f findUsagesBgCaret = new Color("#4B6EAF");
    Color findUsagesTextCaret = new Color("#BBBBBB");
    Color findUsagesLineNumber = new Color("#787878");
    Color findUsagesContent = new Color("#A9B7C6");
    V4f toolbarBg = new Color("#3C3F41");
    V4f toolbarSelectedBg = new Color("#4B6EAF");
  }

  interface Light {
    V4f findUsagesBg = new Color("#FFFFFF");
    V4f findUsagesBgCaret = new Color("#D4E2FF");
    Color findUsagesTextCaret = new Color("#000000");
    Color findUsagesLineNumber = new Color("#818594");
    Color findUsagesContent = new Color("#080808");
    V4f toolbarBg = new Color("#FFFFFF");
    V4f toolbarSelectedBg = new Color("#D4E2FF");
  }

}
