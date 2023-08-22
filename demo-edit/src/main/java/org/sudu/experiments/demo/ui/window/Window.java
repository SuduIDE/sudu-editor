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
  public static final float frameHitTestDp = 5;
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

    int border = context.toPx(frameHitTestDp);
    if (vHit(position.y)) {
      if (leftFrameHitTest(position.x, border))
        return context.windowCursor.set(Cursor.ew_resize);

      if (rightFrameHitTest(position.x, border))
        return context.windowCursor.set(Cursor.ew_resize);
    }
    if (hHit(position.x)) {
      if (topFrameHitTest(position.y, border))
        return context.windowCursor.set(Cursor.ns_resize);

      if (bottomFrameHitTest(position.y, border))
        return context.windowCursor.set(Cursor.ns_resize);
    }
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

  static int id;

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

  private boolean vHit(int y) {
    int top = title.pos.y;
    int bottom = content.pos.y + content.size.y;
    return top <= y && y < bottom;
  }

  private boolean hHit(int x) {
    int left = title.pos.x;
    int right = left + title.size.x;
    return left <= x && x < right;
  }

  private Consumer<MouseEvent> dragHitTest(V2i position) {
    if (!title.sizeEmpty() && title.hitTest(position)) {
      context.windowCursor.set(null);
      return dragWindow(position);
    }

    int frame = context.toPx(frameHitTestDp);
    if (vHit(position.y)) {
      if (leftFrameHitTest(position.x, frame)) {
//        System.out.println("left frame " + ++id);
        return leftResize(position);
      }

      if (rightFrameHitTest(position.x, frame)) {
//        System.out.println("right frame " + ++id);
        return rightResize(position);
      }
    }
    if (hHit(position.x)) {
      if (topFrameHitTest(position.y, frame)) {
//        System.out.println("top frame " + ++id);
        return topResize(position);
      }

      if (bottomFrameHitTest(position.y, frame)) {
//        System.out.println("bottom frame " + ++id);
        return bottomResize(position);
      }
    }
    return null;
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
