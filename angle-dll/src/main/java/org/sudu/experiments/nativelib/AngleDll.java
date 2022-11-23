package org.sudu.experiments.nativelib;

import org.sudu.experiments.DllLoader;

public class AngleDll {
  public static final String libGLESv2 = "libGLESv2";

  public static final String pathGLESv2 = DllLoader.loadDll(libGLESv2, AngleDll.class);

  public static boolean loaded() {
    return pathGLESv2 != null;
  }

  public static void require() {
    if (!loaded()) {
      throw new RuntimeException(libGLESv2 + " can not be loaded");
    }
  }
}
