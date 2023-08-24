package org.sudu.experiments.demo.ui.window;

import org.sudu.experiments.Cursor;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.DialogItemColors;
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
    title.draw(g, theme);
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
        theme.dialogBorderColor, -border, temp);

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

  public boolean hitTest(V2i point) {
    return content.hitTest(point);
  }

  boolean onMouseMove(MouseEvent event) {
    return setCursor(event.position) || content.hitTest(event.position)
        && (content.onMouseMove(event) || context.windowCursor.set(null));
  }

  private boolean setCursor(V2i position) {
    if (title.hitTest(position))
      return context.windowCursor.set(null);

    int frame = context.toPx(frameHitTestDp);
    int corner = context.toPx(cornerSizeDp);

    boolean vHit = vHit(position.y, frame);
    boolean hHit = hHit(position.x, frame);
    int hCorner = hCorner(position.x, corner);
    int vCorner = vCorner(position.y, corner);
    boolean topFrame = hHit && topFrameHitTest(position.y, frame);
    boolean leftFrame = vHit && leftFrameHitTest(position.x, frame);
    boolean rightFrame = vHit && rightFrameHitTest(position.x, frame);
    boolean bottomFrame = hHit && bottomFrameHitTest(position.y, frame);

    if (topFrame) return context.windowCursor.set(
        hCorner < 0 ? Cursor.nwse_resize :
            hCorner > 0 ? Cursor.nesw_resize : Cursor.ns_resize);

    if (leftFrame) return context.windowCursor.set(
        vCorner < 0 ? Cursor.nwse_resize :
            vCorner > 0 ? Cursor.nesw_resize : Cursor.ew_resize);

    if (rightFrame) return context.windowCursor.set(
        vCorner < 0 ? Cursor.nesw_resize :
            vCorner > 0 ? Cursor.nwse_resize : Cursor.ew_resize);

    if (bottomFrame) return context.windowCursor.set(
        hCorner < 0 ? Cursor.nesw_resize :
            hCorner > 0 ? Cursor.nwse_resize : Cursor.ns_resize);
    return false;
  }

  Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      var handler = dragHitTest(event.position);
      if (handler != null) {
        return handler;
      }
    }
    var handler = content.onMouseDown(event, button);
    return handler != null ? handler : content.hitTest(event.position)
        ? View.Static.emptyConsumer : null;
  }

  boolean onMouseUp(MouseEvent event, int button) {
    return content.onMouseUp(event, button);
  }

  boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return content.hitTest(event.position) &&
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
    boolean vHit = vHit(position.y, frame);
    boolean hHit = hHit(position.x, frame);
    int hCorner = hCorner(position.x, corner);
    int vCorner = vCorner(position.y, corner);
    boolean topFrame = hHit && topFrameHitTest(position.y, frame);
    boolean leftFrame = vHit && leftFrameHitTest(position.x, frame);
    boolean rightFrame = vHit && rightFrameHitTest(position.x, frame);
    boolean bottomFrame = hHit && bottomFrameHitTest(position.y, frame);

    if (topFrame) return hCorner < 0 ? nwResize(position)
        : hCorner > 0 ? neResize(position) : topResize(position);
    if (leftFrame) return vCorner < 0 ? nwResize(position)
        : vCorner > 0 ? swResize(position) : leftResize(position);
    if (rightFrame) return vCorner < 0 ? neResize(position)
        : vCorner > 0 ? seResize(position) : rightResize(position);
    if (bottomFrame) return hCorner < 0 ? swResize(position)
        : hCorner > 0 ? seResize(position) : bottomResize(position);
    return null;
  }

    private Consumer<MouseEvent> nwResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final V2i newPos = new V2i();
    final int mouseX = mousePos.x, mouseY = mousePos.y;
    final int posY = content.pos.y, posX = content.pos.x;
    final int sizeY = content.size.y, sizeX = content.size.x;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();

      // y top
      int dY = event.position.y - mouseY;
      int newPosY0 = Math.min(posY + dY,
          context.windowSize.y + title.size.y - visibility);
      int newSizeY0 = posY + sizeY - newPosY0;
      int newSizeY = Math.max(minSize.y, newSizeY0);
      int newPosY = posY + sizeY - newSizeY;

      // x left
      int dX = event.position.x - mouseX;
      int newPosX0 = Math.min(posX + dX, context.windowSize.x - visibility);
      int newSizeX0 = posX + sizeX - newPosX0;
      int newSizeX = Math.max(minSize.x, newSizeX0);
      int newPosX = posX + sizeX - newSizeX;

      newPos.set(newPosX, newPosY);
      newSize.set(newSizeX, newSizeY);
      setPosition(newPos, newSize);
    };
  }

  private Consumer<MouseEvent> neResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final V2i newPos = new V2i();
    final int mouseX = mousePos.x, mouseY = mousePos.y;
    final int posY = content.pos.y, posX = content.pos.x;
    final int sizeY = content.size.y, sizeX = content.size.x;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();

      // y top
      int dY = event.position.y - mouseY;
      int newPosY0 = Math.min(posY + dY,
          context.windowSize.y + title.size.y - visibility);
      int newSizeY0 = posY + sizeY - newPosY0;
      int newSizeY = Math.max(minSize.y, newSizeY0);
      int newPosY = posY + sizeY - newSizeY;

      // x right
      int dX = event.position.x - mouseX;
      int newSizeX = Math.max(sizeX + dX,
          Math.max(minSize.x, visibility - title.pos.x));

      // top  right
      newPos.set(content.pos.x, newPosY);
      newSize.set(newSizeX, newSizeY);

      setPosition(newPos, newSize);
    };
  }

  private Consumer<MouseEvent> seResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final int mouseX = mousePos.x, mouseY = mousePos.y;
    final int sizeX = content.size.x, sizeY = content.size.y;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();

      int dX = event.position.x - mouseX;
      int newSizeX = Math.max(sizeX + dX,
          Math.max(minSize.x, visibility - title.pos.x));

      int dY = event.position.y - mouseY;
      int newSizeY = Math.max(sizeY + dY, minSize.y);

      newSize.set(newSizeX, newSizeY);
      setPosition(content.pos, newSize);
    };
  }

  private Consumer<MouseEvent> swResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final V2i newPos = new V2i();
    final int mouseX = mousePos.x, mouseY = mousePos.y;
    final int sizeX = content.size.x, sizeY = content.size.y;
    final int posX = content.pos.x;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();

      // left
      int dX = event.position.x - mouseX;
      int newPosX0 = Math.min(posX + dX, context.windowSize.x - visibility);
      int newSizeX0 = posX + sizeX - newPosX0;
      int newSizeX = Math.max(minSize.x, newSizeX0);
      int newPosX = posX + sizeX - newSizeX;

      // bottom
      int dY = event.position.y - mouseY;
      int newSizeY = Math.max(sizeY + dY, minSize.y);

      newPos.set(newPosX, content.pos.y);
      newSize.set(newSizeX, newSizeY);
      setPosition(newPos, newSize);
    };
  }

  private Consumer<MouseEvent> topResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final V2i newPos = new V2i();
    final int mouseY = mousePos.y;
    final int posY = content.pos.y;
    final int sizeY = content.size.y;
    return event -> {
      int visibility = context.toPx(minVisibleDp);

      V2i minSize = content.minimalSize();
      int dY = event.position.y - mouseY;
      int newPosY0 = Math.min(posY + dY,
          context.windowSize.y + title.size.y - visibility);
      int newSizeY0 = posY + sizeY - newPosY0;
      int newSizeY = Math.max(minSize.y, newSizeY0);
      newPos.set(content.pos.x, posY + sizeY - newSizeY);
      newSize.set(content.size.x, newSizeY);
      setPosition(newPos, newSize);
    };
  }

  private Consumer<MouseEvent> bottomResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final int mouseY = mousePos.y;
    final int sizeY = content.size.y;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();
      int dY = event.position.y - mouseY;
      int newSizeY = Math.max(sizeY + dY, minSize.y);
      newSize.set(content.size.x, newSizeY);
      setPosition(content.pos, newSize);
    };
  }

  private Consumer<MouseEvent> leftResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final V2i newPos = new V2i();
    final int mouseX = mousePos.x;
    final int posX = content.pos.x;
    final int sizeX = content.size.x;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();

      int dX = event.position.x - mouseX;
      int newPosX0 = Math.min(posX + dX, context.windowSize.x - visibility);
      int newSizeX0 = posX + sizeX - newPosX0;
      int newSizeX = Math.max(minSize.x, newSizeX0);

      newPos.set(posX + sizeX - newSizeX, content.pos.y);
      newSize.set(newSizeX, content.size.y);
      setPosition(newPos, newSize);
    };
  }

  private Consumer<MouseEvent> rightResize(V2i mousePos) {
    final V2i newSize = new V2i();
    final int mouseX = mousePos.x;
    final int sizeX = content.size.x;
    return event -> {
      int visibility = context.toPx(minVisibleDp);
      V2i minSize = content.minimalSize();

      int dX = event.position.x - mouseX;
      int newSizeX = Math.max(sizeX + dX,
          Math.max(minSize.x, visibility - title.pos.x));

      newSize.set(newSizeX, content.size.y);
      setPosition(content.pos, newSize);
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

}
