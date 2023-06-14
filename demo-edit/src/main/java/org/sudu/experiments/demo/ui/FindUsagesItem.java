package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.TextRect;
import org.sudu.experiments.math.V2i;

import java.util.function.Supplier;

public class FindUsagesItem {
    final TextRect tRect = new TextRect();
    final Runnable action;
    final FindUsagesItemColors colors;
    final Supplier<FindUsagesItem[]> subMenu;
    String text;

    private boolean isHovered = false;

    public FindUsagesItem(Runnable r, String text, FindUsagesItemColors colors) {
        this(r, text, colors, null);
    }

    public FindUsagesItem(Runnable r, String text, FindUsagesItemColors colors, Supplier<FindUsagesItem[]> submenu) {
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

    public Supplier<FindUsagesItem[]> subMenu() {
        return subMenu;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHover(boolean b) {
        tRect.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
        isHovered = b;
    }
}
