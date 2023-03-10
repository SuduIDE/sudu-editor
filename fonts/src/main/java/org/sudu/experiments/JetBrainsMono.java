package org.sudu.experiments;

public interface JetBrainsMono {
  String typeface = "JetBrains Mono";
  String folder = "fonts/";

  String ExtraLight = "JetBrainsMono-ExtraLight.ttf";
  String ExtraLightItalic = "JetBrainsMono-ExtraLightItalic.ttf";
  String Light = "JetBrainsMono-Light.ttf";
  String LightItalic = "JetBrainsMono-LightItalic.ttf";
  String Bold = "JetBrainsMono-Bold.ttf";
  String BoldItalic = "JetBrainsMono-BoldItalic.ttf";
  String Italic = "JetBrainsMono-Italic.ttf";
  String Medium = "JetBrainsMono-Medium.ttf";
  String MediumItalic = "JetBrainsMono-MediumItalic.ttf";
  String Regular = "JetBrainsMono-Regular.ttf";
  String SemiBold = "JetBrainsMono-SemiBold.ttf";
  String SemiBoldItalic = "JetBrainsMono-SemiBoldItalic.ttf";

  static FontResources regular() {
    return fontResource(Regular);
  }

  static FontResources all() {
    return fontResource(
        ExtraLight, ExtraLightItalic,
        Light,      LightItalic,
        Regular,    Italic,
        Medium,     MediumItalic,
        SemiBold,   SemiBoldItalic,
        Bold,       BoldItalic);
  }

  static FontResources fontResource(String ... fonts) {
    return new FontResources(JetBrainsMono.class, folder, fonts);
  }

  static FontConfigJs webConfig(String file, String style, int weight) {
    return new FontConfigJs(typeface, folder.concat(file), style, weight);
  }

  static FontConfigJs[] webConfig() {
    return new FontConfigJs[] {
        webConfig(Regular, FontDesk.NORMAL, FontDesk.WEIGHT_REGULAR),
        webConfig(Italic,  FontDesk.ITALIC, FontDesk.WEIGHT_REGULAR),
        webConfig(Bold,       FontDesk.NORMAL, FontDesk.WEIGHT_BOLD),
        webConfig(BoldItalic, FontDesk.ITALIC, FontDesk.WEIGHT_BOLD)
    };
  }
}