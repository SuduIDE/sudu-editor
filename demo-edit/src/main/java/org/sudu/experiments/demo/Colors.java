package org.sudu.experiments.demo;

import org.sudu.experiments.demo.ui.FindUsagesItemColors;
import org.sudu.experiments.demo.ui.ToolbarItemColors;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

public interface Colors {

  V4f scrollBarBody1 = Color.Cvt.fromRGBA(50, 50, 50, 100);
  V4f scrollBarBody2 = Color.Cvt.fromRGBA(80, 80, 80, 200);
  V4f toolbarBg = new Color("#3C3F41");
  V4f toolbarBorder = new Color("#616161");
  V4f toolbarSelectedBg = new Color("#4B6EAF");

  V4f findUsagesBg = new Color("#3C3F41");
  V4f findUsagesBorder = new Color("#616161");
  V4f findUsagesSelectedBg = new Color("#4B6EAF");

  FindUsagesItemColors findUsagesColors = new FindUsagesItemColors(
          IdeaCodeColors.Colors.defaultText, IdeaCodeColors.Colors.editNumbersVLine,
          IdeaCodeColors.Colors.defaultText, Colors.findUsagesBg, Colors.findUsagesSelectedBg
  );

  FindUsagesItemColors findUsagesColorsContinued = new FindUsagesItemColors(
          new Color("#CCCCCC"), IdeaCodeColors.Colors.editNumbersVLine,
          IdeaCodeColors.Colors.defaultText, Colors.findUsagesBg, Colors.findUsagesBg
  );

  ToolbarItemColors popupText = new ToolbarItemColors(
      new Color("#BBBBBB"), toolbarBg, toolbarSelectedBg);
  ToolbarItemColors popupText2 = new ToolbarItemColors(
      new Color("#CCCCCC"), toolbarBg, toolbarSelectedBg);

  static ToolbarItemColors rngToolButton() {
    return new ToolbarItemColors(
        Color.Cvt.fromHSV(Math.random(), 1, 1), toolbarBg, toolbarSelectedBg);
  }

}
