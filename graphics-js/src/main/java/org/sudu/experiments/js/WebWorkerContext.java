package org.sudu.experiments.js;

import org.sudu.experiments.worker.WorkerExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.events.MessageEvent;

public abstract class WebWorkerContext implements JsMessagePort0 {

  @JSBody(params = "url", script = "return new Worker(url);")
  public static native WebWorkerContext newWorker(String url);

  @JSBody(params = "url", script = "return new Worker(url);")
  public static native WebWorkerContext newWorker(JSString url);

  @JSBody(script = "return self")
  public static native WebWorkerContext self();

  @JSProperty("onmessage")
  public abstract void onMessage(JsFunctions.Consumer<MessageEvent> f);

  @JSProperty("onmessage")
  public abstract void onMessage(JSObject jsObject);

  public abstract void terminate();

  public static void start(
      JsFunctions.Consumer<JsArray<WebWorkerContext>> onStart,
      JsFunctions.Consumer<JSObject> error,
      JSString url, int count
  ) {
    JsArray<WebWorkerContext> workers = JsArray.create();
    for (int i = 0; i < count; i++) {
      WebWorkerContext worker = newWorker(url);
      worker.onMessage(message -> {
        if (WorkerProtocol.isStarted(message.getData())) {
          worker.onMessage((JSObject) null);
          workers.push(worker);
          if (workers.getLength() == count) onStart.f(workers);
        } else {
          error.f(JsHelper.newError("worker is not started"));
        }
      });
    }
  }

  public static void start(
      JsFunctions.Consumer<JsArray<WebWorkerContext>> onStart,
      String url, int N
  ) {
    start(onStart, WebWorkerContext::throwJsError, JSString.valueOf(url), N);
  }

  static void throwJsError(JSObject jsError) {
    throw new RuntimeException(jsError.<JSError>cast().getMessage());
  }

  public static void workerMain(WorkerExecutor executor) {
    self().onMessage(e -> WorkerProtocol.onWorkerMessage(executor, e.getData(), self()));
    self().postMessage(WorkerProtocol.started());
  }
}
