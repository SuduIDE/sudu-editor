package org.sudu.experiments;

import org.sudu.experiments.esm.EditArgs;
import org.sudu.experiments.esm.JsView;
import org.sudu.experiments.fonts.FontConfigJs;
import org.sudu.experiments.js.*;

import java.util.function.Function;

public interface ControlFactory<T extends JsView> {
  T apply(WebWindow w, EditArgs u);

  static <T extends JsView> Promise<T> start(
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
        FontFace.loadFonts(FontConfigJs.codiconFontConfig(url))
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
}
