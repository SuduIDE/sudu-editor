package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.core.JSBoolean;

import java.util.function.IntConsumer;

public interface JsIFolderDiffView extends JsTwoPanelDiff {
  boolean isReady();
  JsDisposable onReadyChanged(JsFunctions.Consumer<JSBoolean> callback);

  static IntConsumer toJava(JsFunctions.Consumer<JSBoolean> callback) {
    return i -> callback.f(JSBoolean.valueOf(i != 0));
  }
}
