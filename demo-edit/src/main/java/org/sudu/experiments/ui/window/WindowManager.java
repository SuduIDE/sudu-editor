package org.sudu.experiments.ui.window;


import org.sudu.experiments.Disposable;
import org.sudu.experiments.Subscribers;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

// manages list of active windows,
//      pass input evens,
//      invoke paint in desired order
@SuppressWarnings("ForLoopReplaceableByForEach")
public class WindowManager implements MouseListener, DprChangeListener {

  public final UiContext uiContext;
  private final Subscribers<Window> windows = new Subscribers<>(new Window[0]);
  private PopupMenu popupMenu;
  int lastMouseWindow = -1;
  boolean desktopMousePointer = true;
  final V2i lastMousePosition = new V2i();

  public WindowManager(UiContext uiContext) {
    this.uiContext = uiContext;
  }

  public void showPopup(
      DialogItemColors theme, UiFont f,
      V2i pos, Supplier<ToolbarItem[]> actions
  ) {
    PopupMenu p = new PopupMenu(uiContext);
    p.setTheme(theme, f);
    p.onClose(uiContext.captureAndRestoreFocus());
    p.setItems(pos, actions);
    setPopupMenu(p);
  }

  public void setPopupMenu(PopupMenu popup) {
    if (popupMenu != popup) {
      popupMenu = Disposable.assign(popupMenu, popup);
    }
  }

  public void setPopupTheme(EditorColorScheme theme) {
    if (popupMenu != null) {
      popupMenu.setTheme(theme.dialogItem, theme.popupMenuFont);
    }
  }

  public Runnable hidePopupMenuThen(Runnable r) {
    return () -> {
      hidePopupMenu();
      r.run();
    };
  }

  public <T> Consumer<T> hidePopupMenuThen(Consumer<T> consumer) {
    return t -> {
      hidePopupMenu();
      consumer.accept(t);
    };
  }

  public void hidePopupMenu() {
    if (popupMenu != null) {
      popupMenu.hide();
      setPopupMenu(null);
    }
  }

  public Window addWindow(Window window) {
    if (windows.length() > 0)
      windows.get(0).blur();
    if (lastMouseWindow >= 0) {
      if (window.pickTest(lastMousePosition)) {
        fireMouseLeave();
      } else {
        ++lastMouseWindow;
      }
    }
    windows.add(0, window);
    window.focus();
    return window;
  }

  private void fireMouseLeave() {
    if (lastMouseWindow >= 0) {
      windows.get(lastMouseWindow).fireMouseLeave();
      lastMouseWindow = -1;
    }
  }

  public void removeWindow(Window window) {
    boolean isTop = topWindow() == window;
    if (isTop) {
      if (lastMouseWindow == 0) {
        window.fireMouseLeave();
        lastMouseWindow = -1;
      }
      window.blur();
    }
    int index = windows.find(window);
    if (lastMouseWindow > index)
      --lastMouseWindow;
    windows.remove(window);
    if (windows.length() > 0)
      windows.get(0).focus();
  }

  public void draw() {
    Window[] ws = windows.array();
    for (int i = ws.length - 1; i >= 0; i--) {
      ws[i].draw(uiContext.graphics);
    }
    if (popupMenu != null) popupMenu.paint();
  }

  public boolean enableCleartype(boolean en) {
    boolean changed = uiContext.enableCleartype(en);
    if (changed) onTextRenderingSettingsChange();
    return changed;
  }

  public void onTextRenderingSettingsChange() {
    if (popupMenu != null)
      popupMenu.onTextRenderingSettingsChange();
    for (Window window : windows.array())
      window.onTextRenderingSettingsChange();
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    var popupHit = popupMenu != null && popupMenu.onMouseMove(event);
    int hitIndex = -1, i = 0;

    lastMousePosition.set(event.position);
    if (!popupHit) {
      var setCursor = uiContext.windowCursor;
      Window[] array = windows.array();
      for (; i < array.length; i++) {
        Window window = array[i];
        var hit = window.onMouseMove(event, setCursor);
        if (hit) {
          hitIndex = i;
          break;
        }
      }
    }
    if (lastMouseWindow != hitIndex) {
      if (lastMouseWindow >= 0) {
        windows.get(lastMouseWindow).fireMouseLeave();
      }
      lastMouseWindow = hitIndex;
    }
    if (desktopMousePointer && !popupHit && hitIndex < 0)
      uiContext.window.setCursor(null);
    return popupHit || hitIndex >= 0 ;
  }

  public void setDesktopMousePointer(boolean v) {
    desktopMousePointer = v;
  }

  @Override
  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (popupMenu != null && popupMenu.onMouseClick(event, button, clickCount))
      return true;
    for (Window window : windows.array())
      if (window.onMouseClick(event, button, clickCount))
        return true;
    return false;
  }

  int id;

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
//    System.out.println("onMouseDown(" + ++id + ")");
    if (popupMenu != null) {
      var r = popupMenu.onMouseDown(event, button);
      if (r != null) return r;
    }
    Window[] ws = windows.array();
    for (int i = 0; i < ws.length; i++) {
      Window win = ws[i];
      var lock = win.onMouseDownFrame(event.position, button);
      boolean hit = lock != null || win.contentHitTest(event);
      boolean toTop = button == MOUSE_BUTTON_LEFT && i != 0 && hit;
      if (toTop) {
        int index = windows.find(win);
        if (index > 0) {
          moveToFront(index);
        }
      }
      if (lock == null && hit)
        lock = win.onMouseDownContent(event, button);
      if (lock != null || toTop)
        return lock;
    }
    return null;
  }

  private void moveToFront(int index) {
    windows.get(0).blur();

    if (lastMouseWindow == index) {
      lastMouseWindow = 0;
    } else if (lastMouseWindow >= 0) {
      var t = windows.get(index).pickTest(lastMousePosition);
      if (t) {
        fireMouseLeave();
      } else {
        if (lastMouseWindow < index)
          ++lastMouseWindow;
      }
    }
    windows.moveToFront(index);
    windows.get(0).focus();
  }

  public void nextWindow() {
    int length = windows.length();
    if (length > 1)
      moveToFront(length - 1);
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    if (popupMenu != null && popupMenu.onMouseUp(event, button))
      return true;

    for (Window window : windows.array())
      if (window.onMouseUp(event, button)) return true;

    return false;
  }

  public boolean onScroll(MouseEvent event, float dX, float dY) {
    for (Window window : windows.array())
      if (window.onScroll(event, dX, dY))
        return true;

    return false;
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    for (Window window : windows.array())
      window.onDprChanged(oldDpr, newDpr);
  }

  public boolean update(double timestamp) {
    boolean r = false;
    for (Window window : windows.array())
      r = window.update(timestamp) | r;
    return r;
  }

  public void onResize(V2i newSize, float newDpr) {
    for (Window window : windows.array())
      window.onHostResize(newSize, newDpr);
  }

  public void dispose() {
    setPopupMenu(null);
    for (Window window : windows.array())
      window.dispose();
    windows.clear();
  }

  public Window topWindow() {
    return windows.length() > 0 ? windows.get(0) : null;
  }

  public Window pickWindow(V2i pos) {
    for (int i = 0, n = windows.length(); i < n; i++) {
      if (windows.get(i).pickTest(pos))
        return windows.get(i);
    }
    return null;
  }

  public boolean onContextMenu(MouseEvent mouseEvent) {
    V2i position = mouseEvent.position;
    Window window = pickWindow(position);
    return window != null && window.onContextMenu(position);
  }
}
