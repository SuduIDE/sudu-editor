package org.sudu.experiments.demo.ui.window;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.DialogItemColors;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.demo.ui.UiFont;
import org.sudu.experiments.demo.ui.WindowPaint;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;

public class Window {

  public final UiContext context;
  private final TextLineView title;
  private View content = new View();
  private DialogItemColors theme;

  public Window(UiContext context) {
    this.context = context;
    title = new TextLineView(context);
  }

  public void setTitle(String title, UiFont font) {
    this.title.setTitle(title, font);
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
    int height = title.computeAndSetHeight();
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

  boolean onMouseMove(MouseEvent screenPos) {
    return content.onMouseMove(screenPos);
  }

  boolean onMouseUp(MouseEvent event, int button) {
    return content.onMouseUp(event, button);
  }

  boolean onMouseDown(MouseEvent event, int button) {
    return content.onMouseDown(event, button);
  }

  boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return content.onMouseClick(event, button, clickCount);
  }

  boolean onScroll(MouseEvent event, float dX, float dY) {
    return content.onScroll(event, dX, dY);
  }

  void draw(WglGraphics g) {
    content.draw(g);
    drawFrameAndShadow(g);
    title.draw(g, theme);
  }

  private void drawFrameAndShadow(WglGraphics g) {
    g.enableBlend(true);
    int border = context.toPx(2);
    WindowPaint.drawInnerFrame(g,
        content.size, content.pos,
        theme.dialogBorderColor, -border, context.v2i1);
    WindowPaint.drawShadow(g,
        content.size, content.pos, border,
        theme.shadowParameters.getShadowSize(context.dpr),
        theme.shadowParameters.color, context.v2i1);
  }

  boolean update(double timestamp) {
    return content.update(timestamp);
  }
}
