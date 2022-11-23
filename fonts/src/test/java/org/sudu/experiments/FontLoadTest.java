package org.sudu.experiments;

public class FontLoadTest {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    t("fonts/JetBrainsMono-Medium.ttf");
    t("aaa");
  }

  static void t(String name) {
    System.out.println("loading ".concat(name));
    byte[] result = FontLoader.load(name);
    if (result != null) {
      System.out.println(" result[" + result.length + "]");
    } else {
      System.out.println(" result is null");
    }
  }
}
