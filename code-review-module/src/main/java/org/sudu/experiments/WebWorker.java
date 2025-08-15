package org.sudu.experiments;

import org.sudu.experiments.editor.worker.EditorWorker;
import org.sudu.experiments.js.WebWorkerContext;

public class WebWorker {
  public static void main(String[] args) {
    WebWorkerContext.workerMain(EditorWorker::execute);
  }
}
