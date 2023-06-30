package org.sudu.experiments.demo.ui;

import org.sudu.experiments.Canvas;
import org.sudu.experiments.Debug;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.EditorColorScheme;
import org.sudu.experiments.demo.EditorConst;
import org.sudu.experiments.demo.SetCursor;
import org.sudu.experiments.demo.TextRect;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public class FindUsagesDialog {

  private final DemoRect rect = new DemoRect();
  private final V2i textureSize = new V2i();
  private final V2i v2i = new V2i();
  private final V4f shadow = new V4f().setW(0.075f);
  private int shadowSize;
  private V4f bgColor;
  private FontDesk font;
  private FindUsagesItem[] items = FindUsagesItemBuilder.items0;
  private GL.Texture texture;
  private int border, textXPad;
  private int hoverItemId = -1;
  private Runnable onClickOutside;

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

  public void setBgColor(V4f bgColor) {
    rect.color.set(bgColor);
    this.bgColor = bgColor;
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
    hoverItemId = -1;
    rect.makeEmpty();
  }

  private void disposeTexture() {
    texture = Disposable.assign(texture, null);
    textureSize.set(0, 0);
  }

  public void measure(Canvas mCanvas, double devicePR) {
    // TODO(Major): Remove measureText with space
    if (font == null) throw new RuntimeException("FindUsages font has not been set");
    mCanvas.setFont(font);
    int textHeight = font.lineHeight(), maxW = 0;
    border = Numbers.iRnd(2 * devicePR);
    shadowSize = Numbers.iRnd(devicePR);
    textXPad = Numbers.iRnd(font.WWidth);
    int tw = 0;

    int maxFileNameLen = 0;
    int maxLineLen = 0;
    int maxCodeContentLen = 0;
    for (FindUsagesItem item: items) {
      // TODO(Minor) Remove this crutch when the scroll appears
      if (item.fileName.startsWith("...")) continue;
      int mFile = (int) (mCanvas.measureText(item.fileName) + 7.f / 8);
      int mLines = (int) (mCanvas.measureText(item.lineNumber) + 7.f / 8);
      int mCodeContent = (int) (mCanvas.measureText(item.codeContent) + 7.f / 8);
      maxFileNameLen = Math.max(maxFileNameLen, mFile);
      maxLineLen = Math.max(maxLineLen, mLines);
      maxCodeContentLen = Math.max(maxCodeContentLen, mCodeContent);
    }

    for (FindUsagesItem item : items) {
      int wFile = textXPad + maxFileNameLen;
      int wLines = maxLineLen + textXPad;
      int wCodeContent = maxCodeContentLen + textXPad;
      // TODO(Minor) Remove this crutch when the scroll appears
      maxW = Math.max(maxW, wFile + wLines + wCodeContent);
      if (item.fileName.startsWith("...")) {
        wFile = (int) (mCanvas.measureText(item.fileName) + 7.f / 8) + textXPad;
        wLines = maxW - wFile;
        wCodeContent = 0;
      }

      item.tFiles.pos.x = tw;
      item.tFiles.pos.y = 0;
      item.tFiles.size.x = wFile;
      item.tFiles.size.y = textHeight;
      item.tFiles.textureRegion.set(tw, 0, wFile, textHeight);
      item.tLines.pos.x = tw + wFile;
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
      canvas.drawText(item.fileName, item.tFiles.textureRegion.x + textXPad, baseline);
      canvas.drawText(item.lineNumber, item.tLines.textureRegion.x + textXPad, baseline);
      canvas.drawText(item.codeContent, item.tContent.textureRegion.x + textXPad, baseline);
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
      item.tFiles.drawText(g, texture, 0, 0, 2);
      item.tLines.drawText(g, texture, item.tFiles.size.x, 0, 2);
      item.tContent.drawText(g, texture, item.tFiles.size.x + item.tLines.size.x, 0, 2);
    }
    for (FindUsagesItem item : items) {
      TextRect tFiles = item.tFiles;
      TextRect tLines = item.tLines;
      TextRect tContent = item.tContent;
      v2i.x = rect.size.x - border * 2 - (tFiles.size.x);
      v2i.y = (tFiles.size.y + tLines.size.y + tContent.size.y);
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
    g.drawRect(rect.pos.x + border, rect.pos.y + border, v2i, bgColor);

    // shadow
    v2i.x = rect.size.x;
    v2i.y = shadowSize;
    g.drawRect(rect.pos.x + shadowSize, rect.pos.y + rect.size.y, v2i, shadow);
    g.drawRect(rect.pos.x + shadowSize, rect.pos.y + rect.size.y, v2i, shadow);
    g.drawRect(rect.pos.x + shadowSize * 2, rect.pos.y + rect.size.y + shadowSize, v2i, shadow);

    v2i.x = shadowSize;
    v2i.y = rect.size.y - shadowSize;
    g.drawRect(rect.pos.x + rect.size.x, rect.pos.y + shadowSize, v2i, shadow);
    g.drawRect(rect.pos.x + rect.size.x, rect.pos.y + shadowSize, v2i, shadow);
    g.drawRect(rect.pos.x + rect.size.x + shadowSize, rect.pos.y + shadowSize * 2, v2i, shadow);

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
    if (clickCount == 1 && press) {
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

  public void setTheme(EditorColorScheme scheme) {
    setBgColor(scheme.dialogItemColor.findUsagesColors.bgColor);
    setFrameColor(scheme.dialogItemColor.findUsagesColorBorder);
    for (FindUsagesItem item : items) {
      item.setTheme(scheme);
    }
  }

  public interface HoverCallback {
    void event(V2i mouse, int index, FindUsagesItem item);
  }
}
