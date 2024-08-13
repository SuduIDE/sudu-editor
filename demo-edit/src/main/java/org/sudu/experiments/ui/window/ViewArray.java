package org.sudu.experiments.ui.window;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;

public abstract class ViewArray extends View {

  protected View[] views;

  protected ViewArray() {}

  protected ViewArray(View ... views) {
    setViews(views);
  }

  protected void setViews(View ... views) {
    if (this.views != null) throw new UnsupportedOperationException();
    this.views = views;
  }

  @Override
  public void dispose() {
    for (View view : views) {
      view.dispose();
    }
  }

  @Override
  public void draw(WglGraphics g) {
    for (View view : views) {
      view.draw(g);
    }
  }

  protected boolean update(double timestamp) {
    boolean r = false;
    for (View view : views) {
      r |= view.update(timestamp);
    }
    return r;
  }

  @Override
  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    super.setPosition(newPos, newSize, newDpr);
    layoutViews();
  }

  protected abstract void layoutViews();

  @Override
  protected boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    boolean result = false;
    for (View view : views) {
      if (view.hitTest(event.position)) {
        result |= view.onMouseClick(event, button, clickCount);
      }
    }
    return result;
  }

  @Override
  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    for (View view : views) {
      if (view.hitTest(event.position)) {
        var onMouseDown = view.onMouseDown(event, button);
        if (onMouseDown != null) return onMouseDown;
      }
    }
    return null;
  }

  @Override
  protected boolean onMouseUp(MouseEvent event, int button) {
    boolean result = false;
    for (View view : views) {
      if (view.hitTest(event.position)) {
        result |= view.onMouseUp(event, button);
      }
    }
    return result;
  }

  @Override
  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    boolean result = false;
    for (View view : views) {
      if (view.hitTest(event.position)) {
        result |= view.onMouseMove(event, setCursor);
      }
    }
    return result;
  }

  @Override
  protected boolean onScroll(MouseEvent event, float dX, float dY) {
    boolean result = false;
    for (View view : views) {
      if (view.hitTest(event.position)) {
        result |= view.onScroll(event, dX, dY);
      }
    }
    return result;
  }

  @Override
  protected void onTextRenderingSettingsChange() {
    for (View view : views) {
      view.onTextRenderingSettingsChange();
    }
  }
}
