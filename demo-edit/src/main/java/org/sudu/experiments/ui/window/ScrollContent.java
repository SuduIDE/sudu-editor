package org.sudu.experiments.ui.window;

import org.sudu.experiments.math.V2i;

public class ScrollContent extends View {

  public final V2i virtualSize = new V2i();
  public final V2i scrollPos = new V2i();
  protected ScrollView scrollView;

  public void setScrollView(ScrollView scrollView) {
    this.scrollView = scrollView;
  }

  public void setVirtualSize(int w, int h) {
    virtualSize.set(w, h);
  }

  protected void layoutScroll() {
    if (scrollView != null) {
      scrollView.layoutScroll();
    }
  }

  // after resize or set content
  protected void limitScrollPos() {
    scrollPos.x = limitScrollX(scrollPos.x);
    scrollPos.y = limitScrollY(scrollPos.y);
  }

  // after mouse events
  public boolean setScrollPosX(int x) {
    int px = scrollPos.x, nx = limitScrollX(x);
    scrollPos.x = nx;
    return px != nx;
  }

  // after mouse events
  public boolean setScrollPosY(int y) {
    int py = scrollPos.y, ny = limitScrollY(y);
    scrollPos.y = ny;
    return py != ny;
  }

  private int limitScrollY(int y) {
    return Math.max(0, Math.min(y, virtualSize.y - size.y));
  }

  private int limitScrollX(int x) {
    return Math.max(0, Math.min(x, virtualSize.x - size.x));
  }
}
