package org.sudu.experiments.demo.ui.window;

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

  public final UiContext context;
  private final TextLineView title;
  private View content = new View();
  private DialogItemColors theme;
  private Consumer<MouseEvent> dragLock;

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
    if (dragLock != null) {
      dragLock.accept(event);
      return true;
    }
    return content.onMouseMove(event);
  }

  boolean onMouseDown(MouseEvent event, int button) {
    if (button == MouseListener.MOUSE_BUTTON_LEFT
        && !title.sizeEmpty() && title.hitTest(event.position))
    {
      if (dragLock == null) dragLock = dragWindow(event.position);
      return true;
    }
    return content.onMouseDown(event, button)
        || content.hitTest(event.position);
  }

  boolean onMouseUp(MouseEvent event, int button) {
    if (button == MouseListener.MOUSE_BUTTON_LEFT && dragLock != null) {
      dragLock = null;
      return true;
    }
    return content.onMouseUp(event, button);
  }

  private Consumer<MouseEvent> dragWindow(V2i mousePos) {
    final int diffCx = content.pos.x - mousePos.x;
    final int diffCy = content.pos.y - mousePos.y;
    final int diffTx = title.pos.x - mousePos.x;
    final int diffTy = title.pos.y - mousePos.y;

    return event -> {
      int visibility = context.toPx(5);

      int mX = Math.min(context.windowSize.x - diffTx - visibility,
          Math.max(visibility - diffTx - title.size.x, event.position.x));

      int mY = Math.min(context.windowSize.y - diffTy - visibility,
          Math.max(visibility - diffTy - title.size.y, event.position.y));

      content.pos.x = mX + diffCx;
      content.pos.y = mY + diffCy;
      setPosition(content.pos, content.size);
    };
  }

  boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return content.onMouseClick(event, button, clickCount);
  }

  boolean onScroll(MouseEvent event, float dX, float dY) {
    return content.onScroll(event, dX, dY);
  }

  void draw(WglGraphics g) {
    content.draw(g);
    title.draw(g, theme);
    drawFrameAndShadow(g);
  }

  private void drawFrameAndShadow(WglGraphics g) {
    g.enableBlend(true);
    int border = context.toPx(2);
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
}
