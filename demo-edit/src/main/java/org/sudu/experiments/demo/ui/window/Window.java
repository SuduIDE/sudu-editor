package org.sudu.experiments.demo.ui.window;

import org.sudu.experiments.Cursor;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.colors.DialogItemColors;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.demo.ui.UiFont;
import org.sudu.experiments.demo.ui.WindowPaint;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;

public class Window {

  public static final float borderDrawDp = 2;
  public static final float frameHitTestDp = 7;
  public static final float cornerSizeDp = 25;
  public static final float minVisibleDp = 5;

  public final UiContext context;
  private final TextLineView title;
  private View content = new View();
  private DialogItemColors theme;

  public Window(UiContext context) {
    this.context = context;
    title = new TextLineView(context);
  }

  public void setTitle(String title, UiFont font, float margin) {
    this.title.setText(title, font, margin);
    layoutTitle();
  }

  public void setTheme(DialogItemColors theme) {
    this.theme = theme;
  }

  public void dispose() {
    title.dispose();
    content = Disposable.assign(content, null);
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
  }

  public void setPosition(V2i pos, V2i size) {
    content.setPosition(pos, size, content.dpr);
    layoutTitle();
  }

  private void layoutTitle() {
    title.setWidth(content.size.x);
    title.setDprNoFire(context.dpr);
    int height = title.isEmpty() ? 0 : title.computeAndSetHeight();
    title.pos.set(content.pos.x, content.pos.y - height);
  }

  void draw(WglGraphics g) {
    content.draw(g);
    title.draw(g, theme.windowColors);
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

  boolean onMouseMove(MouseEvent event) {
    return setCursor(event.position) || content.hitTest(event.position)
        && (content.onMouseMove(event) || context.windowCursor.set(null));
  }

  private boolean setCursor(V2i position) {
    if (title.hitTest(position))
      return context.windowCursor.set(null);

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

  Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      var handler = dragHitTest(event.position);
      if (handler != null) {
        return handler;
      }
    }
    var handler = title.hitTest(event.position)
        ? MouseListener.Static.emptyConsumer : content.onMouseDown(event, button);
    if (content == null) return null;
    return handler != null ? handler : content.hitTest(event.position)
        ? MouseListener.Static.emptyConsumer : null;
  }

  boolean onMouseUp(MouseEvent event, int button) {
    return title.hitTest(event.position) ||
        content.hitTest(event.position) && content.onMouseUp(event, button);
  }

  boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return title.hitTest(event.position) ||
        content.hitTest(event.position) &&
        content.onMouseClick(event, button, clickCount);
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

  private Consumer<MouseEvent> dragHitTest(V2i position) {
    if (!title.sizeEmpty() && title.hitTest(position)) {
      return dragWindow(position);
    }

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
}
