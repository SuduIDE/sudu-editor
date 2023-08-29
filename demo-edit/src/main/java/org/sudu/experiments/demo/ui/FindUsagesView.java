package org.sudu.experiments.demo.ui;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.DemoRect;
import org.sudu.experiments.demo.ui.window.ScrollContent;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.Objects;
import java.util.function.Consumer;

public class FindUsagesView extends ScrollContent implements Focusable {
  private final UiContext context;
  private final DemoRect rect = new DemoRect();
  private final V2i textureSize = new V2i();
  private final V2i v2i = new V2i();
  private UiFont uiFont;
  private FontDesk font;
  private FindUsagesItemData[] items = FindUsagesItemBuilder.items0;
  private FindUsagesItem[] view = new FindUsagesItem[0];
  private GL.Texture texture;
  private int border, textXPad;
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

  public FindUsagesView(UiContext context, Runnable onClose) {
    this.context = context;
    this.onClose = onClose;
    onClickOutside(onClose);
    this.border = DprUtil.toPx(2, context.dpr);
  }

  public boolean isEmpty() {
    return items.length == 0;
  }

  public void setItems(FindUsagesItemData[] actions) {
    invalidateTexture();
    items = actions;
  }

  public void setTheme(DialogItemColors dialogItemColors) {
    theme = dialogItemColors;
  }

  public void invalidateTexture() {
    textureSize.set(0, 0);
  }

  public void onClickOutside(Runnable action) {
    onClickOutside = action;
  }

  public void setFont(UiFont font) {
    uiFont = font;
    this.font = context.fontDesk(uiFont);
    regionTexture = new RegionTexture(this.font.lineHeight());
    textXPad = Numbers.iRnd(this.font.WWidth);
    invalidateTexture();
  }

  public void dispose() {
    super.dispose();
    disposeTexture();
    items = FindUsagesItemBuilder.items0;
    view = null;
    maxFileNameLen = 0;
    maxLineLen = 0;
    maxCodeContentLen = 0;
    rect.makeEmpty();
    onClose = null;
  }

  private void disposeTexture() {
    texture = Disposable.assign(texture, null);
    textureSize.set(0, 0);
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    measure();
    this.font = context.fontDesk(uiFont);
    border = DprUtil.toPx(2, context.dpr);
    regionTexture = new RegionTexture(this.font.lineHeight());
    textXPad = Numbers.iRnd(this.font.WWidth);
  }

  @Override
  public void setScrollPosY(int y) {
    super.setScrollPosY(y);
  }

  @Override
  protected void draw(WglGraphics g) {
    Canvas mCanvas = context.mCanvas();
    if (isEmpty()) return;
    Objects.requireNonNull(font);
    mCanvas.setFont(font);
    int textHeight = font.lineHeight();
    var measureWithPad = RegionTextureAllocator.measuringWithWPad(mCanvas, textXPad);

    int cacheLines = Math.min(
        Numbers.iDivRoundUp(rect.size.y, textHeight),
        items.length
    ) + 30;
    boolean realloc = false;
    firstLineRendered = getFirstLine();
    lastLineRendered = getLastLine();
    if (view.length > 0) {
      for (int i = firstLineRendered; i <= lastLineRendered; i++) {
        FindUsagesItem item = itemView(i);
        if (item == null || item.data != items[i]) {
          realloc = true;
          break;
        }
      }
    } else realloc = true;

    FindUsagesItemColors theme = this.theme.findUsagesColors;
    if (realloc) {
      view = FindUsagesItem.reallocRenderLines(
          cacheLines,
          view,
          firstLineRendered,
          lastLineRendered,
          items,
          regionTexture,
          theme,
          measureWithPad
      );
      textureSize.set(regionTexture.getTextureSize());
      if (textureSize.x * textureSize.y == 0) return;
      renderTexture(context.graphics);
    }

    if (view.length == 0) return;
    enableScissor(g);

    // background
      V4f bgColor = theme.bgColor;
      g.drawRect(pos.x, pos.y, size, bgColor);

    int localX = border;
    int x = rect.pos.x;
    int y = rect.pos.y;

    for (int i = firstLineRendered; i <= lastLineRendered; i++) {
      FindUsagesItem item = itemView(i);
      int localY = i * getLineHeight() + (i + 1) * border;
      int fileTX = x + localX;
      int lineTX = fileTX + maxFileNameLen;
      int contentTX = lineTX + maxLineLen;
      boolean hover = hoverItemId == i;

      V4f itemBg = hover ? theme.bgHighlightColor : bgColor;
      V4f itemFile = hover ? theme.textHighlightColor : theme.fileColor;
      V4f itemLine = hover ? theme.textHighlightColor : theme.lineColor;
      V4f itemContent = hover ? theme.textHighlightColor : theme.contentColor;

      int y1 = y + localY - scrollPos.y;
      g.drawText(fileTX, y1, item.sizeFiles, item.tFiles,
          texture, itemFile, itemBg, 0);
      g.drawText(lineTX, y1, item.sizeLines, item.tLines,
          texture, itemLine, itemBg, 0);
      g.drawText(contentTX, y1, item.sizeContent, item.tContent,
          texture, itemContent, itemBg, 0);

      int fileX = fileTX + item.sizeFiles.x;
      v2i.set(Math.max(0, maxFileNameLen - item.sizeFiles.x), item.sizeFiles.y);
      g.drawRect(fileX, y1, v2i, itemBg);
      int linesX = fileTX + maxFileNameLen + item.sizeLines.x;
      v2i.set(Math.max(0, maxLineLen - item.sizeLines.x), item.sizeLines.y);
      g.drawRect(linesX, y1, v2i, itemBg);
      int contentX = contentTX + item.sizeContent.x;
      v2i.set(Math.max(0, rect.size.x - item.sizeContent.x - maxLineLen - maxFileNameLen - border * 2), item.sizeContent.y);
      g.drawRect(contentX, y1, v2i, itemBg);
    }

    disableScissor(g);
  }

