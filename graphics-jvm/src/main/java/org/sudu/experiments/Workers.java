package org.sudu.experiments;

import org.sudu.experiments.worker.WorkerExecutor;
import org.sudu.experiments.worker.WorkerProxy;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Workers {
  final ExecutorService bgWorker;
  final WorkerExecutor workerExecutor;

  public Workers(int numThreads, WorkerExecutor workerExecutor) {
    this.bgWorker = newThreadPool(numThreads);
    this.workerExecutor = workerExecutor;
  }

  public void shutdown() {
    bgWorker.shutdown();
  }

  static ExecutorService newThreadPool(int maxThreads) {
    // to save memory we can use executor.allowCoreThreadTimeOut(true);
    // to shutdown worker threads if no activity
    return maxThreads <= 1
        ? Executors.newSingleThreadExecutor()
        : Executors.newFixedThreadPool(maxThreads);
  }

  void sendToWorker(
      Consumer<Object[]> handler, String method, Object[] args,
      Executor eventQueue
  ) {
    bgWorker.execute(
        WorkerProxy.job(
            workerExecutor, method, args, handler, eventQueue));
  }
}
