package org.sudu.experiments.ui;

import org.sudu.experiments.Const;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PopupMenu implements DprChangeListener, Focusable {
  private final UiContext context;
  private final ArrayList<Toolbar> toolbars = new ArrayList<>();
  private UiFont uiFont;
  private FontDesk font;
  private DialogItemColors theme;
  private Runnable onClose = Const.emptyRunnable;

  public PopupMenu(UiContext context) {
    this.context = context;
    context.dprListeners.add(this);
  }

  public void setTheme(DialogItemColors dialogItemColors, UiFont f) {
    uiFont = f;
    theme = dialogItemColors;
    for (Toolbar toolbar : toolbars) {
      toolbar.setTheme(theme);
    }
  }

  public DialogItemColors theme() { return theme; }

  public void display(V2i mousePos, Supplier<ToolbarItem[]> actions, Runnable onClose) {
    context.requireWindowVisible();
    if (uiFont == null || isVisible()) {
      throw new IllegalArgumentException();
    }
    this.onClose = onClose;
    font = context.fontDesk(uiFont);
    Toolbar rootMenu = displaySubMenu(mousePos, actions, null);
    rootMenu.onClickOutside(this::hide);
    context.setFocus(this);
  }

  public void onTextRenderingSettingsChange() {
    for (Toolbar toolbar : toolbars) {
      toolbar.onTextRenderingSettingsChange();
    }
  }

  public void hide() {
    if (isVisible()) {
      context.removeFocus(this);
      removePopupsAfter(null);
      onClose.run();
      onClose = Const.emptyRunnable;
    }
  }

  private Toolbar displaySubMenu(
      V2i pos,
      Supplier<ToolbarItem[]> items,
      Toolbar parent,
      Toolbar.HoverCallback onEnter
  ) {
    Toolbar popup = new Toolbar();
    popup.setLayoutVertical();
    popup.setItems(items.get());
    popup.setTheme(theme);
    popup.setFont(font, context);

    int x = parent != null ? relativeToParentPos(pos.x, parent, popup) : pos.x;
    setScreenLimitedPosition(popup, x, pos.y, context.windowSize);

    popup.onEnter((mouse, index, item) -> {
      removePopupsAfter(popup);
      if (onEnter != null) onEnter.event(mouse, index, item);
      if (item.isSubmenu()) {
        if (item.onEnter != null) {
          displaySubMenu(
              computeSubmenuPosition(item, popup),
              item.subMenu, popup, item.onEnter);
        } else {
          displaySubMenu(
              computeSubmenuPosition(item, popup),
              item.subMenu, popup);
        }
      }
    });

    toolbars.add(popup);
    return popup;
  }

  private Toolbar displaySubMenu(V2i pos, Supplier<ToolbarItem[]> items, Toolbar parent) {
    return displaySubMenu(pos, items, parent, null);
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    font = context.fontDesk(uiFont);
    for (Toolbar toolbar : toolbars) {
      toolbar.setFont(font, context);
    }
  }

  public void paint() {
    if (toolbars.isEmpty()) return;
    context.graphics.enableBlend(true);
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0; i < toolbars.size(); i++) {
      toolbars.get(i).render(context);
    }
  }

  public boolean onMouseMove(MouseEvent e) {
    boolean r = false;
    for (int i = toolbars.size() - 1; i >= 0; --i) {
      r = toolbars.get(i).onMouseMove(e.position, context.windowCursor);
      if (r) break;
    }
    return r;
  }

  public boolean onMouseClick(MouseEvent e, int button, int clickCount) {
    boolean r = false;
    for (int i = toolbars.size() - 1; i >= 0; --i) {
      r = toolbars.get(i).onMouseClick(e.position, button, clickCount);
      if (r) break;
    }
    return r;
  }

  public Consumer<MouseEvent> onMouseDown(MouseEvent e, int button) {
    Consumer<MouseEvent> r = null;
    for (int i = toolbars.size() - 1; i >= 0; --i) {
      r = toolbars.get(i).onMouseDown(e.position, button);
      if (r != null) break;
    }
    return r;
  }

  public boolean onMouseUp(MouseEvent e, int button) {
    return isVisible();
  }

  // todo: add keyboard up-down-left-right navigation
  public boolean onKeyPress(KeyEvent event) {
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
    return context.windowSize.x >= posX + parent.size().x + popup.size().x
        ? posX + parent.size().x - parent.margin()
        : posX - popup.size().x;
  }

  static void setScreenLimitedPosition(Toolbar popup, int x, int y, V2i screen) {
    popup.setPos(
        Math.max(0, Math.min(x, screen.x - popup.size().x)),
        Math.max(0, Math.min(y, screen.y - popup.size().y)));
  }

  private static V2i computeSubmenuPosition(ToolbarItem parentItem, Toolbar parent) {
    V2i parentPos = parentItem.pos;
    int border = parent.border();
    int margin = parent.margin();
    // TODO: Submenu position leaves a gap if it opens to the left of the parent
    return new V2i(parentPos.x - border * 3 - margin, parentPos.y - border - margin);
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
    context.dprListeners.remove(this);
    context.removeFocus(this);
    disposeList(toolbars);
  }
}
