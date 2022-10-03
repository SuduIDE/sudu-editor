package org.sudu.experiments.nativelib;

import org.sudu.experiments.DllLoader;

public class AngleDll {
  public static final String libEGL = "libEGL";
  public static final String libGLESv2 = "libGLESv2";

  public static final String pathEGL = DllLoader.loadDll(libEGL, AngleDll.class);
  public static final String pathGLESv2 = DllLoader.loadDll(libGLESv2, AngleDll.class);

  public static boolean loaded() {
    return pathEGL != null && pathGLESv2 != null;
  }
}
