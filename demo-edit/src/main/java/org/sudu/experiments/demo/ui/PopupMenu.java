package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Const;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.V2i;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PopupMenu {
  private final V2i windowSize = new V2i();
  private final ArrayList<Toolbar> toolbars = new ArrayList<>();
  private final WglGraphics graphics;
  private double dpr;
  private FontDesk font;
  private DialogItemColors theme;
  private Runnable onClose = Const.emptyRunnable;

  public PopupMenu(WglGraphics graphics) {
    this.graphics = graphics;
  }

  // todo: change font and size if dps changed on
  public void setFont(FontDesk f) {
    font = f;
  }

  public void setTheme(DialogItemColors dialogItemColors) {
    theme = dialogItemColors;
    for (Toolbar toolbar : toolbars) {
      toolbar.setTheme(theme);
    }
  }

  public void display(V2i mousePos, Supplier<ToolbarItem[]> actions, Runnable onClose) {
    if (font == null || isVisible()) {
      throw new IllegalArgumentException();
    }
    this.onClose = onClose;

    Toolbar rootMenu = displaySubMenu(mousePos, actions, null);
    rootMenu.onClickOutside(this::hide);
  }

  public void hide() {
    if (isVisible()) {
      removePopupsAfter(null);
      onClose.run();
      onClose = Const.emptyRunnable;
    }
  }

  private Toolbar displaySubMenu(V2i pos, Supplier<ToolbarItem[]> items, Toolbar parent) {
    if (windowSize.x * windowSize.y == 0 || dpr == 0) {
      throw new IllegalStateException(
          "trying to display popup with unknown screen size and dpr");
    }

    Toolbar popup = new Toolbar();
    popup.setLayoutVertical();
    popup.setItems(items.get());
    setToolbarStyle(popup);
    popup.measure(graphics.mCanvas, dpr);

    int x = parent != null ? relativeToParentPos(pos.x, parent, popup) : pos.x;
    setScreenLimitedPosition(popup, x, pos.y, windowSize);

    popup.onEnter((mouse, index, item) -> {
      removePopupsAfter(popup);
      if (item.isSubmenu()) {
        displaySubMenu(
            computeSubmenuPosition(item, popup),
            item.subMenu(), popup);
      }
    });

    toolbars.add(popup);
    return popup;
  }

  private void setToolbarStyle(Toolbar tb) {
    tb.setTheme(theme);
    tb.setFont(font);
  }

  public void onResize(V2i newSize, double newDpr) {
    windowSize.set(newSize);
    if (this.dpr != newDpr) {
      for (Toolbar toolbar : toolbars) {
        toolbar.measure(graphics.mCanvas, newDpr);
      }
      this.dpr = newDpr;
    }
  }

  public void paint() {
    // let's do 0-garbage rendering
    if (!toolbars.isEmpty()) graphics.enableBlend(true);
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0; i < toolbars.size(); i++) {
      toolbars.get(i).render(graphics, dpr);
    }
  }

  public boolean onMouseMove(V2i mouse, SetCursor windowCursor) {
    boolean r = false;
    for (int i = toolbars.size() - 1; i >= 0; --i) {
      r = toolbars.get(i).onMouseMove(mouse, windowCursor);
      if (r) break;
    }
    return r;
  }

  public boolean onMousePress(V2i position, int button, boolean press, int clickCount) {
    boolean r = false;
    for (int i = toolbars.size() - 1; i >= 0; --i) {
      r = toolbars.get(i).onMousePress(position, button, press, clickCount);
      if (r) break;
    }
    return r;
  }

    // todo: add keyboard up-down-left-right navigation
  public boolean onKey(KeyEvent event) {
    if (!isVisible()) return false;
    return switch (event.keyCode) {
      case KeyCode.ESC -> {
        hide();
        yield true;
      }
      default -> false;
    };
  }

  private int relativeToParentPos(int posX, Toolbar parent, Toolbar popup) {
    return windowSize.x >= posX + parent.size().x + popup.size().x
        ? posX + parent.size().x - parent.margin()
        : posX - popup.size().x;
  }

  static void setScreenLimitedPosition(Toolbar popup, int x, int y, V2i screen) {
    popup.setPos(
        Math.max(0, Math.min(x, screen.x - popup.size().x)),
        Math.max(0, Math.min(y, screen.y - popup.size().y)));
  }

  private static V2i computeSubmenuPosition(ToolbarItem parentItem, Toolbar parent) {
    DemoRect view = parentItem.getView();
    int border = parent.border();
    int margin = parent.margin();
    // TODO: Submenu position leaves a gap if it opens to the left of the parent
    return new V2i(view.pos.x - border * 3 - margin, view.pos.y - border - margin);
  }

  private void removePopupsAfter(Toolbar wall) {
    for (int i = toolbars.size() - 1; i >= 0; i--) {
      Toolbar tb = toolbars.get(i);
      if (wall == tb) break;
      toolbars.remove(i);
      tb.dispose();
    }
  }

  private void disposeList(ArrayList<Toolbar> list) {
    for (Toolbar toolbar : list) {
      toolbar.dispose();
    }
    list.clear();
  }

  public boolean isVisible() {
    return toolbars.size() > 0;
  }

  public void dispose() {
    disposeList(toolbars);
  }
}
