package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSString;

public interface ChildProcess extends JSObject {

  //https://nodejs.org/api/child_process.html
  interface child_process extends JSObject {
    class Globals {
      @JSBody(script = "return child_process")
      public static native child_process child_process();
    }

    abstract class ExecFileOptions implements JSObject {
      @JSBody(params = "encoding", script = "{ encoding: encoding };")
      public native ExecFileOptions create(JSString encoding);

      @JSBody(script = "{};")
      public native ExecFileOptions create();
    }

    @JSFunctor
    interface TerminateCallback extends JSObject {
      void f(JSError error, JSString stdout, JSString stderr);
    }

    //https://nodejs.org/api/child_process.html#child_processexecfilefile-args-options-callback
    // execFile(file[, args][, options][, callback])
    ChildProcess execFile(
        JSString path,
        JsArray<JSString> args,
        ExecFileOptions options,
        TerminateCallback terminateCallback
    );
  }
}
