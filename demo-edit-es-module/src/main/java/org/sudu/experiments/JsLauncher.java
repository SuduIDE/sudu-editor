package org.sudu.experiments;

import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;

public abstract class JsLauncher {

  boolean fontsLoaded;
  private JsArray<WebWorkerContext> workers;

  public JsLauncher(boolean waitFonts) {
    fontsLoaded = !waitFonts;
  }

  public void onWorkersStart(JsArray<WebWorkerContext> workers) {
    this.workers = workers;
    if (fontsLoaded) launch(workers);
  }

  public void onFontsLoaded(JsArrayReader<JSObject> fontFaces) {
    FontFace.addToDocument(fontFaces);
    fontsLoaded = true;
    if (workers != null) launch(workers);
  }

  public abstract void launch(JsArray<WebWorkerContext> workers);
}
