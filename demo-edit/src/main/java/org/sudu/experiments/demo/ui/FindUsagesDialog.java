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
  // TODO(Major) Remove when the scroll appears
  private int tailContentLen = 0;
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
    maxFileNameLen = 0;
    maxLineLen = 0;
    maxCodeContentLen = 0;
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
    border = DprUtil.toPx(2, uiContext.dpr);
    textXPad = Numbers.iRnd(font.WWidth);
    var measureWithPad = RegionTextureAllocator.measuringWithWPad(mCanvas, textXPad);

    RegionTexture regionTexture = new RegionTexture(textHeight);

    for (FindUsagesItem item : items) {
      int mFile = measureWithPad.applyAsInt(item.fileName);
      // TODO(Major) Remove this crutch when the scroll appears
      if (item.fileName.startsWith("...")) {
        tailContentLen = mFile;
        continue;
      }
      int mLines = measureWithPad.applyAsInt(item.lineNumber);
      int mCodeContent = measureWithPad.applyAsInt(item.codeContent);
      maxFileNameLen = Math.max(maxFileNameLen, mFile);
      maxLineLen = Math.max(maxLineLen, mLines);
      maxCodeContentLen = Math.max(maxCodeContentLen, mCodeContent);
    }
    maxW = maxFileNameLen + maxLineLen + maxCodeContentLen + textXPad * 2;
    for (FindUsagesItem item : items) {
      item.tFiles.textureRegion.set(regionTexture.alloc(item.fileName, measureWithPad));
      setCoords(item.tFiles, 0);
      // TODO(Major) Remove this crutch when the scroll appears
      if (item.fileName.startsWith("...")) continue;
      item.tLines.textureRegion.set(regionTexture.alloc(item.lineNumber, measureWithPad));
      setCoords(item.tLines, (maxFileNameLen - item.tFiles.size.x));
      item.tContent.textureRegion.set(regionTexture.alloc(item.codeContent, measureWithPad));
      setCoords(item.tContent, (maxFileNameLen - item.tFiles.size.x) + (maxLineLen - item.tLines.size.x));
      maxFileNameLen = Math.max(maxFileNameLen, item.tFiles.size.x);
      maxLineLen = Math.max(maxLineLen, item.tLines.size.x);
      maxCodeContentLen = Math.max(maxCodeContentLen, item.tContent.size.x);
    }
    textureSize.set(regionTexture.getTextureSize());
    // TODO(Major) Remove `Math.max(..., tailContentLen)` when the scroll appears
    rect.size.x = Math.max(maxW, tailContentLen) + border * 2;
    rect.size.y = (textHeight + border) * items.length + border;
  }

  private void setRectCoords(FindUsagesItem item) {
    // TODO(Major) Remove this crutch when the scroll appears
    if (item.fileName.startsWith("...")) return;
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
      // TODO(Major) Remove this crutch when the scroll appears
      if (!item.fileName.startsWith("...")) {
        setRectCoords(item);
        item.rectFiles.pos.x = x + localX + tFiles.size.x;
        item.rectFiles.pos.y = y + localY;
        item.rectLines.pos.x = x + localX + tLines.pos.x + tLines.size.x;
        item.rectLines.pos.y = y + localY;
        item.rectContent.pos.x = x + localX + tContent.pos.x + tContent.size.x;
        item.rectContent.pos.y = y + localY;
      }
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
      WindowPaint.drawInnerFrame(g, rect.size, rect.pos, rect.bgColor, border, v2i);
      WindowPaint.drawBody(g, rect.size, rect.pos, rect.color, border, v2i);
      WindowPaint.drawShadow(g, rect.size, rect.pos, 0, 0,
          shadowParameters.getShadowSize(context.dpr),
          shadowParameters.color, v2i);
    }

    for (FindUsagesItem item : items) {
      setRectCoords(item);
      item.tFiles.drawText(g, texture, 0, 0, 1);
      item.tLines.drawText(g, texture, item.tFiles.size.x, 0, 1);
      item.tContent.drawText(g, texture, item.tFiles.size.x + item.tLines.size.x, 0, 1);
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

  public boolean onMouseClick(V2i pos, int button, int clickCount) {
    if (!rect.isInside(pos)) {
      if (!rect.isEmpty() && onClickOutside != null) onClickOutside.run();
      return false;
    }
    if (clickCount == 1) {
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
