package org.sudu.experiments.diff;


import org.sudu.experiments.Channel;
import org.sudu.experiments.JsLauncher;
import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.EditArgs;
import org.sudu.experiments.esm.JsFolderDiff;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class JsRemoteFolderDiff0 extends JsFolderDiff0 {

  protected JsRemoteFolderDiff0(WebWindow window, EditArgs args) {
    super(window, args);
    this.folderDiff = (RemoteFolderDiffScene) window.scene();
  }

  static void start(
      EditArgs arguments,
      JsArray<WebWorkerContext> workers,
      JsFunctions.Consumer<JsFolderDiff> postResult,
      JsFunctions.Consumer<JSObject> postError,
      Channel channel
  ) {
    var window = new WebWindow(
        arguments.getContainerId(), workers);
    if (window.init(api -> new RemoteFolderDiffScene(api, channel))) {
      postResult.f(new JsFolderDiff0(window, arguments));
    } else {
      postError.f(JSString.valueOf(WebGLError.text));
    }
  }

  public static Promise<JsFolderDiff> newDiff(EditArgs arguments, Channel channel) {
    return Promise.create((postResult, postError) -> {
      boolean loadCodicon = arguments.hasCodiconUrl();
      var l = new JsLauncher(loadCodicon) {
        @Override
        public void launch(JsArray<WebWorkerContext> workers) {
          start(arguments, workers, postResult, postError, channel);
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

}
