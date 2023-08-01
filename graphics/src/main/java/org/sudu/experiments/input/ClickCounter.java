package org.sudu.experiments.input;

import org.sudu.experiments.math.V2i;

import java.util.function.Supplier;

public class ClickCounter implements MouseListener {

  final V2i clickLoc = new V2i();
  final Supplier<Double> timer;
  double clickTime = 0;
  int clicks = 0;

  public ClickCounter(Supplier<Double> timer) {
    this.timer = timer;
  }

  public int clicks() {
    return clicks;
  }

  @Override
  public boolean onMouseDown(MouseEvent event, int button) {
    clickTime = timer.get();
    if (!event.position.equals(clickLoc)) {
      clicks = 0;
    }
    clickLoc.set(event.position);
    return false;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    clicks = timer.get() - clickTime <= clickTimeFrame ? clicks + 1 : 1;
    return false;
  }
}
