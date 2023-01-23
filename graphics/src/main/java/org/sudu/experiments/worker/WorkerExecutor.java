package org.sudu.experiments.worker;

import java.util.function.Consumer;

public interface WorkerExecutor {
  void execute(String method, Object[] args, Consumer<Object[]> result);

  static WorkerExecutor i() { return (method, args, result) -> {}; }
}