  private void renderTexture(WglGraphics g) {
    Canvas canvas = g.createCanvas(textureSize.x + 150, textureSize.y);
    canvas.setFont(font);
    float baseline = font.fAscent - (font.fAscent + font.fDescent) / 16;
    for (var item: view) {
      if (item == null) continue;
      canvas.drawText(item.data.fileName, item.tFiles.x + textXPad, baseline + item.tFiles.y);
      canvas.drawText(item.data.lineNumber, item.tLines.x + textXPad, baseline + item.tLines.y);
      canvas.drawText(item.data.codeContent, item.tContent.x + textXPad, baseline + item.tContent.y);
    }
    texture = Disposable.assign(texture, g.createTexture());
    texture.setContent(canvas);
    canvas.dispose();
  }

  private FindUsagesItem itemView(int i) {
    return view[i % view.length];
  }

  @Override
  protected void onSizeChange(V2i newSize) {
    rect.size.set(newSize);
  }

  @Override
  protected void onPosChange(V2i newPos) {
    rect.pos.set(newPos);
  }

  @Override
  protected void updateVirtualSize() {
    // TODO: compute font
    int height = getLineHeight() * items.length + border * (items.length + 1);
    virtualSize.set(size.x, height);
  }

  public V2i setLimitedPosition(V2i position, int minY) {
    int width = maxFileNameLen + maxLineLen + maxCodeContentLen;
    return new V2i(
        Numbers.clamp(0, position.x, context.windowSize.x - width),
        Numbers.clamp(minY, position.y, context.windowSize.y - virtualSize.y)
    );
  }

  public V2i calculateSize(V2i position) {
    measure();
    updateVirtualSize();
    int width = maxFileNameLen + maxLineLen + maxCodeContentLen;
    int oX = context.windowSize.x - position.x - context.toPx(5);
    int oY = context.windowSize.y - position.y - context.toPx(5);
    return new V2i(Math.min(width, oX), Math.min(virtualSize.y, oY));
  }

  private void measure() {
    Canvas mCanvas = context.mCanvas();
    if (isEmpty()) return;
    Objects.requireNonNull(font);
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
  public boolean onMouseMove(MouseEvent event) {
    int newHover = find(event.position);
    if (newHover >= 0) hoverItemId = newHover;
    return rect.isInside(pos) && context.windowCursor.setDefault();
  }

  @Override
  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (!rect.isInside(event.position)) {
      if (!rect.isEmpty() && onClickOutside != null) onClickOutside.run();
    }
    return null;
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
    int y = pos.y - this.pos.y + scrollPos.y + border;
    int index = y / (lineHeight + border);
    if (index >= items.length) return -1;
    return index;
  }

  public int getLineHeight() {
    return font.lineHeight();
  }

  private int getFirstLine() {
    return Math.min((scrollPos.y + border) / (getLineHeight() + border), items.length - 1);
  }

  private int getLastLine() {
    return Math.min((scrollPos.y + rect.size.y - 1 + border) / (getLineHeight() + border), items.length - 1);
  }

  @Override
  public boolean onKeyPress(KeyEvent event) {
    switch (event.keyCode) {
      case KeyCode.ESC -> onClose.run();
      case KeyCode.ENTER -> runItem(items[hoverItemId]);

      case KeyCode.ARROW_DOWN ->
          hoverItemId = (hoverItemId + 1) % items.length;
      case KeyCode.ARROW_UP ->
          hoverItemId = (hoverItemId + items.length - 1) % items.length;

      case KeyCode.ARROW_LEFT -> hoverItemId = 0;
      case KeyCode.ARROW_RIGHT -> hoverItemId = items.length - 1;
    }
    return false;
  }
}
