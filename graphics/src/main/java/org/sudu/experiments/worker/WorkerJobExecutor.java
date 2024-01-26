package org.sudu.experiments.worker;

import java.util.function.Consumer;

public interface WorkerJobExecutor {
  void sendToWorker(Consumer<Object[]> handler, String method, Object ... args);
}
