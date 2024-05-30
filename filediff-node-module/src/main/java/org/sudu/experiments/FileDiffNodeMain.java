package org.sudu.experiments;

import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import static org.sudu.experiments.editor.worker.EditorWorker.array;

// see ModuleExports.js.1
// see editor.d.ts

public interface FileDiffNodeMain {

  @JSFunctor interface ModuleFactory extends JSObject {
    Promise<JSObject> f(JSString url, int numThreads);

    class Setter {
      @JSBody(params = {"f"}, script = "moduleFactory = f;")
      public static native void setApi(ModuleFactory f);
    }
  }

  static void main(String[] args) {
    ModuleFactory.Setter.setApi(FileDiffNodeMain::moduleFactory);
  }

  interface DiffModuleJs extends JSObject {
    void terminateWorkers();
    Promise<JSString> fib(int n);
  }

  class DiffModule implements DiffModuleJs {
    final NodeWorkersPool pool;

    DiffModule(JsArray<NodeWorker> worker) {
      pool = new NodeWorkersPool(worker);
    }

    @Override
    public void terminateWorkers() {
      pool.terminateAll();
    }

    @Override
    public Promise<JSString> fib(int n) {
      return Promise.create((postResult,postError) -> {
        pool.sendToWorker(
            result -> {
              int[] intResult = array(result, 0).ints();
              postResult.f(JSString.valueOf(
                  "r: " + intResult[0] + ", time: " + intResult[1]
              ));
            }, TestJobs.fibonacci, new int[]{ n }
        );
      });
    }
  }

  static Promise<JSObject> moduleFactory(JSString workerUrl, int numThreads) {
    int nT = numThreads < 1 || numThreads > 10 ? 5 : numThreads;
    if (numThreads != nT)
      JsHelper.consoleInfo("wrong number of threads", numThreads);

    return Promise.create(
        (postResult, postError) -> NodeWorkersPool.start(
            array -> postResult.f(new DiffModule(array)),
            postError, workerUrl, nT));
  }
}
