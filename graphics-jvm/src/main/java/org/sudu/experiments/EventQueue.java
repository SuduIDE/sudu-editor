package org.sudu.experiments;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class EventQueue implements Executor {
  final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();

  @Override
  public void execute(Runnable command) {
    queue.add(command);
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
