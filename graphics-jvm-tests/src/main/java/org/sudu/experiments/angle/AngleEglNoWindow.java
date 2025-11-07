package org.sudu.experiments.angle;

import org.sudu.experiments.win32.Helper;

public class AngleEglNoWindow {
  public static void main(String[] args) {
    Helper.loadDlls();

    AngleEglTest.dumpVersion(0);

    long display = AngleEGL.getPlatformDisplayD3D11(0);
    System.out.println("display = " + Long.toHexString(display));

  }
}
