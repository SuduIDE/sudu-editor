package org.sudu.experiments.angle;

import java.util.HashMap;

public class AngleDevice {

  final long win32hDC;
  long display;
  long config;

  public AngleDevice(long hDC) {
    win32hDC = hDC;
    display = AngleEGL.getPlatformDisplayD3D11(hDC);
    if (display == 0) { AngleEGL.dumpError(); return; }

    boolean init = AngleEGL.initialize(display);
    if (!init) { AngleEGL.dumpError(); return; }

    config = AngleEGL.chooseConfig8888(display);
    if (config == 0) AngleEGL.dumpError();
  }

  public long createContext(boolean debug, long share_context) {
    return initSuccess() ? AngleEGL.createContext(display, config, share_context, debug) : 0;
  }

  public long createWindowSurface(long hWnd) {
    return initSuccess() ? AngleEGL.createWindowSurface(display, config, hWnd, null) : 0;
  }

  public boolean initSuccess() {
    return config != 0 && display != 0;
  }

  public void dispose() {
    boolean terminate = AngleEGL.terminate(display);
    if (!terminate) AngleEGL.dumpError();
    display = 0;
    config = 0;
  }

  @Override
  public String toString() {
    return "AngleDeviceContext{" +
        "win32hDC=" + Long.toHexString(win32hDC) +
        ", display=" + Long.toHexString(display) +
        ", config=" + Long.toHexString(config) +
        '}';
  }

  interface Static {
    HashMap<Long, AngleDevice> map = new HashMap<>();

    static AngleDevice get(long hDC) {
      return map.computeIfAbsent(hDC, AngleDevice::new);
    }

    static void dispose() {
      for (AngleDevice angleDevice : map.values()) {
        angleDevice.dispose();
      }
      map.clear();
    }
  }
}
