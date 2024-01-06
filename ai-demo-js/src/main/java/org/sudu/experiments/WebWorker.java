package org.sudu.experiments;

import org.sudu.experiments.editor.worker.EditorWorker;
import org.sudu.experiments.js.WorkerContext;

public class WebWorker {
  public static void main(String[] args) {
    WorkerContext.workerMain(EditorWorker::execute);
  }
}
