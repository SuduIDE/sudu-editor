package org.sudu.experiments.diff;

import org.sudu.experiments.editor.DiffRef;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.ScrollContent;
import org.sudu.experiments.ui.window.ScrollView;

import java.util.function.IntConsumer;

class DiffRefView implements DiffRef {
  final ScrollView scrollView;
  final ScrollContent view;
  float lineHeight = 15;

  public DiffRefView(ScrollView view, ScrollContent content) {
    scrollView = view;
    this.view = content;
  }

  public int getFirstLine() {
    return view.scrollPos.y / lineHeight();
  }

  public int getLastLine() {
    return Numbers.iDivRoundUp(view.virtualSize.y, lineHeight());
  }

  @Override
  public int lineToPos(int line) {
    return line * lineHeight() + view.pos.y - view.scrollPos.y;
  }

  public V2i pos() {
    return view.pos;
  }

  public V2i size() {
    return view.size;
  }

  public void setScrollListeners(Runnable hListener, IntConsumer vListener) {
    scrollView.setListeners(hListener, vListener);
  }

  public boolean setVScrollPosSilent(int pos) {
    return scrollView.setVScrollPosSilent(pos);
  }

  public int lineHeight() {
    return view.toPx(lineHeight);
  }

  public boolean onKeyPress(KeyEvent event) {
    return false;
  }
}
