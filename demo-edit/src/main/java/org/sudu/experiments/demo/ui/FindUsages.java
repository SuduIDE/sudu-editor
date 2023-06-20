package org.sudu.experiments.demo.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.TextRect;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.math.*;

public class FindUsages {

    // TODO(DELETE) 160 250
    int lineSep = 100;
    int codeContentSep = 180;
    private FontDesk font;
    private final DemoRect rect = new DemoRect();
    private final V2i textureSize = new V2i();
    private final V2i v2i = new V2i();
    private final V4f shadow = new V4f().setW(0.125f);
    private FindUsagesItem[] items = FindUsagesItemBuilder.items0;
    private GL.Texture texture;
    private int border, textXPad;
    private int hoverItem = -1;
    boolean isVertical;

    private Runnable onClickOutside;
    private HoverCallback onEnter;
    private HoverCallback onLeave;

    public interface HoverCallback {
        void event(V2i mouse, int index, FindUsagesItem item);
    }

    public void setItems(FindUsagesItem[] actions) {
        invalidateTexture();
        items = actions;
    }

    public void onClickOutside(Runnable action) {
        onClickOutside = action;
    }

    public void onEnter(HoverCallback callback) {
        onEnter = callback;
    }

    public void onLeave(HoverCallback callback) {
        onLeave = callback;
    }

    public void setBgColor(V4f bgColor) {
        rect.color.set(bgColor);
    }

    public void setFrameColor(V4f bgColor) {
        rect.bgColor.set(bgColor);
    }

    public void setFont(FontDesk font) {
        this.font = font;
        invalidateTexture();
    }

    public void dispose() {
        disposeTexture();
        items = FindUsagesItemBuilder.items0;
        hoverItem = -1;
        rect.makeEmpty();
    }

    private void disposeTexture() {
        texture = Disposable.assign(texture, null);
        textureSize.set(0, 0);
    }

    public void measure(Canvas mCanvas, double devicePR) {
        if (font == null) throw new RuntimeException("FindUsages font has not been set");
        mCanvas.setFont(font);
        int textHeight = font.lineHeight(), maxW = 0;
        border = Numbers.iRnd(2 * devicePR);
        textXPad = Numbers.iRnd(font.WWidth);
        int tw = 0;
        //TODO(DELETE)
        int measureTextExtra = 200;

        for (FindUsagesItem item : items) {
            int m = (int) (measureTextExtra + mCanvas.measureText(item.fileName + item.lineNumber + item.codeContent) + 7.f / 8);
            int mFile = (int) (mCanvas.measureText(item.fileName) + 7.f / 8);
            int mLines = (int) (mCanvas.measureText(item.lineNumber) + 7.f / 8);
            int mCodeContent = (int) (mCanvas.measureText(item.codeContent) + 7.f / 8);

            int wFile = textXPad + mFile;
            int wLines = mLines + textXPad;
            int wCodeContent = mCodeContent + textXPad;
            // TODO(Math.max(maxW, w)) think
            maxW = Math.max(maxW, wFile + wLines + wCodeContent);

            item.tFiles.pos.x = tw;
            item.tFiles.pos.y = 0;
            item.tFiles.size.x = wFile;
            item.tFiles.size.y = textHeight;
            item.tFiles.textureRegion.set(tw, 0, wFile, textHeight);
            item.tLines.pos.x = wFile + textXPad;
            item.tLines.pos.y = 0;
            item.tLines.size.x = wLines;
            item.tLines.size.y = textHeight;
            item.tLines.textureRegion.set(tw + wFile, 0, wLines, textHeight);
            item.tContent.pos.x = tw + wFile + wLines;
            item.tContent.pos.y = 0;
            item.tContent.size.x = wCodeContent;
            item.tContent.size.y = textHeight;
            item.tContent.textureRegion.set(tw + wFile + wLines, 0, wCodeContent, textHeight);
            tw += wFile + wLines + wCodeContent;
        }
        textureSize.x = tw;
        textureSize.y = textHeight;
        rect.size.x = maxW + border * 2;
        rect.size.y = (textHeight + border) * items.length + border;
    }

    public void setPos(int x, int y) {
        rect.pos.set(x, y);
        int localX = border, localY = border;
        for (FindUsagesItem item : items) {
            TextRect tFiles = item.tFiles;
            TextRect tLines = item.tLines;
            TextRect tContent = item.tContent;
            tFiles.pos.x = x + localX;
            tFiles.pos.y = y + localY;
            tLines.pos.x = x + localX;
            tLines.pos.y = y + localY;
            tContent.pos.x = x + localX;
            tContent.pos.y = y + localY;
            if (isVertical) {
                if (tFiles.size.y == 0 || tLines.size.y == 0 || tContent.size.y == 0) tRectWarning();
                localY += tFiles.size.y + border;
            } else {
                if (tFiles.size.x == 0) tRectWarning();
                localX += tFiles.size.x + border;
            }
        }
    }

    private static void tRectWarning() {
        Debug.consoleInfo("FindUsages.setPos: tRect.size == 0");
    }

    public V2i size() {
        if (textureSize.x == 9 && textureSize.y == 0) {
            throw new RuntimeException("FindUsages size is unknown");
        }
        return rect.size;
    }

