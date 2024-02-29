package org.sudu.experiments.ui.window;


import org.sudu.experiments.Disposable;
import org.sudu.experiments.Subscribers;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.PopupMenu;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.UiFont;

import java.util.ArrayList;
import java.util.function.Consumer;

// manages list of active windows,
//      pass input evens,
//      invoke paint in desired order
@SuppressWarnings("ForLoopReplaceableByForEach")
public class WindowManager implements MouseListener, DprChangeListener {

  public final UiContext uiContext;
  private final Subscribers<Window> windows = new Subscribers<>(new Window[0]);
  private PopupMenu popupMenu;

  public WindowManager(UiContext uiContext) {
    this.uiContext = uiContext;
  }

  public PopupMenu newPopup(DialogItemColors theme, UiFont f) {
    PopupMenu popupMenu = new PopupMenu(uiContext);
    popupMenu.setTheme(theme, f);
    popupMenu.onClose(uiContext.captureAndRestoreFocus());
    return popupMenu;
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

  public void hidePopupMenu() {
    if (popupMenu != null) {
      popupMenu.hide();
      setPopupMenu(null);
    }
  }

  public Window addWindow(Window window) {
    windows.add(0, window);
    return window;
  }

  public void removeWindow(Window window) {
    windows.remove(window);
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
    if (popupMenu != null && popupMenu.onMouseMove(event))
      return true;

    for (Window window : windows.array())
      if (window.onMouseMove(event))
        return true;
    return false;
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
      Consumer<MouseEvent> lock = win.onMouseDown(event, button);
      if (lock != null) {
        if (win != topWindow()) {
          int index = windows.find(win);
          if (index >= 0) windows.moveToFront(index);
        }
        return lock;
      }
    }
    return null;
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
}
