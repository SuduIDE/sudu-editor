package org.sudu.experiments.input;

import org.sudu.experiments.Disposable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InputListenersTest {
  Repainter r = new Repainter();
  InputListeners ls = new InputListeners(r);
  Listener l1 = new Listener();
  Listener l2 = new Listener();
  Listener l3 = new Listener();
  int order;

  @Test void testRepaint() {
    ls.sendBlurEvent();
  }

  @Test
  void testDelivery() {
    ListenerRemover r = new ListenerRemover();

    ls.onBlur.add(r);
    r.toRemove = ls.onBlur.disposableAdd(l1);
    ls.onBlur.add(l2);
    ls.onBlur.add(l3);

    ls.sendBlurEvent();


    Assertions.assertTrue(r.fired && l1.fired && l2.fired && l3.fired);
    Assertions.assertTrue(
        r.order < l1.order &&
            l1.order < l2.order &&
            l2.order < l3.order);

    ls.sendBlurEvent();
    // now l1 removed

    r.fired = l1.fired = l2.fired = l3.fired = false;

    ls.sendBlurEvent();

    Assertions.assertTrue(r.fired && !l1.fired && l2.fired && l3.fired);
    Assertions.assertTrue(r.order < l2.order && l2.order < l3.order);
  }

  @Test
  void testDisposeManyTimes() {
    Disposable d = ls.onBlur.disposableAdd(l1);

    d.dispose();

    d.dispose();
    d.dispose();
  }

  class Listener implements Runnable {
    boolean fired = false;
    int order;
    public void run() {
      fired = true;
      this.order = getOrder();
    }
  }

  class ListenerRemover extends Listener {
    Disposable toRemove;
    @Override
    public void run() {
      super.run();
      toRemove.dispose();
      toRemove = Disposable.empty();
    }
  }


  private int getOrder() {
    return order++;
  }

  static class Repainter implements Runnable {
    boolean fired;
    public void run() { fired = true; }
  }

}
