package org.sudu.experiments.demo.ui.window;


import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.DprChangeListener;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;

import java.util.ArrayList;
import java.util.function.Consumer;

// manages list of active windows,
//      pass input evens,
//      invoke paint in desired order
@SuppressWarnings("ForLoopReplaceableByForEach")
public class WindowManager implements MouseListener, DprChangeListener {

  private final ArrayList<Window> windows = new ArrayList<>();
  private Consumer<MouseEvent> dragLock;
  private int dragButton;

  public WindowManager() {}

  public void addWindow(Window window) {
    windows.add(0, window);
  }

  public void removeWindow(Window window) {
    windows.remove(window);
  }

  public void draw(WglGraphics graphics) {
    for (int i = windows.size() - 1; i >= 0; i--) {
      windows.get(i).draw(graphics);
    }
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    if (dragLock != null) {
      dragLock.accept(event);
      return true;
    }

    for (int i = 0; i < windows.size(); i++) {
      if (windows.get(i).onMouseMove(event)) return true;
    }
    return false;
  }

  @Override
  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    for (int i = 0; i < windows.size(); i++) {
      if (windows.get(i).onMouseClick(event, button, clickCount)) return true;
    }
    return false;
  }

  @Override
  public boolean onMouseDown(MouseEvent event, int button) {
    if (dragLock != null) return true;
//    System.out.println("onMouseDown lock = " + dragLock);

    for (int i = 0; i < windows.size(); i++) {
      Consumer<MouseEvent> lock = windows.get(i).onMouseDown(event, button);
      if (lock != null) {
        dragLock = lock;
        dragButton = button;
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
//    System.out.println("onMouseUp");
    if (button == dragButton && dragLock != null) {
      dragLock = null;
//      System.out.println("    dragLock = null;");
      return true;
    }

    for (int i = 0; i < windows.size(); i++) {
      if (windows.get(i).onMouseUp(event, button)) return true;
    }
    return false;
  }

  boolean onScroll(MouseEvent event, float dX, float dY) {
    for (int i = 0; i < windows.size(); i++) {
      if (windows.get(i).onScroll(event, dX, dY)) return true;
    }
    return false;
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    for (int i = 0; i < windows.size(); i++) {
      windows.get(i).onDprChanged(oldDpr, newDpr);
    }
  }

  public boolean update(double timestamp) {
    boolean r = false;
    for (int i = 0; i < windows.size(); i++) {
      r = windows.get(i).update(timestamp) | r;
    }
    return r;
  }

  public void onResize(V2i newSize, float newDpr) {
    for (int i = 0; i < windows.size(); i++) {
      windows.get(i).onHostResize(newSize, newDpr);
    }
  }

  public void dispose() {
    for (int i = 0; i < windows.size(); i++) {
      windows.get(i).dispose();
    }
    windows.clear();
  }
}
