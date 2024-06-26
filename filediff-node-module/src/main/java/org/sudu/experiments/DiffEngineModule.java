package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface DiffEngineModule {

  @JSFunctor interface ModuleFactory extends JSObject {
    Promise<JSObject> f(JSString url, int numThreads);

    class Setter {
      @JSBody(params = {"f"}, script = "createDiffEngine = f;")
      public static native void setApi(ModuleFactory f);
    }
  }

  static void main(String[] args) {
    ModuleFactory.Setter.setApi(DiffEngineModule::moduleFactory);
    LoggingJs.Setter.set();
    ChannelTest.publishChannelTest();
  }

  static Promise<JSObject> moduleFactory(JSString workerUrl, int numThreads) {
    int nT = numThreads < 1 || numThreads > 10 ? 3 : numThreads;
    if (numThreads != nT)
      JsHelper.consoleInfo("wrong number of threads: ", numThreads);

    return Promise.create(
        (postResult, postError) -> NodeWorker.start(
            array -> postResult.f(new DiffEngine(array)),
            postError, workerUrl, nT));
  }
}
