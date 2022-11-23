package org.sudu.experiments.nativelib;

import org.sudu.experiments.DllLoader;

public class SuduDll {
  public static final String lib = "sudu";

  public static final String path = DllLoader.loadDll(lib, SuduDll.class);

  public static boolean loaded() {
    return path != null;
  }

  public static void require() {
    if (!loaded()) throw new RuntimeException(lib + " can not be loaded");
  }
}
