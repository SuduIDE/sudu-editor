package org.sudu.experiments.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.DemoRect;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.Objects;
import java.util.function.Consumer;

public class Toolbar {
  static final int dpMargin = 3;
  static final int dpBorder = 2;
  static final int textHorizontalMargin = 12;
  static final float textHeightScale = 1.25f;

  private FontDesk font;
  private final DemoRect rect = new DemoRect();
  private final V2i textureSize = new V2i();
  private final V2i v2i = new V2i();
  private ShadowParameters shadowParameters;
  private ToolbarItem[] items = ToolbarItemBuilder.items0;
  private GL.Texture texture;
  private int border, margin, textXPad;
  private int hoverItemId = -1;
  private boolean textureDirty = true;
  boolean isVertical;

  private Runnable onClickOutside;
  private HoverCallback onEnter;
  private HoverCallback onLeave;
  private ToolbarItemColors itemColors;

  public interface HoverCallback {
    void event(V2i mouse, int index, ToolbarItem item);
  }

  public void setItems(ToolbarItem[] actions) {
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

  private void setBgColor(V4f bgColor) {
    rect.color.set(bgColor);
  }

  private void setFrameColor(V4f bgColor) {
    rect.bgColor.set(bgColor);
  }

  public void setFont(FontDesk font, UiContext context) {
    this.font = font;
    measure(context);
    invalidateTexture();
  }

  public void setTheme(DialogItemColors dialogItemColors) {
    shadowParameters = dialogItemColors.shadowParameters;
    setBgColor(dialogItemColors.toolbarItemColors.bgColor);
    setFrameColor(dialogItemColors.windowColors.windowBorderColor);
    itemColors = dialogItemColors.toolbarItemColors;
  }

  void onTextRenderingSettingsChange() {
    invalidateTexture();
  }

  public void dispose() {
    disposeTexture();
    items = ToolbarItemBuilder.items0;
    hoverItemId = -1;
    rect.makeEmpty();
  }

  private void disposeTexture() {
    texture = Disposable.assign(texture, null);
    textureSize.set(0, 0);
  }

  private void measure(UiContext uiContext) {
    Canvas mCanvas = uiContext.mCanvas();
    float devicePR = uiContext.dpr;
    Objects.requireNonNull(font);
    mCanvas.setFont(font);
    int textHeight = font.lineHeight(textHeightScale), maxW = 0;
    border = DprUtil.toPx(dpBorder, devicePR);
    margin = DprUtil.toPx(dpMargin, devicePR);
    textXPad = DprUtil.toPx(textHorizontalMargin, devicePR);
    int tw = 0;
    for (ToolbarItem item : items) {
      int m = mCanvas.measurePx(item.text);
      int w = textXPad + m + textXPad;
      maxW = Math.max(maxW, w);

      item.pos.x = tw;
      item.pos.y = 0;
      item.size.x = w;
      item.size.y = textHeight;
      item.textureRegion.set(tw, 0, w, textHeight);
      tw += w;
    }
    textureSize.x = tw;
    textureSize.y = textHeight;
    rect.size.x = isVertical
        ? maxW + border * 2 + margin * 2
        : tw + border + border * items.length;
    rect.size.y = isVertical
        ? (textHeight + border) * items.length + border + margin * 2
        : textHeight + border * 2;
  }

  public void setPos(int x, int y) {
    rect.pos.set(x, y);
    int localX = border + margin, localY = border + margin;
    for (ToolbarItem item : items) {
      item.pos.x = x + localX;
      item.pos.y = y + localY;
      if (isVertical) {
        if (item.size.y == 0) tRectWarning();
        localY += item.size.y + border;
      } else {
        if (item.size.x == 0) tRectWarning();
        localX += item.size.x + border;
      }
    }
  }

  private static void tRectWarning() {
    Debug.consoleInfo("Toolbar.setPos: tRect.size == 0");
  }

  public V2i size() {
    if (textureSize.x == 0 || textureSize.y == 0) {
      throw new RuntimeException("toolbar size is unknown");
    }
    return rect.size;
  }

  private void renderTexture(WglGraphics g, boolean cleartype) {
    Canvas canvas = g.createCanvas(textureSize.x, textureSize.y, cleartype);
    canvas.setFont(font);
    int textMargin = font.lineHeight(textHeightScale * .5f - .5f);
    float baseline = textMargin + font.fAscent - (font.fAscent + font.fDescent) / 16;

    for (ToolbarItem item : items) {
      canvas.drawText(item.text, item.textureRegion.x + textXPad, baseline);
    }
    texture.setContent(canvas);
    textureDirty = false;
    canvas.dispose();
  }

  public void invalidateTexture() {
    textureDirty = true;
  }

  public void render(UiContext context) {
    WglGraphics g = context.graphics;
    if (items.length == 0) return;
    if (texture == null) texture = g.createTexture();
    if (textureDirty || textureSize.x * textureSize.y == 0) {
      if (textureSize.x * textureSize.y == 0) measure(context);
      if (textureSize.x * textureSize.y == 0) return;
      renderTexture(g, context.cleartype);
    }

    if (!rect.isEmpty()) {
      WindowPaint.drawInnerFrame(g, rect.size, rect.pos, rect.bgColor, border, v2i);
      WindowPaint.drawBody(g, rect.size, rect.pos, rect.color, border, v2i);
      if (isVertical) {
        WindowPaint.drawShadow(g, rect.size, rect.pos, 0, 0,
            shadowParameters.getShadowSize(context.dpr),
            shadowParameters.color, v2i);
      }
    }

    for (ToolbarItem item : items) {
      if (context.cleartype) {
        g.drawTextCT(item.pos.x, item.pos.y, item.size,
            item.textureRegion, texture,
            itemColors.color, itemColors.bgColor(item.hover));
      } else {
        g.drawText(item.pos.x, item.pos.y, item.size,
            item.textureRegion, texture,
            itemColors.color, itemColors.bgColor(item.hover), 0);
      }
    }
    if (isVertical) {
      for (ToolbarItem item : items) {
        v2i.x = rect.size.x - border * 2 - margin * 2 - item.size.x;
        v2i.y = item.size.y;
        if (v2i.x > 0) {
          g.drawRect(item.pos.x + item.size.x, item.pos.y,
              v2i, itemColors.bgColor(item.hover));
        }
      }
    }
  }

  public boolean onMouseMove(V2i pos, SetCursor setCursor) {
    boolean inside = rect.isInside(pos);
    int mouseItem = inside ? find(pos) : -1;

    if (hoverItemId != mouseItem) {
      if (hoverItemId >= 0) {
        ToolbarItem oldItem = items[hoverItemId];
        oldItem.setHover(false);
        if (onLeave != null) onLeave.event(pos, hoverItemId, oldItem);
      }
      if (mouseItem >= 0) {
        ToolbarItem newItem = items[mouseItem];
        if (onEnter != null) onEnter.event(pos, mouseItem, newItem);
        newItem.setHover(true);
      }
      hoverItemId = mouseItem;
    }
    return inside && setCursor.setDefault();
  }

  public Consumer<MouseEvent> onMouseDown(V2i pos, int button) {
    boolean inside = rect.isInside(pos);
    if (!inside) {
      if (!rect.isEmpty() && onClickOutside != null) {
        onClickOutside.run();
      }
    }
    return inside ? MouseListener.Static.emptyConsumer : null;
  }

  public boolean onMouseClick(V2i pos, int button, int clickCount) {
    int index = find(pos);
    if (index >= 0) {
      ToolbarItem item = items[index];
      if (!item.isSubmenu()) {
        item.action.run();
      }
    }
    return true;
  }

  private int find(V2i pos) {
    for (int i = 0; i < items.length; i++) {
      ToolbarItem item = items[i];
      if (item.isInside(pos)) {
        return i;
      }
      if (isVertical) {
        int x = item.pos.x + item.size.x;
        int y = item.pos.y;
        v2i.x = rect.size.x - border * 2 - item.size.x;
        v2i.y = item.size.y;
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

  public int border() {
    return border;
  }

  public int margin() {
    return margin;
  }

}
