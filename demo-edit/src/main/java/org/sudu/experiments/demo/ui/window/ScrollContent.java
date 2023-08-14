package org.sudu.experiments.demo.ui.window;

import org.sudu.experiments.math.V2i;

public class ScrollContent extends View {

  public final V2i virtualSize = new V2i();
  public final V2i scrollPos = new V2i();

  protected void updateVirtualSize() {
    virtualSize.set(
        Math.max(virtualSize.x, size.x),
        Math.max(virtualSize.y, size.y));
  }

  protected void limitScrollPos() {
    scrollPos.x = limitScrollX(scrollPos.x);
    scrollPos.y = limitScrollY(scrollPos.y);
  }

  public void setScrollPosX(int x) {
    scrollPos.x = limitScrollX(x);
  }

  public void setScrollPosY(int y) {
    scrollPos.y = limitScrollY(y);
  }

  private int limitScrollY(int y) {
    return Math.max(0, Math.min(y, virtualSize.y - size.y));
  }

  private int limitScrollX(int x) {
    return Math.max(0, Math.min(x, virtualSize.x - size.x));
  }
}
