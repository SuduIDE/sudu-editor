package org.sudu.experiments.ui.window;


import org.sudu.experiments.Disposable;
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
  private final ArrayList<Window> windows = new ArrayList<>();
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

  public PopupMenu popupMenu() { return popupMenu; }

  public void addWindow(Window window) {
    windows.add(0, window);
  }

  public void removeWindow(Window window) {
    windows.remove(window);
  }

  public void draw() {
    for (int i = windows.size() - 1; i >= 0; i--) {
      windows.get(i).draw(uiContext.graphics);
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
    for (int i = 0; i < windows.size(); i++) {
      windows.get(i).onTextRenderingSettingsChange();
    }
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    if (popupMenu != null && popupMenu.onMouseMove(event))
      return true;

    for (int i = 0; i < windows.size(); i++) {
      if (windows.get(i).onMouseMove(event)) return true;
    }
    return false;
  }

  @Override
  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (popupMenu != null && popupMenu.onMouseClick(event, button, clickCount))
      return true;
    for (int i = 0; i < windows.size(); i++) {
      if (windows.get(i).onMouseClick(event, button, clickCount)) return true;
    }
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
    for (int i = 0; i < windows.size(); i++) {
      Consumer<MouseEvent> lock = windows.get(i).onMouseDown(event, button);
      if (lock != null) return lock;
    }
    return null;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    if (popupMenu != null && popupMenu.onMouseUp(event, button))
      return true;
    for (int i = 0; i < windows.size(); i++) {
      if (windows.get(i).onMouseUp(event, button)) return true;
    }
    return false;
  }

  public boolean onScroll(MouseEvent event, float dX, float dY) {
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
    setPopupMenu(null);
    for (int i = 0; i < windows.size(); i++) {
      windows.get(i).dispose();
    }
    windows.clear();
  }
}
