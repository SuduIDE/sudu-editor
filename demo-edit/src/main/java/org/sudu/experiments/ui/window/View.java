package org.sudu.experiments.ui.window;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.DprUtil;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public class View implements Disposable {
  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public float dpr;

  public void dispose() {}

  public V2i minimalSize() {
    return new V2i(0,0);
  }

  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    if (!pos.equals(newPos)) {
      onPosChange(newPos);
      pos.set(newPos);
    }
    if (!size.equals(newSize)) {
      onSizeChange(newSize);
      size.set(newSize);
    }
    if (dpr != newDpr) onDprChange(dpr, dpr = newDpr);
  }

  public void setDprNoFire(float newDpr) {
    dpr = newDpr;
  }

  public int toPx(float value) {
    return DprUtil.toPx(value, dpr);
  }

  public float toDp(int px) {
    return dpr != 0 ? px / dpr : 0;
  }

  public boolean hitTest(V2i point) {
    return Rect.isInside(point, pos, size);
  }

  public int right() {
    return pos.x + size.x;
  }

  public void draw(WglGraphics g) {
    g.drawRect(pos.x, pos.y, size, Colors.viewColor);
  }

  protected final void enableScissor(WglGraphics g) {
    g.enableScissor(pos.x, pos.y, size);
  }

  protected static void disableScissor(WglGraphics g) {
    g.disableScissor();
  }

  protected boolean update(double timestamp) {
    return false;
  }

  protected void onPosChange(V2i newPos) {}

  protected void onSizeChange(V2i newSize) {}

  protected void onDprChange(float olDpr, float newDpr) {}

  protected void onTextRenderingSettingsChange() {}

  protected boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return false;
  }

  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    return null;
  }

  protected boolean onMouseUp(MouseEvent event, int button) {
    return false;
  }

  public void onMouseMove(MouseEvent event, SetCursor setCursor) {}

  protected void onMouseLeaveWindow() {}

  protected boolean onScroll(MouseEvent event, float dX, float dY) {
    return false;
  }

}
