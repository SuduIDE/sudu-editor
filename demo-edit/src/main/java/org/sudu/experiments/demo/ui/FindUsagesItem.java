package org.sudu.experiments.demo.ui;

import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.TextRect;

import java.util.function.Supplier;

public class FindUsagesItem {
    final TextRect tFiles = new TextRect();
    final TextRect tLines = new TextRect();
    final TextRect tContent = new TextRect();
    final Runnable action;
    final FindUsagesItemColors colors;
    final Supplier<FindUsagesItem[]> subMenu;
    String fileName, lineNumber, codeContent;

    private boolean isHovered = false;

    public FindUsagesItem(Runnable r, String fileName, String lineNumber, String codeContent, FindUsagesItemColors colors) {
        this(r, fileName, lineNumber, codeContent, colors, null);
    }

    public FindUsagesItem(Runnable r, String fileName, String lineNumber, String codeContent, FindUsagesItemColors colors, Supplier<FindUsagesItem[]> submenu) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.codeContent = codeContent;
        this.colors = colors;
        action = r;
        tFiles.color.set(colors.fileColor);
        tFiles.bgColor.set(colors.bgColor);
        tLines.color.set(colors.lineColor);
        tLines.bgColor.set(colors.bgColor);
        tContent.color.set(colors.contentColor);
        tContent.bgColor.set(colors.bgColor);
        subMenu = submenu;
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
        tFiles.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
        tLines.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
        tContent.bgColor.set(b ? colors.bgHighlight : colors.bgColor);
        isHovered = b;
    }
}
