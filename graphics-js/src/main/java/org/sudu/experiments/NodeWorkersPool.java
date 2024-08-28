package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.NodeWorker;

public class NodeWorkersPool extends WorkersPool {
  public NodeWorkersPool(JsArray<NodeWorker> workers) {
    super(workers);
  }

  @Override
  protected void setMessageHandler(int index) {
    workers.get(index).<NodeWorker>cast().onMessage(
        msg -> onWorkerMessage(msg, index)
    );
  }

  public void terminateAll() {
    for (int i = 0; i < workers.getLength(); ++i) {
      workers.get(i).<NodeWorker>cast().terminate();
      workers.set(i, null);
    }
  }

  public int workersLength() {
    return workers.getLength();
  }
}
