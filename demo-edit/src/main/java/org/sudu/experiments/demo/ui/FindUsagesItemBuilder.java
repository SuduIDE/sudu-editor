package org.sudu.experiments.demo.ui;

import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Supplier;

public class FindUsagesItemBuilder {
    static final FindUsagesItem[] items0 = new FindUsagesItem[0];
    private final ArrayList<FindUsagesItem> list = new ArrayList<>();

    public void addItem(String text, FindUsagesItemColors colors, Runnable r) {
        addItem(new FindUsagesItem(r, text, colors));
    }

    public static FindUsagesItem ti(String text, FindUsagesItemColors colors, Runnable r) {
        return new FindUsagesItem(r, text, colors);
    }

    public void addItem(String text, FindUsagesItemColors colors, Supplier<FindUsagesItem[]> submenu) {
        addItem(new FindUsagesItem(null, text, colors, submenu));
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
