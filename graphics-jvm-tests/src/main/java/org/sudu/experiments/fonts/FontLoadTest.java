package org.sudu.experiments.fonts;

import org.sudu.experiments.ResourceLoader;

public class FontLoadTest {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    t(JetBrainsMono.Medium);
    t("aaa");
  }

  static void t(String name) {
    System.out.print("loading ".concat(name));
    byte[] result = ResourceLoader.load(name, JetBrainsMono.regular());
    if (result != null) {
      System.out.println(": result[" + result.length + "]");
    } else {
      System.out.println(": result is null");
    }
  }
}
