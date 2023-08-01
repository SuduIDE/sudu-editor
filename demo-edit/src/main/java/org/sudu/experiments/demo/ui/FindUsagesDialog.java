package org.sudu.experiments.demo.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.EditorConst;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.TextRect;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.math.*;

import java.util.Objects;

public class FindUsagesDialog {

  private final DemoRect rect = new DemoRect();
  private final V2i textureSize = new V2i();
  private final V2i v2i = new V2i();
  private ShadowParameters shadowParameters;
  private V4f bgColor;
  private FontDesk font;
  private FindUsagesItem[] items = FindUsagesItemBuilder.items0;
  private GL.Texture texture;
  private int border, textXPad;
  private int hoverItemId = -1;
  private Runnable onClickOutside;
  private int maxFileNameLen = 0;
  private int maxLineLen = 0;
  private int maxCodeContentLen = 0;
  static boolean debug = false;

  public boolean isEmpty() {
    return items.length == 0;
  }

  private static void tRectWarning() {
    Debug.consoleInfo("FindUsages.setPos: tRect.size == 0");
  }

  public boolean goToSelectedItem() {
    if (hoverItemId == -1) {
      return false;
    }
    items[hoverItemId].action.run();
    return true;
  }

  public void setItems(FindUsagesItem[] actions) {
    invalidateTexture();
    items = actions;
  }

  public void onClickOutside(Runnable action) {
    onClickOutside = action;
  }

  private void setBgColor(V4f bgColor) {
    rect.color.set(bgColor);
    this.bgColor = bgColor;
  }

  private void setFrameColor(V4f bgColor) {
    rect.bgColor.set(bgColor);
  }

  public void setFont(FontDesk font) {
    this.font = font;
    invalidateTexture();
  }

  public void dispose() {
    disposeTexture();
    items = FindUsagesItemBuilder.items0;
    hoverItemId = -1;
    rect.makeEmpty();
  }

  private void disposeTexture() {
    texture = Disposable.assign(texture, null);
    textureSize.set(0, 0);
  }

  void measure(UiContext uiContext) {
    Canvas mCanvas = uiContext.mCanvas();
    if (isEmpty()) return;
    Objects.requireNonNull(font);
    mCanvas.setFont(font);
    int textHeight = font.lineHeight(), maxW = 0;
    border = Numbers.iRnd(2 * uiContext.dpr);
    textXPad = Numbers.iRnd(font.WWidth);
    var measureWithPad = RegionTextureAllocator.measuringWithWPad(mCanvas, textXPad);

    RegionTexture regionTexture = new RegionTexture();

    for (FindUsagesItem item : items) {
      // TODO(Minor) Remove this crutch when the scroll appears
      if (item.fileName.startsWith("...")) continue;
      int mFile = measureWithPad.applyAsInt(item.fileName);
      int mLines = measureWithPad.applyAsInt(item.lineNumber);
      int mCodeContent = measureWithPad.applyAsInt(item.codeContent);
      maxFileNameLen = Math.max(maxFileNameLen, mFile);
      maxLineLen = Math.max(maxLineLen, mLines);
      maxCodeContentLen = Math.max(maxCodeContentLen, mCodeContent);
    }
    maxW = maxFileNameLen + maxLineLen + maxCodeContentLen + textXPad * 2;
    for (FindUsagesItem item : items) {
      item.tFiles.textureRegion.set(regionTexture.alloc(item.fileName, measureWithPad, textHeight));
      setCoords(item.tFiles, 0);
      item.tLines.textureRegion.set(regionTexture.alloc(item.lineNumber, measureWithPad, textHeight));
      setCoords(item.tLines, (maxFileNameLen - item.tFiles.size.x));
      item.tContent.textureRegion.set(regionTexture.alloc(item.codeContent, measureWithPad, textHeight));
      setCoords(item.tContent, (maxFileNameLen - item.tFiles.size.x) + (maxLineLen - item.tLines.size.x));
      maxFileNameLen = Math.max(maxFileNameLen, item.tFiles.size.x);
      maxLineLen = Math.max(maxLineLen, item.tLines.size.x);
      maxCodeContentLen = Math.max(maxCodeContentLen, item.tContent.size.x);
    }
    textureSize.set(regionTexture.getTextureSize());
    rect.size.x = maxW + border * 2;
    rect.size.y = (textHeight + border) * items.length + border;
  }

