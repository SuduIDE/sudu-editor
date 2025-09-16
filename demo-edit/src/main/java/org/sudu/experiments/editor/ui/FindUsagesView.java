package org.sudu.experiments.editor.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.FindUsagesItemColors;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.*;
import org.sudu.experiments.ui.window.ScrollContent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class FindUsagesView extends ScrollContent implements Focusable {
  static final float hMarinDp = 2;
  static final int spacingDp = 2;

  private final UiContext context;
  private final V2i textureSize = new V2i();
  private UiFont uiFont;
  private FontDesk font;
  private FindUsagesItemData[] items = FindUsagesItemBuilder.items0;
  private FindUsagesItem[] view = new FindUsagesItem[0];
  private GL.Texture texture;
  private int spacing, textXPad;
  private Runnable onClickOutside;
  private int maxFileNameLen = 0;
  private int maxLineLen = 0;
  private int maxCodeContentLen = 0;
  private int firstLineRendered = 0;
  private int lastLineRendered = 0;
  private int hoverItemId;

  RegionTexture regionTexture = new RegionTexture(0);

  private Runnable onClose;
  private DialogItemColors theme;
  private final Map<String, CountedItem<V4f>> fileNameCache = new HashMap<>();

  public FindUsagesView(UiContext context, Runnable onClose) {
    this.context = context;
    this.onClose = onClose;
    onClickOutside(onClose);
  }

  public boolean isEmpty() {
    return items.length == 0;
  }

  public void setItems(FindUsagesItemData[] actions) {
    invalidateTexture();
    items = actions;
  }

  public void invalidateTexture() {
    textureSize.set(0, 0);
  }

  public void onClickOutside(Runnable action) {
    onClickOutside = action;
  }

  public void setTheme(DialogItemColors dialogItemColors, UiFont font) {
    theme = dialogItemColors;
    uiFont = font;
    this.font = null;
    regionTexture = null;
    textXPad = 0;
    invalidateTexture();
  }

  public void dispose() {
    super.dispose();
    disposeTexture();
    fileNameCache.clear();
    regionTexture = null;
    items = FindUsagesItemBuilder.items0;
    view = null;
    maxFileNameLen = 0;
    maxLineLen = 0;
    maxCodeContentLen = 0;
    onClose = null;
  }

  private void disposeTexture() {
    texture = Disposable.assign(texture, null);
    textureSize.set(0, 0);
  }

  @Override
  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    boolean dprChange = dpr != newDpr;
    super.setPosition(newPos, newSize, newDpr);
    spacing = DprUtil.toPx(spacingDp, dpr);
    if (dprChange) {
      font = null;
      textXPad = 0;
      regionTexture = null;
      measure();
    }
  }

  @Override
  public void draw(WglGraphics g) {
    Canvas mCanvas = context.mCanvas();
    if (isEmpty()) return;
    requireFont();
    mCanvas.setFont(font);
    int textHeight = getLineHeight();
    var measureWithPad = RegionTextureAllocator.measuringWithWPad(mCanvas, textXPad);

    int cacheLines = Math.min(
        Numbers.iDivRoundUp(size.y, textHeight),
        items.length
    ) + 30;
    FindUsagesItemColors theme = this.theme.findUsagesColors;

    if (view.length < cacheLines) {
      view = FindUsagesItem.reallocRenderLines(
          cacheLines,
          view,
          firstLineRendered,
          lastLineRendered,
          items,
          regionTexture,
          measureWithPad,
          fileNameCache
      );
      textureSize.set(regionTexture.getTextureSize());
      renderTexture(context.graphics);
    }

    firstLineRendered = getFirstLine();
    lastLineRendered = getLastLine();

    if (view.length == 0) return;
    checkCached(measureWithPad);
    enableScissor(g);

    // background
    V4f bgColor = theme.bgColor;
    g.drawRect(pos.x, pos.y, size, bgColor);

    int x = pos.x;
    int y = pos.y;
    int hMargin = context.toPx(hMarinDp);

    V2i v2i = context.v2i1;

    for (int i = firstLineRendered; i <= lastLineRendered; i++) {
      FindUsagesItem item = itemView(i);
      int localY = i * textHeight + (i + 1) * spacing;
      int fileTX = x + hMargin;
      int lineTX = fileTX + maxFileNameLen;
      int contentTX = lineTX + maxLineLen;
      boolean hover = hoverItemId == i;

      V4f itemBg = hover ? theme.bgCaretColor : bgColor;
      V4f itemFile = hover ? theme.textCaretColor : theme.fileColor;
      V4f itemLine = hover ? theme.textCaretColor : theme.lineColor;
      V4f itemContent = hover ? theme.textCaretColor : theme.contentColor;

      int y1 = y + localY - scrollPos.y;
      g.drawText(fileTX, y1, item.sizeFiles, item.tFiles,
          texture, itemFile, itemBg, context.cleartype);
      g.drawText(lineTX, y1, item.sizeLines, item.tLines,
          texture, itemLine, itemBg, context.cleartype);
      g.drawText(contentTX, y1, item.sizeContent, item.tContent,
          texture, itemContent, itemBg, context.cleartype);

      int fileX = fileTX + item.sizeFiles.x;
      v2i.set(Math.max(0, maxFileNameLen - item.sizeFiles.x), item.sizeFiles.y);
      g.drawRect(fileX, y1, v2i, itemBg);
      int linesX = fileTX + maxFileNameLen + item.sizeLines.x;
      v2i.set(Math.max(0, maxLineLen - item.sizeLines.x), item.sizeLines.y);
      g.drawRect(linesX, y1, v2i, itemBg);
      int contentX = contentTX + item.sizeContent.x;
      v2i.set(Math.max(0, size.x - item.sizeContent.x - maxLineLen - maxFileNameLen - hMargin), item.sizeContent.y);
      g.drawRect(contentX, y1, v2i, itemBg);

      // border
      int borderX = x + size.x - hMargin;
      v2i.set(hMargin, textHeight + spacing);
      g.drawRect(borderX, y1, v2i, bgColor);
    }

    disableScissor(g);
  }

  private void checkCached(ToIntFunction<String> m) {
    boolean rerender = false;
    for (int i = firstLineRendered; i <= lastLineRendered; i++) {
      FindUsagesItem item = itemView(i);
      if (item == null || item.data != items[i]) {
        FindUsagesItem.setNewItem(view, items, regionTexture, m, fileNameCache, i);
        rerender = true;
      }
    }

    if (rerender) {
      textureSize.set(regionTexture.getTextureSize());
      renderTexture(context.graphics);
    }
  }

  private void renderTexture(WglGraphics g) {
    Canvas canvas = g.createCanvas(textureSize.x + 150, textureSize.y, context.cleartype);
    canvas.setFont(font);
    float baseline = font.fAscent - (font.fAscent + font.fDescent) / 16;
    for (var item: view) {
      if (item == null) continue;
      canvas.drawText(item.data.lineNumber, item.tLines.x + textXPad, baseline + item.tLines.y);
      canvas.drawText(item.data.codeContent, item.tContent.x + textXPad, baseline + item.tContent.y);
    }
    fileNameCache.forEach(
        (fileName, v4f) -> canvas.drawText(fileName, v4f.content.x + textXPad, baseline + v4f.content.y)
    );
    if (texture == null) texture = g.createTexture();
    texture.setContent(canvas);
    canvas.dispose();
  }

  private FindUsagesItem itemView(int i) {
    return view[i % view.length];
  }

  public V2i setLimitedPosition(V2i position, int minY) {
    int width = maxFileNameLen + maxLineLen + maxCodeContentLen;
    int xPad = context.toPx(5);
    return new V2i(
        Numbers.clamp(xPad, position.x, context.windowSize.x - width),
        Numbers.clamp(minY, position.y, context.windowSize.y - virtualSize.y)
    );
  }

  public V2i calculateSize(V2i position) {
    measure();
    int height = getLineHeight() * items.length + spacing * (items.length + 1);
    virtualSize.set(size.x, height);
    int width = maxFileNameLen + maxLineLen + maxCodeContentLen;
    int oX = context.windowSize.x - position.x - context.toPx(5);
    int oY = context.windowSize.y - position.y - context.toPx(5);
    return new V2i(Math.min(width, oX), Math.min(virtualSize.y, oY));
  }

  private void measure() {
    if (isEmpty()) return;
    requireFont();
    Canvas mCanvas = context.mCanvas();
    mCanvas.setFont(font);
    var measureWithPad = RegionTextureAllocator.measuringWithWPad(mCanvas, textXPad);

    for (var item: items) {
      int mFile = measureWithPad.applyAsInt(item.fileName);
      int mLines = measureWithPad.applyAsInt(item.lineNumber);
      int mCodeContent = measureWithPad.applyAsInt(item.codeContent);

      maxFileNameLen = Math.max(maxFileNameLen, mFile);
      maxLineLen = Math.max(maxLineLen, mLines);
      maxCodeContentLen = Math.max(maxCodeContentLen, mCodeContent);
    }
  }

  @Override
  public void onMouseMove(MouseEvent event, SetCursor setCursor) {
    if (hitTest(event.position)) {
      int newHover = find(event.position);
      if (newHover >= 0) hoverItemId = newHover;
      context.windowCursor.setDefault();
    }
  }

  @Override
  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (!hitTest(event.position)) {
      if (onClickOutside != null) onClickOutside.run();
    }
    return MouseListener.Static.emptyConsumer;
  }

  @Override
  protected boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (clickCount == 1) {
      int index = find(event.position);
      if (index >= 0) runItem(items[index]);
    }
    return true;
  }

  private void runItem(FindUsagesItemData item) {
    onClose.run();
    item.action.run();
  }

  private int find(V2i pos) {
    if (view.length == 0) return -1;

    int lineHeight = getLineHeight();
    int y = pos.y - this.pos.y + scrollPos.y + spacing;
    int index = y / (lineHeight + spacing);
    if (index >= items.length) return -1;
    return index;
  }

  public int getLineHeight() {
    return font.lineHeight();
  }

  private int getFirstLine() {
    return Math.min((scrollPos.y + spacing) / (getLineHeight() + spacing), items.length - 1);
  }

  private int getLastLine() {
    return Math.min((scrollPos.y + size.y - 1 + spacing) / (getLineHeight() + spacing), items.length - 1);
  }

  private void requireFont() {
    if (font == null) {
      font = context.fontDesk(uiFont);
      regionTexture = new RegionTexture(font.lineHeight());
      textXPad = Numbers.iRnd(font.WWidth);
    }
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    switch (event.keyCode) {
      case KeyCode.ESC -> {
        onClose.run();
        return false;
      }
      case KeyCode.ENTER -> {
        runItem(items[hoverItemId]);
        return false;
      }
      case KeyCode.ARROW_DOWN -> {
        hoverItemId = (hoverItemId + 1) % items.length;
      }
      case KeyCode.ARROW_UP -> {
        hoverItemId = (hoverItemId + items.length - 1) % items.length;
      }
      case KeyCode.ARROW_LEFT, KeyCode.HOME -> {
        hoverItemId = 0;
      }
      case KeyCode.ARROW_RIGHT, KeyCode.END -> {
        hoverItemId = items.length - 1;
      }
      // Page up and down should show the item in the middle of the screen
      case KeyCode.PAGE_UP -> {
        hoverItemId = firstLineRendered;
        scrollView.setScrollPos(scrollPos.x, scrollPos.y - size.y / 2);
        return false;
      }
      case KeyCode.PAGE_DOWN -> {
        hoverItemId = lastLineRendered;
        scrollView.setScrollPos(scrollPos.x, scrollPos.y + size.y / 2);
        return false;
      }

    }
    if (font != null) {
      if (hoverItemId <= firstLineRendered) {
        scrollView.setScrollPos(
            scrollPos.x,
            hoverItemId * getLineHeight() + hoverItemId * spacing
        );
      } else if (hoverItemId >= lastLineRendered) {
        scrollView.setScrollPos(
            scrollPos.x,
            (hoverItemId + 1) * getLineHeight() + (hoverItemId + 2) * spacing - size.y
        );
      }
    } else {
      System.out.println("FindUsagesView.onKeyPress: font is null");
    }
    return false;
  }
}
