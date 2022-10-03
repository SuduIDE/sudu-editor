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

    ls.addListener(r);
    r.toRemove = ls.addListener(l1);
    ls.addListener(l2);
    ls.addListener(l3);

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
    Disposable d = ls.addListener(l1);

    d.dispose();

    try {
      d.dispose();
      Assertions.fail("no exception on second dispose");
    } catch (RuntimeException ignored) {}
    try {
      d.dispose();
      Assertions.fail("no exception on 3rd dispose");
    } catch (RuntimeException ignored) {}
  }

  class Listener implements InputListener {
    boolean fired = false;
    int order;
    public void onBlur() {
      fired = true;
      this.order = getOrder();
    }
  }

  class ListenerRemover extends Listener {
    Disposable toRemove;
    @Override
    public void onBlur() {
      super.onBlur();
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