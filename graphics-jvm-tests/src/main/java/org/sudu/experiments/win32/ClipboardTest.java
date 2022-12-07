package org.sudu.experiments.win32;

public class ClipboardTest {

  public static class Set {
    public static void main(String[] args) throws InterruptedException {
      Helper.loadDlls();

      long hwnd = WindowTest.createWindow(Win32::DefWindowProcW);

      boolean result = Win32.setClipboardText(hwnd, "текст надо ?");
      System.out.println("setClipboardText = " + result);

      String clipboardText = Win32.getClipboardText(hwnd, null);
      System.out.println("getClipboardText = " + clipboardText);

      Win32.DestroyWindow(hwnd);
    }
  }

}
