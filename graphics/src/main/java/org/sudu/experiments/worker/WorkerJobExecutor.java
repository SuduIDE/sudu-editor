package org.sudu.experiments.worker;

import java.util.function.Consumer;

public interface WorkerJobExecutor {
  int ACTIVITY_CHANNEL = 0;

  void sendToWorker(Consumer<Object[]> handler, String method, Object ... args);
  void sendToWorker(Consumer<Object[]> handler, int ch, String method, Object ... args);
}
