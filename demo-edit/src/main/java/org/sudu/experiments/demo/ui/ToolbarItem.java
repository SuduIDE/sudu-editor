package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.TextRect;
import org.sudu.experiments.math.V2i;

import java.util.function.Supplier;

public class ToolbarItem {
  final TextRect tRect = new TextRect();
  final Runnable action;
  ToolbarItemColors colors;
  final Supplier<ToolbarItem[]> subMenu;
  String text;
  boolean isHover = false;

  public ToolbarItem(Runnable r, String text, ToolbarItemColors colors) {
    this(r, text, colors, null);
  }

  public ToolbarItem(Runnable r, String text, ToolbarItemColors colors, Supplier<ToolbarItem[]> submenu) {
    this.text = text;
    this.colors = colors;
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

  public Supplier<ToolbarItem[]> subMenu() {
    return subMenu;
  }

  public void setHover(boolean b) {
    tRect.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
    isHover = b;
  }

  public void setTheme(ToolbarItemColors toolbarItemColors) {
    colors = toolbarItemColors;
    tRect.setColors(
        toolbarItemColors.color,
        toolbarItemColors.bgColor
    );
    if (isHover) setHover(true);
  }
}
