package org.sudu.experiments.ui;

import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ToolbarItemBuilder {
  static final ToolbarItem[] items0 = new ToolbarItem[0];
  private final ArrayList<ToolbarItem> list = new ArrayList<>();

  public void addItem(String text, Runnable r) {
    addItem(ti(text, r));
  }

  public static ToolbarItem ti(String text, Runnable r) {
    return new ToolbarItem(r, text);
  }

  public void addItem(String text, Supplier<ToolbarItem[]> submenu) {
    addItem(text, submenu, null);
  }

  public void addItem(String text,
                      Supplier<ToolbarItem[]> submenu,
                      Toolbar.HoverCallback onEnter
  ) {
    addItem(new ToolbarItem(null, text, submenu, onEnter));
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
