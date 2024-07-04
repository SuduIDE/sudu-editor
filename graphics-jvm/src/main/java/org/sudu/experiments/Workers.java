package org.sudu.experiments;

import org.sudu.experiments.worker.WorkerExecutor;
import org.sudu.experiments.worker.WorkerJobExecutor;
import org.sudu.experiments.worker.WorkerProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Workers implements WorkerJobExecutor {
  final ExecutorService bgWorkerLo;
  final ExecutorService bgWorkerHi;
  final EventQueue eventQueue;
  final WorkerExecutor workerExecutor;

  public Workers(
      int numThreads,
      WorkerExecutor workerExecutor,
      EventQueue eventQueue
  ) {
    this.bgWorkerLo = newThreadPool(numThreads);
    this.bgWorkerHi = newThreadPool(numThreads);
    this.eventQueue = eventQueue;
    this.workerExecutor = workerExecutor;
  }

  public void shutdown() {
    bgWorkerLo.shutdown();
    bgWorkerHi.shutdown();
  }

  static ExecutorService newThreadPool(int maxThreads) {
    // to save memory we can use executor.allowCoreThreadTimeOut(true);
    // to shutdown worker threads if no activity
    return maxThreads <= 1
        ? Executors.newSingleThreadExecutor()
        : Executors.newFixedThreadPool(maxThreads);
  }

  @Override
  public void sendToWorker(
      boolean priority,
      Consumer<Object[]> handler, String method, Object[] args
  ) {
    (priority ? bgWorkerHi : bgWorkerLo).execute(
        WorkerProxy.job(
            workerExecutor, method, args, handler, eventQueue));
  }
}
