package org.sudu.experiments;

import org.sudu.experiments.esm.EditArgs;
import org.sudu.experiments.esm.JsView;
import org.sudu.experiments.fonts.FontConfigJs;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.js.*;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.JSObject;

import java.util.function.Function;

public abstract class JsLauncher {

  boolean fontsLoaded;
  private JsArray<WebWorkerContext> workers;

  JsLauncher(boolean waitFonts) {
    fontsLoaded = !waitFonts;
  }

  void onWorkersStart(JsArray<WebWorkerContext> workers) {
    this.workers = workers;
    if (fontsLoaded) launch(workers);
  }

  void onFontsLoaded(JsArrayReader<JSObject> fontFaces) {
    FontFace.addToDocument(fontFaces);
    fontsLoaded = true;
    if (workers != null) launch(workers);
  }

  abstract void launch(JsArray<WebWorkerContext> workers);

  public interface ControlFactory <T extends JsView> {
    T apply(WebWindow w, EditArgs u);
  }

  public static <T extends JsView> Promise<T> start(
      EditArgs arguments,
      Function<SceneApi, Scene> sf,
      ControlFactory<T> rf
  ) {
    if (!JsCanvas.checkFontMetricsAPI())
      return Promise.reject(FireFoxWarning.message);

    return Promise.create((postResult, postError) -> {
      boolean loadCodicon = arguments.hasCodiconUrl();
      var l = new JsLauncher(loadCodicon) {
        @Override
        public void launch(JsArray<WebWorkerContext> workers) {
          var w = new WebWindow(arguments.getContainerId(), workers);
          if (w.init(sf)) {
            postResult.f(rf.apply(w, arguments));
          } else {
            postError.f(JsHelper.newError(WebGLError.text));
          }
        }
      };

      if (loadCodicon) {
        var url = arguments.getCodiconUrl().stringValue();
        FontFace.loadFonts(codiconFontConfig(url))
            .then(l::onFontsLoaded,
                e -> postError.f(e.cast()));
      }
      WebWorkerContext.start(
          l::onWorkersStart,
          postError,
          arguments.workerUrl(),
          arguments.numWorkerThreads());
    });
  }

  static FontConfigJs[] codiconFontConfig(String filename) {
    return ArrayOp.array(
        new FontConfigJs(Fonts.codicon, filename,
            FontDesk.NORMAL, FontDesk.WEIGHT_REGULAR));
  }
}
