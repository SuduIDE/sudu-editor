package org.sudu.experiments;

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
}
