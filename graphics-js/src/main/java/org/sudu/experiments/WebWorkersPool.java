package org.sudu.experiments;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.WebWorkerContext;

public class WebWorkersPool extends WorkersPool {
  public WebWorkersPool(JsArray<WebWorkerContext> workers) {
    super(workers);
  }

  @Override
  protected void setMessageHandler(int workerIndex) {
    workers.get(workerIndex).<WebWorkerContext>cast().onMessage(
        event -> onWorkerMessage(event.getData(), workerIndex));
  }

  public void terminateAll() {
    for (int i = 0; i < workers.getLength(); ++i) {
      workers.get(i).<WebWorkerContext>cast().terminate();
      workers.set(i, null);
    }
  }
}