  private void setRectCoords(FindUsagesItem item) {
    item.rectFiles.set(
        item.tFiles.pos.x + item.tFiles.size.x,
        item.tFiles.pos.y,
        maxFileNameLen - item.tFiles.size.x,
        item.tFiles.size.y
    );

    item.rectLines.set(
        item.rectFiles.pos.x + item.rectFiles.size.x + item.tLines.size.x,
        item.tLines.pos.y,
        maxLineLen - item.tLines.size.x,
        item.tLines.size.y
    );

    item.rectContent.set(
        item.rectLines.pos.x + item.rectLines.size.x + item.tContent.size.x,
        item.tContent.pos.y,
        rect.size.x - item.tContent.size.x - maxLineLen - maxFileNameLen - border * 2,
        item.tContent.size.y
    );
  }

  private static void setCoords(TextRect item, float dx) {
    item.pos.x = (int) (item.textureRegion.x + dx);
    item.pos.y = (int) item.textureRegion.y;
    item.size.x = (int) item.textureRegion.z;
    item.size.y = (int) item.textureRegion.w;
  }

  public void setScreenLimitedPosition(int x, int y, V2i screen) {
    V2i size = size();
    x = Math.max(0, Math.min(x, screen.x - size.x));
    y = Math.max(0, Math.min(y, screen.y - size.y));
    setPos(x, y);
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
      tLines.pos.x = x + localX + (maxFileNameLen - tFiles.size.x);
      tLines.pos.y = y + localY;
      tContent.pos.x = x + localX + (maxFileNameLen - tFiles.size.x) + (maxLineLen - tLines.size.x);
      tContent.pos.y = y + localY;
      setRectCoords(item);
      item.rectFiles.pos.x = x + localX + tFiles.size.x;
      item.rectFiles.pos.y = y + localY;
      item.rectLines.pos.x = x + localX + tLines.pos.x + tLines.size.x;
      item.rectLines.pos.y = y + localY;
      item.rectContent.pos.x = x + localX + tContent.pos.x + tContent.size.x;
      item.rectContent.pos.y = y + localY;
      if (tFiles.size.y == 0 || tLines.size.y == 0 || tContent.size.y == 0) tRectWarning();
      localY += tFiles.size.y + border;
    }
  }

  public V2i size() {
    if (textureSize.x == 0 || textureSize.y == 0) {
      throw new RuntimeException("FindUsages size is unknown");
    }
    return rect.size;
  }

  private void renderTexture(WglGraphics g) {
    Canvas canvas = g.createCanvas(textureSize.x + 150, textureSize.y);
    canvas.setFont(font);
    float baseline = font.fAscent - (font.fAscent + font.fDescent) / 16;
    for (FindUsagesItem item : items) {
      canvas.drawText(item.fileName, item.tFiles.textureRegion.x + textXPad, baseline + item.tFiles.textureRegion.y);
      canvas.drawText(item.lineNumber, item.tLines.textureRegion.x + textXPad, baseline + item.tLines.textureRegion.y);
      canvas.drawText(item.codeContent, item.tContent.textureRegion.x + textXPad, baseline + item.tContent.textureRegion.y);
    }
    texture = Disposable.assign(texture, g.createTexture());
    texture.setContent(canvas);
    canvas.dispose();
  }

  public void invalidateTexture() {
    textureSize.set(0, 0);
  }

  public void render(UiContext context) {
    if (items.length == 0) return;
    if (texture == null || textureSize.x * textureSize.y == 0) {
      if (textureSize.x * textureSize.y == 0) measure(context);
      if (textureSize.x * textureSize.y == 0) return;
      renderTexture(context.graphics);
    }

    WglGraphics g = context.graphics;

    g.enableBlend(true);

    if (!rect.isEmpty()) {
      drawFrameAndShadow(g, context);
    }

    for (FindUsagesItem item : items) {
      setRectCoords(item);
      item.tFiles.drawText(g, texture, 0, 0, 2);
      item.tLines.drawText(g, texture, item.tFiles.size.x, 0, 2);
      item.tContent.drawText(g, texture, item.tFiles.size.x + item.tLines.size.x, 0, 2);
      item.rectFiles.draw(g, 0, 0);
      item.rectLines.draw(g, 0, 0);
      item.rectContent.draw(g, 0, 0);
      if (debug) {
        Color.Cvt.fromHSV(1, 1, 1, item.rectFiles.color).setW(0.3f);
        Color.Cvt.fromHSV(0.2, 1, 1, item.rectLines.color).setW(0.3f);
        Color.Cvt.fromHSV(0.5, 1, 1, item.rectContent.color).setW(0.3f);
      }
    }
    for (FindUsagesItem item : items) {
      TextRect tFiles = item.tFiles;
      TextRect tLines = item.tLines;
      TextRect tContent = item.tContent;
      v2i.x = rect.size.x - border * 2 - (tFiles.size.x);
      v2i.y = (tFiles.size.y + tLines.size.y + tContent.size.y);
    }

  }

  private void drawFrameAndShadow(WglGraphics g, UiContext context) {
    int shadowSize = shadowParameters.getShadowSize(context.dpr);

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
    g.drawRect(rect.pos.x + border, rect.pos.y + border, v2i, bgColor);

    // shadow
    v2i.x = rect.size.x;
    v2i.y = shadowSize;
    g.drawRect(rect.pos.x + shadowSize, rect.pos.y + rect.size.y, v2i, shadowParameters.color);
    g.drawRect(rect.pos.x + shadowSize, rect.pos.y + rect.size.y, v2i, shadowParameters.color);
    g.drawRect(rect.pos.x + shadowSize * 2, rect.pos.y + rect.size.y + shadowSize, v2i, shadowParameters.color);

    v2i.x = shadowSize;
    v2i.y = rect.size.y - shadowSize;
    g.drawRect(rect.pos.x + rect.size.x, rect.pos.y + shadowSize, v2i, shadowParameters.color);
    g.drawRect(rect.pos.x + rect.size.x, rect.pos.y + shadowSize, v2i, shadowParameters.color);
    g.drawRect(rect.pos.x + rect.size.x + shadowSize, rect.pos.y + shadowSize * 2, v2i, shadowParameters.color);

  }

  boolean onKeyArrow(int keyCode) {
    if (hoverItemId >= 0) items[hoverItemId].setHover(false);

    switch (keyCode) {
      case KeyCode.ARROW_UP -> {
        hoverItemId = (items.length + hoverItemId - 1) % items.length;
        if (hoverItemId == EditorConst.MAX_SHOW_USAGES_NUMBER)
          hoverItemId = EditorConst.MAX_SHOW_USAGES_NUMBER - 1;
      }
      case KeyCode.ARROW_DOWN -> {
        if (hoverItemId == EditorConst.MAX_SHOW_USAGES_NUMBER - 1)
          hoverItemId = 0;
        else
          hoverItemId = (hoverItemId + 1) % items.length;
      }
      case KeyCode.ARROW_RIGHT -> hoverItemId = Math.min(items.length, EditorConst.MAX_SHOW_USAGES_NUMBER) - 1;
      default -> hoverItemId = 0;
    }
    items[hoverItemId].setHover(true);
    return true;
  }

  public boolean onMouseMove(V2i pos, SetCursor setCursor) {
    boolean inside = rect.isInside(pos);
    int mouseItem = inside ? find(pos) : -1;

    if (hoverItemId != mouseItem) {
      if (mouseItem >= 0) {
        FindUsagesItem newItem = items[mouseItem];
        newItem.setHover(true);
        if (hoverItemId >= 0) {
          FindUsagesItem oldItem = items[hoverItemId];
          oldItem.setHover(false);
        }
        hoverItemId = mouseItem;
      }
    }
    return inside && setCursor.setDefault();
  }

  public boolean onMousePress(V2i pos, int button, boolean press, int clickCount) {
    if (!rect.isInside(pos)) {
      if (press && !rect.isEmpty() && onClickOutside != null) onClickOutside.run();
      return false;
    }
    if (press) {
      int index = find(pos);
      if (index >= 0) {
        FindUsagesItem item = items[index];
        item.action.run();
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
      int x = tRect.pos.x + tRect.size.x;
      int y = tRect.pos.y;
      v2i.x = rect.size.x - border * 2 - tRect.size.x;
      v2i.y = tRect.size.y;
      if (Rect.isInside(pos, x, y, v2i)) {
        return i;
      }

    }
    return -1;
  }

  public void setTheme(DialogItemColors dialogItemColors) {
    shadowParameters = dialogItemColors.shadowParameters;
    setBgColor(dialogItemColors.findUsagesColors.bgColor);
    setFrameColor(dialogItemColors.dialogBorderColor);
    for (int i = 0; i < items.length; i++) {
      items[i].setTheme(dialogItemColors.findUsagesColors);
      if (hoverItemId == i) items[i].setHover(true);
    }
  }
}
