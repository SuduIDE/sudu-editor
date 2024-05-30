package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.WebWorkerContext;
import org.sudu.experiments.js.WorkerProtocol;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.events.MessageEvent;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class WorkersPool {

  private final LinkedList<Job> delayedJobs = new LinkedList<>();
  private final JsArray<WebWorkerContext> workers;
  private final TreeMap<Integer, Consumer<Object[]>> jobs = new TreeMap<>();
  private final int[] freeWorkers;
  private int workerJobIdNext, freeWorkersCount;

  static final boolean debug = false;

  public WorkersPool(JsArray<WebWorkerContext> workers) {
    this.workers = workers;
    int numWorkers = workers.getLength();
    for (int i = 0; i < numWorkers; ++i) {
      final int workerIndex = i;
      workers.get(i).onMessage(message -> onWorkerMessage(message, workerIndex));
      WorkerProtocol.sendPingToWorker(workers.get(i));
    }
    freeWorkersCount = 0;
    freeWorkers = new int[numWorkers];
  }

  public void terminateAll() {
    for (int i = 0; i < workers.getLength(); ++i) {
      workers.get(i).terminate();
      workers.set(i, null);
    }
  }

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
    WorkerProtocol.sendToWorker(workers.get(index), id, method, args);
  }

  private int nextFreeWorker() {
    return freeWorkers[--freeWorkersCount];
  }

  final Function<Integer, Consumer<Object[]>> jobHandler = jobs::remove;

  private void onWorkerMessage(MessageEvent event, int index) {
    if (debug) JsHelper.consoleInfo(
        "onWorkerMessage: delayedJobs.size = ", delayedJobs.size());
    if (WorkerProtocol.isPing(event.getData()) && debug) {
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
    WorkerProtocol.onEdtMessage(jobHandler, event.getData());
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
