package org.sudu.experiments.ui;

import org.sudu.experiments.editor.DemoRect;
import org.sudu.experiments.editor.TextRect;
import org.sudu.experiments.math.V2i;

import java.util.function.Supplier;

public class ToolbarItem {
  final TextRect tRect = new TextRect();
  final Runnable action;
  ToolbarItemColors colors;
  final Supplier<ToolbarItem[]> subMenu;
  final Toolbar.HoverCallback onEnter;
  String text;

  public ToolbarItem(Runnable r, String text, ToolbarItemColors colors) {
    this(r, text, colors, null, null);
  }

  public ToolbarItem(
      Runnable r,
      String text,
      ToolbarItemColors colors,
      Supplier<ToolbarItem[]> submenu,
      Toolbar.HoverCallback onEnter
  ) {
    this.text = text;
    this.colors = colors;
    this.onEnter = onEnter;
    action = r;
    tRect.color.set(colors.color);
    tRect.bgColor.set(colors.bgColor);
    subMenu = submenu;
  }

  public V2i getPos() {
    return tRect.pos;
  }

  public DemoRect getView() {
    return tRect;
  }

  public boolean isSubmenu() {
    return subMenu != null;
  }

  public Toolbar.HoverCallback onEnter() {
    return onEnter;
  }

  public Runnable action() {
    return action;
  }

  public Supplier<ToolbarItem[]> subMenu() {
    return subMenu;
  }

  public void setHover(boolean b) {
    tRect.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
  }

  public void setTheme(ToolbarItemColors toolbarItemColors) {
    colors = toolbarItemColors;
    tRect.setColors(
        toolbarItemColors.color,
        toolbarItemColors.bgColor
    );
  }
}
