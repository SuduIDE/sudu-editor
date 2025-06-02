package org.sudu.experiments.ui.window;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.DprUtil;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.SetCursor;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class ScrollView extends View {

  private final Consumer<ScrollBar.Event> vScrollHandler = this::onMoveScrollV;
  private final Consumer<ScrollBar.Event> hScrollHandler = this::onMoveScrollH;

  private ScrollContent content;
  private ScrollBar vScroll, hScroll;
  private Runnable hListener;
  private IntConsumer vListener;
  private float scrollWidth = 10;
  private V4f sbLineColor, sbBackColor;
  private boolean vScrollVisible = true;
  private boolean hScrollVisible = true;

//  public ScrollView() { this(new ScrollContent()); }

  public ScrollView(ScrollContent content) {
    this.content = content;
    content.setScrollView(this);
  }

  public void setVerticalScrollVisibility(boolean visible) {
    vScrollVisible = visible;
  }

  public void setHorizontalScrollVisible(boolean visible) {
    hScrollVisible = visible;
  }

  @Override
  public void dispose() {
    content.setScrollView(null);
    content = Disposable.assign(content, null);
  }

  public void setContent(ScrollContent newContent) {
    if (content == newContent) return;
    content.setScrollView(null);
    content = Disposable.assign(content, newContent);
    newContent.setScrollView(this);
    newContent.setPosition(pos, size, dpr);
    if (dpr != 0) {
      layoutScroll();
    }
  }

  public void setScrollColor(V4f scrollBarLine, V4f scrollBarBg) {
    sbBackColor = scrollBarBg;
    sbLineColor = scrollBarLine;
    if (vScroll != null) vScroll.setColor(sbLineColor, sbBackColor);
    if (hScroll != null) hScroll.setColor(sbLineColor, sbBackColor);
  }

  public ScrollContent content() {
    return content;
  }

  public void setPosition(V2i newPos, V2i newSize, float newDpr) {
    super.setPosition(newPos, newSize, newDpr);
    content.setPosition(newPos, newSize, newDpr);
    if (newDpr != 0) layoutScroll();
  }

  @Override
  protected void onMouseLeaveWindow() {
    content.onMouseLeaveWindow();
  }

  public void setListeners(Runnable hsListener, IntConsumer vsListener) {
    hListener = hsListener;
    vListener = vsListener;
  }

  public void setScrollWidth(float scrollWidth) {
    this.scrollWidth = scrollWidth;
  }

  public void setScrollPos(int x, int y) {
    setScrollPosX(x);
    setScrollPosY(y);
  }

  protected void layoutScroll() {
    content.limitScrollPos();

    int sButtonLength = scrollWidthPx() * ScrollBar.BUTTON_SIZE;

    if (needHScroll(sButtonLength)) layoutHScroll();
    else hScroll = null;

    if (needVScroll(sButtonLength)) layoutVScroll();
    else vScroll = null;
  }

  private boolean needVScroll(int buttonLength) {
    return size.y > buttonLength && content.virtualSize.y > size.y;
  }

  private boolean needHScroll(int buttonLength) {
    return size.x > buttonLength && content.virtualSize.x > size.x;
  }

  private void layoutHScroll() {
    ensureHScroll().layoutHorizontal(
        content.scrollPos.x,
        pos.x, size.x, content.virtualSize.x,
        pos.y + size.y,
        scrollWidthPx());
  }

  private void layoutVScroll() {
    ensureVScroll().layoutVertical(
        content.scrollPos.y,
        pos.y, size.y, content.virtualSize.y,
        pos.x + size.x,
        scrollWidthPx());
  }

  public int scrollWidthPx() {
    return toPx(scrollWidth);
  }

  private ScrollBar ensureHScroll() {
    hScroll = (hScroll != null) ? hScroll : new ScrollBar();
    setScrollColor(hScroll);
    return hScroll;
  }

  private ScrollBar ensureVScroll() {
    vScroll = (vScroll != null) ? vScroll : new ScrollBar();
    setScrollColor(vScroll);
    return vScroll;
  }

  private void setScrollColor(ScrollBar scrollBar) {
    scrollBar.setColor(sbLineColor, sbBackColor);
  }

  public void draw(WglGraphics graphics) {
    content.draw(graphics);
    if (vScroll == null && hScroll == null) return;
    if (!vScrollVisible && !hScrollVisible) return;
    graphics.enableBlend(true);
    if (vScrollVisible && vScroll != null) vScroll.drawBg(graphics);
    if (hScrollVisible && hScroll != null) hScroll.drawBg(graphics);
    if (vScrollVisible && vScroll != null) vScroll.drawButton(graphics);
    if (hScrollVisible && hScroll != null) hScroll.drawButton(graphics);
    graphics.enableBlend(false);
  }

  private boolean scrollHitTest(V2i position) {
    return vScroll != null && vScroll.hitTest(position)
        || hScroll != null && hScroll.hitTest(position);
  }

  protected boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    return scrollHitTest(event.position)
        || content.onMouseClick(event, button, clickCount);
  }

  public void setScrollPosX(int hScrollPos) {
    if (setHScrollPosSilent(hScrollPos) && hListener != null) {
      hListener.run();
    }
  }

  public void setScrollPosY(int vScrollPos) {
    int delta = vScrollPos - content.scrollPos.y;
    if (setVScrollPosSilent(vScrollPos) && vListener != null) {
      vListener.accept(delta);
    }
  }

  public boolean setHScrollPosSilent(int hScrollPos) {
    boolean set = content.setScrollPosX(hScrollPos);
    int buttonLength = scrollWidthPx() * ScrollBar.BUTTON_SIZE;
    if (needHScroll(buttonLength)) layoutHScroll();
    return set;
  }

  public boolean setVScrollPosSilent(int vScrollPos) {
    boolean set = content.setScrollPosY(vScrollPos);
    int buttonLength = scrollWidthPx() * ScrollBar.BUTTON_SIZE;
    if (needVScroll(buttonLength)) layoutVScroll();
    return set;
  }

  private void onMoveScrollV(ScrollBar.Event event) {
    int vScrollPos = event.getPosition(content.virtualSize.y - size.y);
    setScrollPosY(vScrollPos);
  }

  private void onMoveScrollH(ScrollBar.Event event) {
    int hScrollPos = event.getPosition(content.virtualSize.x - size.x);
    setScrollPosX(hScrollPos);
  }

  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (vScroll != null) {
      var lock = vScroll.onMouseDown(event.position, vScrollHandler, true);
      if (lock != null) return lock;
    }
    if (hScroll != null) {
      var lock = hScroll.onMouseDown(event.position, hScrollHandler, false);
      if (lock != null) return lock;
    }
    return content.onMouseDown(event, button);
  }

  protected boolean onMouseUp(MouseEvent event, int button) {
    return scrollHitTest(event.position)
        || content.onMouseUp(event, button);
  }

  public void onMouseMove(MouseEvent event, SetCursor setter) {
    V2i position = event.position;
    var v = vScroll != null && vScroll.onMouseMove(position, setter);
    var h = hScroll != null && hScroll.onMouseMove(position, setter);
    if (!(h | v))
      content.onMouseMove(event, setter);
  }

  @Override
  protected boolean onScroll(MouseEvent event, float dX, float dY) {
    if (!hitTest(event.position)) return false;

    int changeY = DprUtil.toPx(dY * .5f, dpr);
    int changeX = DprUtil.toPx(dX * .5f, dpr);

    if (event.shift) {
      changeX += changeY;
      changeY = 0;
    }

    if (vScroll != null && changeY != 0) {
      setScrollPosY(content.scrollPos.y + changeY);
    }

    if (hScroll != null && changeX != 0) {
      setScrollPosX(content.scrollPos.x + changeX);
    }

    return true;
  }

  @Override
  public V2i minimalSize() {
    int sizePx = scrollWidthPx();
    V2i minimalSize = content.minimalSize();
    minimalSize.x = Math.max(minimalSize.x, sizePx);
    minimalSize.y = Math.max(minimalSize.y, sizePx);
    return minimalSize;
  }
}
