package org.sudu.experiments;

import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

// js reflection of this file is located at
// demo-edit-es-module/module/editor.d.ts

public interface Editor_d_ts extends JSObject {
  void dispose();
  void focus();
  JSString saySomething();
  void setText(JSString t);

  interface EditArguments extends JSObject {
    @JSProperty JSString getContainerId();

    String workerUrlProperty = "workerUrl";
    @JSProperty JSString getWorkerUrl();
  }

  @JSFunctor interface Factory extends JSObject {
    Promise<Editor_d_ts> create(EditArguments args);
  }

  class Setter {
    @JSBody(params = {"api"}, script = "editorFactory = api;")
    static native void setApi(Editor_d_ts.Factory api);
  }
}
