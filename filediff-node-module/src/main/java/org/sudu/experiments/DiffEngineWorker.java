package org.sudu.experiments;

import org.sudu.experiments.editor.worker.FileDiffWorker;
import org.sudu.experiments.js.NodeWorker;

public class DiffEngineWorker {
  public static void main(String[] args) {
    NodeWorker.workerMain(FileDiffWorker::execute);
  }
}
