package org.sudu.experiments.demo.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.TextRect;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.math.*;

import java.io.Console;

public class FindUsages {

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
        for (FindUsagesItem item : items) {
            int m = (int) (mCanvas.measureText(item.text) + 7.f / 8);
            int w = textXPad + m + textXPad;
            maxW = Math.max(maxW, w);

            item.tRect.pos.x = tw;
            item.tRect.pos.y = 0;
            item.tRect.size.x = w;
            item.tRect.size.y = textHeight;
            item.tRect.textureRegion.set(tw, 0, w, textHeight);
            tw += w;
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
            TextRect tRect = item.tRect;
            tRect.pos.x = x + localX;
            tRect.pos.y = y + localY;
            if (isVertical) {
                if (tRect.size.y == 0) tRectWarning();
                localY += tRect.size.y + border;
            } else {
                if (tRect.size.x == 0) tRectWarning();
                localX += tRect.size.x + border;
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
        Canvas canvas = g.createCanvas(textureSize.x, textureSize.y);
        canvas.setFont(font);
        float baseline = font.fAscent - (font.fAscent + font.fDescent) / 16;

        for (FindUsagesItem item : items) {
            canvas.drawText(item.text, item.tRect.textureRegion.x + textXPad, baseline);
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
            item.tRect.drawText(g, texture, 0, 0, 0);
        }
        if (isVertical) {
            for (FindUsagesItem item : items) {
                TextRect tRect = item.tRect;
                v2i.x = rect.size.x - border * 2 - tRect.size.x;
                v2i.y = tRect.size.y;
                if (v2i.x > 0) {
                    g.drawRect(tRect.pos.x + tRect.size.x, tRect.pos.y,
                            v2i, tRect.bgColor);
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
            TextRect tRect = item.tRect;
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
