package org.sudu.experiments.fonts;

public class FontResources {
  public final Class<?> resourceClass;
  public final String   folder;
  public final String[] fonts;

  public FontResources(
      Class<?> aClass, String folder,
      String... fonts
  ) {
    this.resourceClass = aClass;
    this.folder = folder;
    this.fonts = fonts;
  }

  public static FontResources noFonts() {
    return new FontResources(FontResources.class, "fonts/");
  }
}
