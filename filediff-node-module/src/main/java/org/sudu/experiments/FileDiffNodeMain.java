package org.sudu.experiments;

import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

import static org.sudu.experiments.editor.worker.EditorWorker.array;

// see ModuleExports.js.1
// see editor.d.ts

public interface FileDiffNodeMain {

  @JSFunctor interface ModuleFactory extends JSObject {
    Promise<JSObject> f(JSString url, int numThreads);

    class Setter {
      @JSBody(params = {"f"}, script = "createDiffEngine = f;")
      public static native void setApi(ModuleFactory f);
    }
  }

  static void main(String[] args) {
    ModuleFactory.Setter.setApi(FileDiffNodeMain::moduleFactory);
  }

  interface Channel extends JSObject {
    void sendMessage(JsArray<JSObject> message);
    @JSProperty("onMessage")
    void setOnMessage(JsFunctions.Consumer<JsArray<JSObject>> onMessage);
  }

  interface DiffEngineJs extends JSObject {
    void terminateWorkers();
    Promise<JSString> fib(int n);
    void startFolderDiff(JSString leftPath, JSString rightPath, Channel channel);
    void testFS(JSString path);
  }

  class DiffEngine implements DiffEngineJs {
    final NodeWorkersPool pool;

    DiffEngine(JsArray<NodeWorker> worker) {
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

    @Override
    public void startFolderDiff(JSString leftPath, JSString rightPath, Channel channel) {
      JsHelper.consoleInfo("Starting folder diff ");
      JsHelper.consoleInfo("\t leftPath ", leftPath);
      JsHelper.consoleInfo("\t rightPath ", rightPath);
      channel.setOnMessage(
          m -> JsHelper.consoleInfo("channel onMessage ", m)
      );
    }

    @Override
    public void testFS(JSString path) {
      JsHelper.consoleInfo("fs = ", Fs.fs());
      JsHelper.consoleInfo("O_APPEND = ", Fs.fs().constants().O_APPEND());
    }
  }

  static Promise<JSObject> moduleFactory(JSString workerUrl, int numThreads) {
    int nT = numThreads < 1 || numThreads > 10 ? 5 : numThreads;
    if (numThreads != nT)
      JsHelper.consoleInfo("wrong number of threads: ", numThreads);

    return Promise.create(
        (postResult, postError) -> NodeWorkersPool.start(
            array -> postResult.f(new DiffEngine(array)),
            postError, workerUrl, nT));
  }
}
