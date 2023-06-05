package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsITextModel extends JsDisposable {

  int getOffsetAt(JsPosition position);
  JsPosition getPositionAt(int offset);

  @JSFunctor interface Factory extends JSObject {
    JsITextModel create(JSString value, JSString language, JsUri uri);
  }

  class Setter {
    @JSBody(params = {"model"}, script = "modelFactory = model;")
    public static native void setModel(JsITextModel.Factory model);
  }
}
