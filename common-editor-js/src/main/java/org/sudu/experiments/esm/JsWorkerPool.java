package org.sudu.experiments.esm;

import org.sudu.experiments.WebWorkersPool;
import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.Promise;
import org.sudu.experiments.js.WebWorkerContext;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

// export function newWorkerPool(
//     workerUrl: string, numThreads: number
// ): Promise<WorkerPool>;

@JSFunctor
interface NewWorkerPool extends JSObject {
  Promise<JsWorkerPool> f(JSString url, int numThreads);

  class Setter {
    @JSBody(params = {"f"}, script = "newWorkerPool = f;")
    static native void setDiff(NewWorkerPool f);
  }
}

public interface JsWorkerPool extends JSObject {
  int getNumThreads();

  static Promise<JsWorkerPool> create(JSString url, int numThreads) {
    return Promise.create((postResult, postError) -> {
      WebWorkerContext.start(
          workersArray -> postResult.f(new Impl(workersArray)),
          postError, url, numThreads);

    });
  }

  static void install() {
    NewWorkerPool.Setter.setDiff(JsWorkerPool::create);
  }

  class Impl implements JsWorkerPool {
    final WebWorkersPool workers;

    public Impl(JsArray<WebWorkerContext> workerArray) {
      workers = new WebWorkersPool(workerArray);
    }

    @Override
    public int getNumThreads() {
      return workers.numThreads();
    }
  }
}

