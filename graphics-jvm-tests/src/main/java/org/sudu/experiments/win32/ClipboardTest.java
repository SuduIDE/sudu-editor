package org.sudu.experiments.win32;

import org.junit.jupiter.api.Assertions;

public class ClipboardTest {

  public static class Set {
    public static void main(String[] args) throws InterruptedException {
      Helper.loadDlls();

      long hwnd = WindowTest.createWindow(Win32::DefWindowProcW);

      String text = "текст надо ?";
      boolean result = Win32.setClipboardText(hwnd, text);
      System.out.println("setClipboardText = " + result);

      String clipboardText = Win32.getClipboardText(hwnd, null);
      System.out.println("getClipboardText = " + clipboardText);

      Win32.DestroyWindow(hwnd);

      Assertions.assertEquals(text, clipboardText);
    }
  }

}
