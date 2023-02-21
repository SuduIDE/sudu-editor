package org.sudu.experiments.angle;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.win32.Win32;
import org.sudu.experiments.win32.Win32Graphics;

import java.util.function.Supplier;

public class AngleWindow {
  long surface, context, numContextRef;

  AngleDevice device;
  AngleWindow share_context;
  Win32Graphics graphics;

  public AngleWindow(long hWnd, boolean debug, Supplier<Win32Graphics> s) {
    this(hWnd, debug, null, s);
  }

  public Win32Graphics graphics() {
    return graphics;
  }

  public AngleWindow(long hWnd, boolean debug, AngleWindow share_context, Supplier<Win32Graphics> g) {
    this.share_context = share_context;
    device = AngleDevice.Static.get(Win32.GetDC(hWnd));

    if (share_context != null) share_context.numContextRef++;

    context = device.createContext(debug, share_context == null ? 0 : share_context.context);
    if (context == 0) return;

    surface = device.createWindowSurface(hWnd);
    if (surface == 0) return;

    boolean contextOK = setCurrentContext();
    if (!contextOK) return;

    V2i surfaceSize = getSurfaceSize();
    if (surfaceSize == null) return;

    graphics = share_context != null ? share_context.graphics : g.get();
    graphics.setViewPortAndClientRect(surfaceSize.x, surfaceSize.y);
  }

  public boolean isRootContext() {
    return share_context == null;
  }

  public boolean initialized() {
    return graphics != null && context != 0 && surface != 0;
  }

  public V2i makeCurrent() {
    boolean r = setCurrentContext();
    if (r) {
      V2i size = getSurfaceSize();
      graphics.setViewPortAndClientRect(size.x, size.y);
      graphics.restoreState();
      return size;
    } else {
      System.err.println("AngleWindow::makeCurrent setCurrentContext failed");
      AngleEGL.dumpError();
      return null;
    }
  }

  private boolean setCurrentContext() {
    return AngleEGL.makeCurrent(device.display, surface, surface, context);
  }

  public boolean swapInterval(int interval) {
    boolean r = AngleEGL.swapInterval(device.display, interval);
    if (!r) AngleEGL.dumpError();
    return r;
  }

  public boolean swapBuffers() {
    graphics.resetState();
    boolean r = AngleEGL.swapBuffers(device.display, surface);
    if (!r) AngleEGL.dumpError();
    return r;
  }

  public V2i getSurfaceSize() {
    return AngleEGL.querySurfaceSize(device.display, surface);
  }

  public boolean dispose() {
    if (share_context != null) share_context.numContextRef--;
    if (numContextRef > 0)
      System.err.println("trying to delete shared eglContext with numContextRef = " + numContextRef);
    boolean b1 = context != 0 && AngleEGL.destroyContext(device.display, context);
    boolean b2 = surface != 0 && AngleEGL.destroySurface(device.display, surface);
    context = 0;
    surface = 0;
    return b1 && b2;
  }

  @Override
  public String toString() {
    return "AngleWindow{" +
        "surface=" + Long.toHexString(surface) +
        ", context=" + Long.toHexString(context) +
        ", device=" + device + '}';
  }
}
