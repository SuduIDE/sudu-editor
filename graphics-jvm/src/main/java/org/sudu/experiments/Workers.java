package org.sudu.experiments;

import org.sudu.experiments.worker.WorkerExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Workers {
  final ExecutorService bgWorker;
  final WorkerExecutor workerExecutor;
  final ExecutorService[] channelExecutors;

  public Workers(int numThreads, WorkerExecutor workerExecutor) {
    this.bgWorker = newThreadPool(numThreads);
    this.workerExecutor = workerExecutor;
    channelExecutors = new ExecutorService[numThreads];
  }

  public void shutdown() {
    bgWorker.shutdown();
    for (int i = 0; i < channelExecutors.length; i++) {
      ExecutorService executor = channelExecutors[i];
      if (executor != null) {
        channelExecutors[i] = null;
        executor.shutdown();
      }
    }
  }

  private Executor dedicatedExecutor(int ch) {
    int idx = ch < channelExecutors.length ? ch : 0;
    ExecutorService executor = channelExecutors[idx];
    if (executor == null) {
      executor = Executors.newSingleThreadExecutor();
      channelExecutors[ch] = executor;
    }
    return executor;
  }

  static ExecutorService newThreadPool(int maxThreads) {
    // to save memory we can use executor.allowCoreThreadTimeOut(true);
    // to shutdown worker threads if no activity
    return maxThreads <= 1
        ? Executors.newSingleThreadExecutor()
        : Executors.newFixedThreadPool(maxThreads);
  }

  void executeOnSingleThread(int channel, Runnable job) {
    dedicatedExecutor(channel).execute(job);
  }
}