    private void renderTexture(WglGraphics g) {
        Canvas canvas = g.createCanvas(textureSize.x + 150, textureSize.y);
        canvas.setFont(font);
        float baseline = font.fAscent - (font.fAscent + font.fDescent) / 16;

        for (FindUsagesItem item : items) {
            canvas.drawText(item.fileName, item.tFiles.textureRegion.x + textXPad, baseline);
            canvas.drawText(item.lineNumber, item.tLines.textureRegion.x + textXPad, baseline);
            canvas.drawText(item.codeContent, item.tContent.textureRegion.x + textXPad, baseline);

//            canvas.drawText(item.text, item.tRect.textureRegion.x + textXPad, baseline);
        }
        texture = Disposable.assign(texture, g.createTexture());
        texture.setContent(canvas);
        canvas.dispose();
    }

    public void invalidateTexture() {
        textureSize.set(0, 0);
    }

    public void render(WglGraphics g, double dpr) {
        if (items.length == 0) return;
        if (texture == null || textureSize.x * textureSize.y == 0) {
            if (textureSize.x * textureSize.y == 0) measure(g.mCanvas, dpr);
            if (textureSize.x * textureSize.y == 0) return;
            renderTexture(g);
        }

        if (!rect.isEmpty()) {
            drawFrameAndShadow(g);
        }

        for (FindUsagesItem item : items) {
//            g.drawText(0, 0, item.tRect.size, item.tRect.textureRegion, texture, new Color("#e28720"), new Color("#"))
            item.tFiles.drawText(g, texture, 0, 0, 2);
            item.tLines.drawText(g, texture, item.tFiles.size.x, 0, 2);
            item.tContent.drawText(g, texture, item.tFiles.size.x + item.tLines.size.x, 0, 2);
        }
        if (isVertical) {
            for (FindUsagesItem item : items) {
                TextRect tFiles = item.tFiles;
                TextRect tLines = item.tLines;
                TextRect tContent = item.tContent;
                v2i.x = rect.size.x - border * 2 - (tFiles.size.x);
                v2i.y = (tFiles.size.y + tLines.size.y + tContent.size.y);
                if (v2i.x > 0) {
//                    g.drawRect(tFiles.pos.x + tFiles.size.x, tFiles.pos.y,
//                            v2i, tFiles.bgColor);
                }
            }
        }
    }

    private void drawFrameAndShadow(WglGraphics g) {
        // frame
        v2i.x = rect.size.x;
        v2i.y = border;
        g.drawRect(rect.pos.x, rect.pos.y, v2i, rect.bgColor);
        g.drawRect(rect.pos.x, rect.pos.y + rect.size.y - border, v2i, rect.bgColor);

        v2i.x = border;
        v2i.y = rect.size.y - border - border;
        g.drawRect(rect.pos.x, rect.pos.y + border, v2i, rect.bgColor);
        g.drawRect(rect.pos.x + rect.size.x - border, rect.pos.y + border, v2i, rect.bgColor);

        // body
        v2i.x = rect.size.x - border - border;
        v2i.y = rect.size.y - border - border;
        g.drawRect(rect.pos.x + border, rect.pos.y + border, v2i, rect.color);

        // shadow
        if (isVertical) {
            v2i.x = rect.size.x;
            v2i.y = border;
            g.drawRect(rect.pos.x + border, rect.pos.y + rect.size.y, v2i, shadow);
            g.drawRect(rect.pos.x + border, rect.pos.y + rect.size.y, v2i, shadow);
            g.drawRect(rect.pos.x + border * 2, rect.pos.y + rect.size.y + border, v2i, shadow);

            v2i.x = border;
            v2i.y = rect.size.y - border;
            g.drawRect(rect.pos.x + rect.size.x, rect.pos.y + border, v2i, shadow);
            g.drawRect(rect.pos.x + rect.size.x, rect.pos.y + border, v2i, shadow);
            g.drawRect(rect.pos.x + rect.size.x + border, rect.pos.y + border * 2, v2i, shadow);
        }
    }

    public void onKeyArrow(int keyCode) {
        if(hoverItem >= 0) items[hoverItem].setHover(false);
        int balance = keyCode == KeyCode.ARROW_UP ? -1 : 1;
        hoverItem = (hoverItem + balance + items.length) % items.length;
        items[hoverItem].setHover(true);
    }

    public boolean onMouseMove(V2i pos, SetCursor setCursor) {
        boolean inside = rect.isInside(pos);
        int mouseItem = inside ? find(pos) : -1;

        if (hoverItem != mouseItem) {
            if (mouseItem >= 0) {
                FindUsagesItem newItem = items[mouseItem];
                newItem.setHover(true);
                if (hoverItem >= 0) {
                    FindUsagesItem oldItem = items[hoverItem];
                    oldItem.setHover(false);
                }
                hoverItem = mouseItem;
            }
        }
        return inside && setCursor.setDefault();
    }

    public boolean onMousePress(V2i pos, int button, boolean press, int clickCount) {
        if (!rect.isInside(pos)) {
            if (press && !rect.isEmpty() && onClickOutside != null) onClickOutside.run();
            return false;
        }
        if (clickCount == 1 && press) {
            int index = find(pos);
            if (index >= 0) {
                FindUsagesItem item = items[index];
                if (!item.isSubmenu()) {
                    item.action.run();
                }
            }
        }
        return true;
    }

    private int find(V2i pos) {
        for (int i = 0; i < items.length; i++) {
            FindUsagesItem item = items[i];
            TextRect tRect = item.tFiles;
            if (tRect.isInside(pos)) {
                return i;
            }
            if (isVertical) {
                int x = tRect.pos.x + tRect.size.x;
                int y = tRect.pos.y;
                v2i.x = rect.size.x - border * 2 - tRect.size.x;
                v2i.y = tRect.size.y;
                if (Rect.isInside(pos, x, y, v2i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setLayoutVertical() {
        isVertical = true;
    }

    public int borderSize() {
        return border;
    }
}
