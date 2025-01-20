package org.sudu.experiments;

import org.sudu.experiments.editor.worker.FileDiffWorker;
import org.sudu.experiments.js.NodeWorker;
import org.sudu.experiments.js.node.NodeWorkersBridge;

import java.util.function.Consumer;

public class DiffEngineWorker {
  static class Executor {
    public static void execute(String method, Object[] a, Consumer<Object[]> onResult) {
      FileDiffWorker.execute(method, a, onResult);
    }
  }
  public static void main(String[] args) {
    NodeWorker.workerMain(Executor::execute, new NodeWorkersBridge());
  }
}
