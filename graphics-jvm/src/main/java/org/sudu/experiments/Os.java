package org.sudu.experiments;

public enum Os {
  Windows, Linux, Mac, Other;

  public static final String os = System.getProperty("os.name").toLowerCase();
  public static final Os current = current(os);

  public static Os current(String os) {
    if (os.contains("windows")) return Windows;
    if (os.contains("linux")) return Linux;
    if (os.contains("mac")) return Mac;
    return Other;
  }
}
