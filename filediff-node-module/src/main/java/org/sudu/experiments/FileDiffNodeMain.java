package org.sudu.experiments;

import org.sudu.experiments.js.JsMessagePort;
import org.sudu.experiments.js.Promise;
import org.sudu.experiments.js.WorkerProtocol;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

// see ModuleExports.js.1
// see editor.d.ts

public interface FileDiffNodeMain {

  @JSFunctor interface ModuleFactory extends JSObject {
    Promise<JSObject> f(JSString args);

    class Setter {
      @JSBody(params = {"f"}, script = "moduleFactory = f;")
      public static native void setApi(ModuleFactory f);
    }
  }

  static void main(String[] args) {
    ModuleFactory.Setter.setApi(FileDiffNodeMain::moduleFactory);
  }

  interface DiffModuleJs extends JSObject {
    Promise<JSString> foo();
  }

  class DiffModule implements DiffModuleJs {
    final JsMessagePort worker;

    DiffModule(JsMessagePort worker) {
      this.worker = worker;
    }

    @Override
    public Promise<JSString> foo() {
      return Promise.create((postResult,postError) -> {
        worker.onMessage(postResult.cast());
        worker.postMessage(WorkerProtocol.ping());
      });
    }
  }

  static Promise<JSObject> moduleFactory(JSString workerUrl) {
    return Promise.create(
        (postResult, postError) -> {
          JsMessagePort worker = JsMessagePort.Native.newWorker(workerUrl);
          worker.onMessage(message -> {
            if (WorkerProtocol.isStarted(message)) {
              postResult.f(new DiffModule(worker));
            }
          });
          worker.on("error", postError);

//          WebWorkerContext.start(
//              worker -> postResult.f(new JsCodeEditor0(arguments, worker)),
//              postError,
//              arguments.workerUrl(),
//              arguments.numWorkerThreads());

        }
    );
  }

}
