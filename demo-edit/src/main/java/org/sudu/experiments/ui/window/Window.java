package org.sudu.experiments.ui.window;

import org.sudu.experiments.Cursor;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.UiFont;
import org.sudu.experiments.ui.WindowPaint;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Window {

  public static final float borderDrawDp = 2;
  public static final float frameHitTestDp = 7;
  public static final float cornerSizeDp = 25;
  public static final float minVisibleDp = 5;
  static final int bypassHitTest = 1;

  public final UiContext context;

  private Predicate<V2i> onContextMenu;
  private Runnable onFocus, onBlur;
  private final TextLineView title;
  private SystemMenu systemMenu;
  private View content;
  private DialogItemColors theme;
  private int flags;

  public Window(UiContext context) {
    this(context, new View());
  }

  public Window(UiContext context, View content) {
    this.context = context;
    title = new TextLineView(context);
    this.content = content;
  }

  public void setOnClose(Runnable onClose) {
    if (systemMenu == null) {
      systemMenu = new SystemMenu();
      if (theme != null) systemMenu.setTheme(theme);
    }
    systemMenu.onClose = onClose;
  }

  public void setContextMenu(Predicate<V2i> onContextMenu) {
    this.onContextMenu = onContextMenu;
  }

  public void onFocus(Runnable h) { onFocus = h; }
  public void onBlur(Runnable h) { onBlur = h; }

  void focus() {
    if (onFocus != null) onFocus.run();
  }

  void blur() {
    if (onBlur != null) onBlur.run();
  }

  public void setTitle(String text) {
    title.setText(text);
    layoutTitle();
  }

  public void setTitleFont(UiFont font, float margin) {
    title.setFont(font, margin);
    layoutTitle();
  }

  public void setTheme(DialogItemColors theme) {
    this.theme = theme;
    setTitleFont(theme.windowTitleFont, theme.windowTitleMargin);
    if (systemMenu != null)
      systemMenu.setTheme(theme);
  }

  public void setBypassHitTest(boolean bypass) {
    if (bypass) {
      flags |= bypassHitTest;
    } else {
      flags &= ~bypassHitTest;
    }
  }

  public boolean bypassHitTest() {
    return (flags & bypassHitTest) != 0;
  }

  public void dispose() {
    title.dispose();
    content = Disposable.assign(content, null);
    if (systemMenu != null) systemMenu.dispose();
  }

  public void setContent(View newContent) {
    V2i pos = content.pos;
    V2i size = content.size;
    content = Disposable.assign(content, newContent);
    content.setPosition(pos, size, context.dpr);
  }

  public void onDprChanged(float oldDpr, float newDpr) {
    content.setPosition(content.pos, content.size, newDpr);
    title.onDprChange();
    layoutTitle();
    if (systemMenu != null)
      systemMenu.onDprChanged();
  }

  public void setPosition(V2i pos, V2i size) {
    content.setPosition(pos, size, context.dpr);
    layoutTitle();
  }

  private void layoutTitle() {
    if (context.dpr == 0 || title.uiFont == null) return;
    title.setWidth(content.size.x);
    title.setDprNoFire(context.dpr);
    int height = title.isEmpty() ? 0 : title.computeAndSetHeight();
    title.pos.set(content.pos.x, content.pos.y - height);
    if (systemMenu != null) systemMenu.pos.set(title.pos);
  }

  public int computeTitleHeight() {
    return title.computeHeight();
  }

  void draw(WglGraphics g) {
    content.draw(g);
    title.draw(g, theme.windowColors);
    if (systemMenu != null) {
      systemMenu.draw(context, title, theme.windowColors);
    }
    drawFrameAndShadow(g);
  }

  private void drawFrameAndShadow(WglGraphics g) {
    g.enableBlend(true);
    int border = context.toPx(borderDrawDp);
    boolean noTitle = title.sizeEmpty();
    V2i temp = context.v2i1;
    V2i size = context.v2i2;
    int titleHeight = noTitle ? 0 : title.size.y;
    size.set(content.size.x, content.size.y + titleHeight);

    WindowPaint.drawInnerFrame(g,
        size, noTitle ? content.pos : title.pos,
        theme.windowColors.windowBorderColor, -border, temp);

    WindowPaint.drawShadow(g,
        content.size, content.pos, border, titleHeight,
        theme.shadowParameters.getShadowSize(context.dpr),
        theme.shadowParameters.color, temp);
  }

  boolean update(double timestamp) {
    return content.update(timestamp);
  }

  public V2i position() {
    return content.pos;
  }

  public V2i size() {
    return content.size;
  }

  public void onHostResize(V2i newSize, float newDpr) {}

  boolean contentHitTest(MouseEvent event) {
    return contentHitTest(event.position);
  }

  private boolean contentHitTest(V2i pos) {
    return bypassHitTest() || content.hitTest(pos);
  }

  boolean onMouseMove(MouseEvent event) {
    return overTitleFrame(event.position) || content.hitTest(event.position)
        && (content.onMouseMove(event) || context.windowCursor.set(null));
  }

  private boolean overTitleFrame(V2i position) {
    if (title.hitTest(position)) {
      if (systemMenu != null)
        systemMenu.onMouseMove(position);
      return context.windowCursor.set(null);
    }
    if (systemMenu != null)
      systemMenu.hover = false;

    int frame = context.toPx(frameHitTestDp);
    int corner = context.toPx(cornerSizeDp);

    if (hHit(position.x, frame)) {
      int hCorner = hCorner(position.x, corner);
      if (topFrameHitTest(position.y, frame))
        return context.windowCursor.set(cur(hCorner, Cursor.ns_resize));
      if (bottomFrameHitTest(position.y, frame))
        return context.windowCursor.set(cur(-hCorner, Cursor.ns_resize));
    }

    if (vHit(position.y, frame)) {
      int vCorner = vCorner(position.y, corner);
      if (leftFrameHitTest(position.x, frame))
        return context.windowCursor.set(cur(vCorner, Cursor.ew_resize));
      if (rightFrameHitTest(position.x, frame))
        return context.windowCursor.set(cur(-vCorner, Cursor.ew_resize));
    }

    return false;
  }

  static String cur(int v, String frameCursor) {
    return v < 0 ? Cursor.nwse_resize :
        v > 0 ? Cursor.nesw_resize : frameCursor;
  }

  Consumer<MouseEvent> onMouseDownFrame(V2i position, int button) {
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      var handler = dragFrameTest(position);
      if (handler != null)
        return handler;

      if (title.hitTest(position)) {
        if (systemMenu != null && systemMenu.hitTest(position)) {
          systemMenu.onClose.run();
          return MouseListener.Static.emptyConsumer;
        } else {
          return dragWindow(position);
        }
      }
    }
    return null;
  }

  Consumer<MouseEvent> onMouseDownContent(MouseEvent event, int button) {
    return content.onMouseDown(event, button);
  }

  boolean onMouseUp(MouseEvent event, int button) {
    return title.hitTest(event.position) ||
        contentHitTest(event) && content.onMouseUp(event, button);
  }

  boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return title.hitTest(event.position) || frameHitTest(event.position) ||
        contentHitTest(event) &&
            content.onMouseClick(event, button, clickCount);
  }

  boolean pickTest(V2i position) {
    return title.hitTest(position)
        || frameHitTest(position)
        || contentHitTest(position);
  }

  boolean onContextMenu(V2i position) {
    return onContextMenu != null && onContextMenu.test(position);
  }

  boolean onScroll(MouseEvent event, float dX, float dY) {
    return content.onScroll(event, dX, dY);
  }

  private boolean leftFrameHitTest(int x, int frame) {
    int left = title.pos.x;
    return left - frame <= x && x < left;
  }

  private boolean rightFrameHitTest(int x, int frame) {
    int right = title.pos.x + title.size.x;
    return right <= x && x < right + frame;
  }

  private boolean topFrameHitTest(int y, int frame) {
    int top = title.pos.y;
    return top - frame <= y && y < top;
  }

  private boolean bottomFrameHitTest(int y, int frame) {
    int bottom = content.pos.y + content.size.y;
    return bottom <= y && y < bottom + frame;
  }

  private boolean vHit(int y, int frame) {
    int top = title.pos.y - frame;
    int bottom = content.pos.y + content.size.y + frame;
    return top <= y && y < bottom;
  }

  private int vCorner(int y, int corner) {
    int top = title.pos.y + corner;
    int bottom = content.pos.y + content.size.y - corner;
    return test(y, top, bottom);
  }

  private boolean hHit(int x, int frame) {
    int left = title.pos.x - frame;
    int right = title.pos.x + title.size.x + frame;
    return left <= x && x < right;
  }

  private int hCorner(int x, int corner) {
    int left = title.pos.x + corner;
    int right = title.pos.x + title.size.x - corner;
    return test(x, left, right);
  }

  private static int test(int t, int tMin, int tMax) {
    if (tMin < tMax) {
      if (t < tMin) return -1;
      if (t > tMax) return 1;
    } else {
      if (t * 2 < tMin + tMax) return -1;
      if (t * 2 > tMin + tMax) return 1;
    }
    return 0;
  }

  private Consumer<MouseEvent> dragFrameTest(V2i position) {
    int frame = context.toPx(frameHitTestDp);
    int corner = context.toPx(cornerSizeDp);

    if (hHit(position.x, frame)) {
      int xMode = hCorner(position.x, corner);
      if (topFrameHitTest(position.y, frame))
        return resize(position, xMode, -1);
      if (bottomFrameHitTest(position.y, frame))
        return resize(position, xMode, 1);
    }

    if (vHit(position.y, frame)) {
      int yMode = vCorner(position.y, corner);
      if (leftFrameHitTest(position.x, frame))
        return resize(position, -1, yMode);
      if (rightFrameHitTest(position.x, frame))
        return resize(position, 1, yMode);
    }

    return null;
  }

  public boolean frameHitTest(V2i position) {
    int frame = context.toPx(frameHitTestDp);
    return hHit(position.x, frame) &&
        (topFrameHitTest(position.y, frame)
            || bottomFrameHitTest(position.y, frame)) ||
        vHit(position.y, frame) &&
            (leftFrameHitTest(position.x, frame)
                || rightFrameHitTest(position.x, frame));
  }

  private Consumer<MouseEvent> resize(V2i mousePos, int xMode, int yMode) {
    final V2i newSize = new V2i();
    final V2i newPos = new V2i();
    final int mouseX = mousePos.x, mouseY = mousePos.y;
    final int posX = content.pos.x, posY = content.pos.y;
    final int sizeX = content.size.x, sizeY = content.size.y;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();

      newSize.set(content.size);
      newPos.set(content.pos);

      switch (xMode) {
        case -1 -> { // left
          int dX = event.position.x - mouseX;
          int newPosX0 = Math.min(posX + dX, context.windowSize.x - visibility);
          int newSizeX = Math.max(minSize.x, posX + sizeX - newPosX0);
          newPos.x = posX + sizeX - newSizeX;
          newSize.x = newSizeX;
        }
        case 1 -> { // right
          newSize.x = Math.max(sizeX + event.position.x - mouseX,
              Math.max(minSize.x, visibility - title.pos.x));
        }
      }

      switch (yMode) {
        case -1 -> { // top
          int dY = event.position.y - mouseY;
          int newPosY0 = Math.max(visibility, Math.min(posY + dY,
              context.windowSize.y + title.size.y - visibility));
          int newSizeY = Math.max(minSize.y, posY + sizeY - newPosY0);

          newPos.y = posY + sizeY - newSizeY;
          newSize.y = newSizeY;
        }
        case 1 -> { // bottom
          newSize.y = Math.max(sizeY + event.position.y - mouseY, minSize.y);
        }
      }

      setPosition(newPos, newSize);
    };
  }

  private Consumer<MouseEvent> dragWindow(V2i mousePos) {
    final int diffCx = content.pos.x - mousePos.x;
    final int diffCy = content.pos.y - mousePos.y;
    final int diffTx = title.pos.x - mousePos.x;
    final int diffTy = title.pos.y - mousePos.y;
    final V2i newPos = new V2i();

    return event -> {
      int visibility = context.toPx(minVisibleDp);

      int mX = Math.min(context.windowSize.x - diffTx - visibility,
          Math.max(visibility - diffTx - title.size.x, event.position.x));

      int mY = Math.min(context.windowSize.y - diffTy - visibility,
          Math.max(visibility - diffTy - title.size.y, event.position.y));
      newPos.set(mX + diffCx, mY + diffCy);
      setPosition(newPos, content.size);
    };
  }

  public int titleHeight() {
    return title.size.y + context.toPx(borderDrawDp);
  }

  public void onTextRenderingSettingsChange() {
    title.onTextRenderingSettingsChange();
    content.onTextRenderingSettingsChange();
  }

  public void close() {
    if (systemMenu == null) throw new NullPointerException();
    systemMenu.onClose.run();
  }
}
