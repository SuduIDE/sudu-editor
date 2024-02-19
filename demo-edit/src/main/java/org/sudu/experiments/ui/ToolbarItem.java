package org.sudu.experiments.ui;

import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.function.Supplier;

// todo refactor to model view
public class ToolbarItem {
  final Runnable action;

  final V4f textureRegion = new V4f();
  final V2i pos = new V2i();
  final V2i size = new V2i();

  final Supplier<ToolbarItem[]> subMenu;
  final Toolbar.HoverCallback onEnter;
  boolean hover;
  String text;

  public ToolbarItem(Runnable r, String text) {
    this(r, text, null, null);
  }

  public ToolbarItem(
      Runnable r,
      String text,
      Supplier<ToolbarItem[]> submenu,
      Toolbar.HoverCallback onEnter
  ) {
    this.text = text;
    this.onEnter = onEnter;
    action = r;
    subMenu = submenu;
  }

  public boolean isSubmenu() {
    return subMenu != null;
  }

  public void setHover(boolean b) {
    hover = b;
  }

  public boolean isInside(V2i p) {
    return Rect.isInside(p, pos, size);
  }
}
