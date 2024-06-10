package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class WorkersPool {

  protected final JsArray<? extends JsMessagePort0> workers;
  private final LinkedList<Job> delayedJobs = new LinkedList<>();
  private final TreeMap<Integer, Consumer<Object[]>> jobs = new TreeMap<>();
  private final int[] freeWorkers;
  private int workerJobIdNext, freeWorkersCount;

  static final boolean debug = false;

  public WorkersPool(JsArray<? extends JsMessagePort0> workers) {
    this.workers = workers;
    int numWorkers = workers.getLength();
    for (int i = 0; i < numWorkers; ++i) {
      setMessageHandler(i);
      WorkerProtocol.sendPingToWorker(workers.get(i));
    }
    freeWorkersCount = 0;
    freeWorkers = new int[numWorkers];
  }

  protected abstract void setMessageHandler(int index);

  private int nextId() {
    return ++workerJobIdNext;
  }

  public void sendToWorker(Consumer<Object[]> handler, String method, Object... args) {
    if (freeWorkersCount > 0) {
      sendToWorkerIdx(handler, method, args, nextFreeWorker());
    } else {
      delayedJobs.addLast(new Job(handler, method, args));
    }
  }

  private void sendToWorkerIdx(Consumer<Object[]> handler, String method, Object[] args, int index) {
    int id = nextId();
    jobs.put(id, handler);
    JsMessagePort0 worker = workers.get(index);
    if (worker != null)
      WorkerProtocol.sendToWorker(worker, id, method, args);
    else
      JsHelper.consoleError("sendToWorker after shutdown, method = ", JSString.valueOf(method));
  }

  private int nextFreeWorker() {
    return freeWorkers[--freeWorkersCount];
  }

  final Function<Integer, Consumer<Object[]>> jobHandler = jobs::remove;

  protected void onWorkerMessage(JSObject data, int index) {
    if (debug) JsHelper.consoleInfo(
        "onWorkerMessage: delayedJobs.size = ", delayedJobs.size());
    if (WorkerProtocol.isPing(data) && debug) {
      JsHelper.consoleInfo("  ping response from worker ", index);
    }
    Job job = delayedJobs.pollFirst();
    if (job != null) {
      if (debug) {
        JsHelper.consoleInfo("  reuse worker index ", index);
        JsHelper.consoleInfo("  method ", JSString.valueOf(job.method));
      }
      sendToWorkerIdx(job.handler, job.method, job.args, index);
    } else {
      freeWorkers[freeWorkersCount++] = index;
      if (debug) {
        JsHelper.consoleInfo("  add a free worker N ", index);
        JsHelper.consoleInfo("  freeWorkersCount = ", freeWorkersCount);
      }
    }
    WorkerProtocol.onEdtMessage(jobHandler, data);
  }

  static class Job {
    Consumer<Object[]> handler;
    String method;
    Object[] args;

    Job(Consumer<Object[]> handler, String method, Object[] args) {
      this.handler = handler;
      this.method = method;
      this.args = args;
    }
  }
}
