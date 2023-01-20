package org.sudu.experiments.worker;

public interface WorkerExecutor {
  Object[] execute(String method, Object[] args);
}
