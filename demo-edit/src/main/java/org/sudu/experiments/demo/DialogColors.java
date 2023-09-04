package org.sudu.experiments.demo;

import org.sudu.experiments.math.Color;

public interface DialogColors {

  interface Darcula {
    Color findUsagesBg = new Color("#3C3F41");
    Color findUsagesBgCaret = new Color("#4B6EAF");
    Color findUsagesTextCaret = new Color("#BBBBBB");
    Color findUsagesLineNumber = new Color("#787878");
    Color findUsagesContent = new Color("#A9B7C6");
    Color toolbarBg = new Color("#3C3F41");
    Color toolbarSelectedBg = new Color("#4B6EAF");
    Color scrollBarLine = new Color(118, 121, 122, 128);
    Color scrollBarBg = new Color(63, 66, 68);
  }

  // TODO: get colors
  interface Dark {
    Color findUsagesBg = new Color("#");
    Color findUsagesBgCaret = new Color("#");
    Color findUsagesTextCaret = new Color("#");
    Color findUsagesLineNumber = new Color("#");
    Color findUsagesContent = new Color("#");
    Color toolbarBg = new Color("#");
    Color toolbarSelectedBg = new Color("#");
    Color scrollBarLine = new Color("#");
    Color scrollBarBg = new Color(63, 66, 68);
  }

  interface Light {
    Color findUsagesBg = new Color("#FFFFFF");
    Color findUsagesBgCaret = new Color("#D4E2FF");
    Color findUsagesTextCaret = new Color("#000000");
    Color findUsagesLineNumber = new Color("#818594");
    Color findUsagesContent = new Color("#080808");
    Color toolbarBg = new Color("#FFFFFF");
    Color toolbarSelectedBg = new Color("#D4E2FF");
    Color scrollBarLine = new Color(205, 205, 205, 153);
    Color scrollBarBg = new Color(247, 248, 250);
  }

}
