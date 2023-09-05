package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.ui.colors.ToolbarItemColors;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ToolbarItemBuilder {
  static final ToolbarItem[] items0 = new ToolbarItem[0];
  private final ArrayList<ToolbarItem> list = new ArrayList<>();

  public void addItem(String text, ToolbarItemColors colors, Runnable r) {
    addItem(new ToolbarItem(r, text, colors));
  }

  public static ToolbarItem ti(String text, ToolbarItemColors colors, Runnable r) {
    return new ToolbarItem(r, text, colors);
  }

  public void addItem(String text, ToolbarItemColors colors, Supplier<ToolbarItem[]> submenu) {
    addItem(text, colors, submenu, null);
  }

  public void addItem(String text, ToolbarItemColors colors,
                      Supplier<ToolbarItem[]> submenu, Toolbar.HoverCallback onEnter) {
    addItem(new ToolbarItem(null, text, colors, submenu, onEnter));
  }

  public void addItem(ToolbarItem item) {
    list.add(item);
  }

  public ToolbarItem[] items() {
    return list.toArray(items0);
  }

  public Supplier<ToolbarItem[]> supplier() {
    return ArrayOp.supplier(items());
  }
}
