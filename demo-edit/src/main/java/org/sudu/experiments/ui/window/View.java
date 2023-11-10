package org.sudu.experiments.ui.window;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Rect;
import org.sudu.experiments.math.V2i;

import java.util.function.Consumer;

public class View implements Disposable {
  public final V2i pos = new V2i();
  public final V2i size = new V2i();
  public float dpr;

  public void dispose() {}

  protected V2i minimalSize() {
    return new V2i(0,0);
  }

  protected void setPosition(V2i newPos, V2i newSize, float newDpr) {
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

  public boolean hitTest(V2i point) {
    return Rect.isInside(point, pos, size);
  }

  protected void draw(WglGraphics g) {
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

  protected boolean onMouseMove(MouseEvent event) {
    return false;
  }

  boolean onScroll(MouseEvent event, float dX, float dY) {
    return false;
  }

}
