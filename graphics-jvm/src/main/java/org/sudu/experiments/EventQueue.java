package org.sudu.experiments;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventQueue {
  final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();

  public void addEvent(Runnable r) {
    queue.add(r);
  }

  public void execute() {
    Runnable r = queue.poll();
    while (r != null) {
      try {
        r.run();
      } catch (Exception ex) {
        System.err.println("Exception in event: " + ex.getMessage());
      }
      r = queue.poll();
    }
  }
}
