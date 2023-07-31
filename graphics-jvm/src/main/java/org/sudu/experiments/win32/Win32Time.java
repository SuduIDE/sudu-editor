package org.sudu.experiments.win32;

import org.sudu.experiments.TimeUtil;

public class Win32Time {
  double frequency = Win32.GetPerformanceFrequency();
  long t0 = Win32.GetPerformanceCounter();

  public double now() {
    long tNow = Win32.GetPerformanceCounter() - t0;
    return tNow / frequency;
  }

  public void printTime(String title) {
    System.out.println("[Time] " + title + TimeUtil.toString3(now()));
  }
}
