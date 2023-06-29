package org.sudu.experiments.demo;

import org.sudu.experiments.demo.ui.FindUsagesItemColors;
import org.sudu.experiments.demo.ui.ToolbarItemColors;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V4f;

import java.util.Objects;

public interface Colors {

  V4f scrollBarBody1 = Color.Cvt.fromRGBA(50, 50, 50, 100);
  V4f scrollBarBody2 = Color.Cvt.fromRGBA(80, 80, 80, 200);
  V4f toolbarBg = new Color("#3C3F41");
  V4f toolbarBorder = new Color("#616161");
  V4f toolbarSelectedBg = new Color("#4B6EAF");
  V4f toolbarErrorBg = new Color("#781732");

  V4f findUsagesBg = new Color("#3C3F41");
  V4f findUsagesBorder = new Color("#616161");
  V4f findUsagesSelectedBg = new Color("#4B6EAF");
  V4f findusagesBgLight = IdeaCodeColors.Colors.editBgColorLight;
  V4f findUsagesErrorBg = new Color("#781732");

  static Color findUsagesColorBorderByScheme(EditorColorScheme scheme) {
    if (Objects.equals(scheme.editBgColor, IdeaCodeColors.Colors.editBgColor)) {
      return new Color("#616161");
    }
    return new Color("#B9BDC9");
  }

  static FindUsagesItemColors findUsagesColorsByScheme(EditorColorScheme scheme) {
    if (Objects.equals(scheme.editBgColor, IdeaCodeColors.Colors.editBgColor)) {
      return FindUsagesItemColors.darkFindUsagesItemColors();
    }
    return FindUsagesItemColors.lightFindUsagesItemColors();
  }

  static FindUsagesItemColors findUsagesColorsContinuedByScheme(EditorColorScheme scheme) {
    if (Objects.equals(scheme.editBgColor, IdeaCodeColors.Colors.editBgColor)) {
      return FindUsagesItemColors.darkFindUsagesItemColorsExtraLine();
    }
    return FindUsagesItemColors.lightFindUsagesItemColorsExtraLine();
  }

  static FindUsagesItemColors findUsagesColorsErrorByScheme(EditorColorScheme scheme) {
    if (Objects.equals(scheme.editBgColor, IdeaCodeColors.Colors.editBgColor)) {
      return FindUsagesItemColors.darkFindUsagesItemColorsError();
    }
    return FindUsagesItemColors.lightFindUsagesItemColorsError();
  }

  ToolbarItemColors popupText = new ToolbarItemColors(
      new Color("#BBBBBB"), toolbarBg, toolbarSelectedBg);
  ToolbarItemColors popupText2 = new ToolbarItemColors(
      new Color("#CCCCCC"), toolbarBg, toolbarSelectedBg);
  ToolbarItemColors popupErrorText = new ToolbarItemColors(
      new Color("#BBBBBB"), toolbarErrorBg, toolbarErrorBg);

  static ToolbarItemColors rngToolButton() {
    return new ToolbarItemColors(
        Color.Cvt.fromHSV(Math.random(), 1, 1), toolbarBg, toolbarSelectedBg);
  }
}
