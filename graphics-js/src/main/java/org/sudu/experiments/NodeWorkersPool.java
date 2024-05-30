package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsMessagePort;

public class NodeWorkersPool extends WorkersPool {
  public NodeWorkersPool(JsArray<JsMessagePort> workers) {
    super(workers);
  }

  @Override
  protected void setMessageHandler(int index) {
    workers.get(index).<JsMessagePort>cast().onMessage(
        msg -> onWorkerMessage(msg, index)
    );
  }

  public void terminateAll() {
    for (int i = 0; i < workers.getLength(); ++i) {
      workers.get(i).<JsMessagePort>cast().close();
      workers.set(i, null);
    }
  }
}
