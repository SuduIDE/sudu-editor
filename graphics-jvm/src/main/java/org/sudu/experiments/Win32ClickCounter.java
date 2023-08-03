package org.sudu.experiments;

import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.win32.Win32Time;

import java.util.function.Supplier;

import static org.sudu.experiments.input.MouseListener.MOUSE_BUTTON_LEFT;
import static org.sudu.experiments.input.MouseListener.clickTimeFrame;

public class Win32ClickCounter {

  final V2i clickLoc = new V2i();
  final Win32Time timer;
  double clickTime = 0;
  int clicks = 0;

  public Win32ClickCounter(Win32Time timer) {
    this.timer = timer;
  }

  public int clicks() {
    return clicks;
  }

  public void onMouseDown(MouseEvent event, int button) {
    if (button != MOUSE_BUTTON_LEFT) return;

    clickTime = timer.now();
    if (!event.position.equals(clickLoc)) {
      clicks = 0;
    }
    clickLoc.set(event.position);
  }

  public void onMouseUp(MouseEvent event, int button) {
    if (button != MOUSE_BUTTON_LEFT) return;

    clicks = timer.now() - clickTime <= clickTimeFrame ? clicks + 1 : 1;
  }
}
