package org.sudu.experiments.angle;

import org.sudu.experiments.win32.D2dFactory;
import org.sudu.experiments.win32.Helper;
import org.sudu.experiments.win32.Win32;
import org.sudu.experiments.win32.Win32Graphics;

import java.util.function.Supplier;

public class AngleEglTwoWindows {
  public static void main(String[] args) {
    Helper.loadDlls();
    Win32.coInitialize();

    int windowWidth = 1280;
    int windowHeight = 720;

    long hWnd1 = getCreateWindow("egl window 1", windowWidth, windowHeight);
    long hWnd2 = getCreateWindow("egl window 2", windowWidth, windowHeight);

    Supplier<Win32Graphics> graphics = Win32Graphics.lazyInit(D2dFactory.create());

    AngleWindow angleWindowContext1 = new AngleWindow(hWnd1, true, graphics);
    AngleWindow angleWindowContext2 = new AngleWindow(hWnd2, true, graphics);

    if (angleWindowContext1.device != AngleDevice.Static.get(Win32.GetDC(hWnd1))) {
      throw new RuntimeException("angleWindowContext1.deviceContext wrong");
    }

    if (angleWindowContext2.device != AngleDevice.Static.get(Win32.GetDC(hWnd2))) {
      throw new RuntimeException("angleWindowContext2.deviceContext wrong");
    }

    System.out.println("angleWindowContext1 = " + angleWindowContext1);
    System.out.println("angleWindowContext2 = " + angleWindowContext2);

    System.out.println("angleWindowContext1.getSurfaceSize(): " + angleWindowContext1.getSurfaceSize());
    System.out.println("angleWindowContext2.getSurfaceSize(): " + angleWindowContext2.getSurfaceSize());

    if (!angleWindowContext1.initialized()) throw new RuntimeException("angleWindowContext init failed");
    if (!angleWindowContext2.initialized()) throw new RuntimeException("angleWindowContext init failed");

    System.out.println("context1 = " + Long.toHexString(angleWindowContext1.context));
    System.out.println("context2 = " + Long.toHexString(angleWindowContext2.context));

    if (!angleWindowContext1.dispose()) throw new RuntimeException("angleWindowContext1 dispose failed");
    if (!angleWindowContext2.dispose()) throw new RuntimeException("angleWindowContext2 dispose failed");

    System.out.println("angle context successfully created and destroyed");
  }

  static long getCreateWindow(String title, int windowWidth, int windowHeight) {
    return Win32.CreateWindow(Win32::DefWindowProcW, title,
        Win32.CW_USEDEFAULT, Win32.CW_USEDEFAULT, windowWidth, windowHeight,
        Win32.GetModuleHandle0(), 2000);
  }
}
