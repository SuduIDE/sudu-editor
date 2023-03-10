package org.sudu.experiments;

public class FontLoadTest {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    t(JetBrainsMono.Medium);
    t("aaa");
  }

  static void t(String name) {
    System.out.print("loading ".concat(name));
    FontResources fr = JetBrainsMono.regular();
    FontLoaderJvm fontLoader = Application.fontLoader(fr);
    byte[] result = fontLoader.loader.apply(name);
    if (result != null) {
      System.out.println(": result[" + result.length + "]");
    } else {
      System.out.println(": result is null");
    }
  }
}
