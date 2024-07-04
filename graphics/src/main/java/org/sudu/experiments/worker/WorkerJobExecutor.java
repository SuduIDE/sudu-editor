package org.sudu.experiments.worker;

import java.util.function.Consumer;

public interface WorkerJobExecutor {
  default void sendToWorker(Consumer<Object[]> handler, String method, Object ... args) {
    sendToWorker(false, handler, method, args);
  }

  void sendToWorker(
      boolean priority,
      Consumer<Object[]> handler,
      String method,
      Object ... args);
}
