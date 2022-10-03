package org.sudu.experiments.nativelib;

class AngleDllTest {
  public static void main(String[] args) {
    System.out.println("AngleDll.loaded() = " + AngleDll.loaded());
    System.out.println("pathEGL = " + AngleDll.pathEGL);
    System.out.println("pathGLESv2 = " + AngleDll.pathGLESv2);
  }
}