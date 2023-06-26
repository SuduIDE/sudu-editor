package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Supplier;

public class FindUsagesItemBuilder {
  static final FindUsagesItem[] items0 = new FindUsagesItem[0];
  private final ArrayList<FindUsagesItem> list = new ArrayList<>();

  // TODO(Minor): Move runnable from item to window
  public void addItem(String fileName, String lineNumber, String codeContent, FindUsagesItemColors colors, Runnable r) {
    addItem(new FindUsagesItem(r, fileName, lineNumber, codeContent, colors));
  }

  public void addItem(FindUsagesItem item) {
    list.add(item);
  }

  public FindUsagesItem[] items() {
    return list.toArray(items0);
  }

  public Supplier<FindUsagesItem[]> supplier() {
    return ArrayOp.supplier(items());
  }
}
