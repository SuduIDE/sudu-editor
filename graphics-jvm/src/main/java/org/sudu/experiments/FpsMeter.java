package org.sudu.experiments;

import java.util.function.DoubleConsumer;

public class FpsMeter {
  private final DoubleConsumer fire;
  private double fireTime;
  private int frames;

  public FpsMeter(DoubleConsumer fire) {
    this.fire = fire;
    fireTime = TimeUtil.now();
  }

  public void notifyNewFrame() {
    frames++;
    double now = TimeUtil.now();
    if (fireTime + 2 < now) {
      double fps = frames / (now - fireTime);
      fireTime = now;
      frames = 0;
      fire.accept(fps);
    }
  }
}
