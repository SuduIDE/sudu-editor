package org.sudu.experiments;

import org.sudu.experiments.editor.worker.FileDiffWorker;
import org.sudu.experiments.editor.worker.ThreadId;
import org.sudu.experiments.js.NodeWorker;
import org.sudu.experiments.js.node.NodeWorkersBridge;

public class DiffEngineWorker {
  public static void main(String[] args) {
    ThreadId.id = "DiffEngineWorker.main";
    NodeWorker.workerMain(FileDiffWorker::execute, new NodeWorkersBridge());
  }
}
