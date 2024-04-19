package org.sudu.experiments.diff;

import org.sudu.experiments.JsLauncher;
import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.fonts.FontConfigJs;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.js.*;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class JsFolderDiff0 implements JsFolderDiff {

  public final WebWindow window;
  private FolderDiffScene folderDiff;

  static void start(
      EditArgs arguments,
      JsArray<WorkerContext> workers,
      JsFunctions.Consumer<JsFolderDiff> postResult,
      JsFunctions.Consumer<JSObject> postError
  ) {
    var window = new WebWindow(
        arguments.getContainerId(), workers);
    if (window.init(FolderDiffScene::new)) {
      postResult.f(new JsFolderDiff0(window, arguments));
    } else {
      postError.f(JSString.valueOf(WebGLError.text));
    }
  }

  private JsFolderDiff0(WebWindow window, EditArgs args) {
    this.window = window;
    this.folderDiff = (FolderDiffScene) window.scene();
    if (args.hasTheme()) setTheme(args.getTheme());
  }

  @Override
  public final void dispose() {
    window.dispose();
    System.out.println("debug: JsFolderDiff disposed");
    folderDiff = null;
  }

  @Override
  public void disconnectFromDom() {
    window.disconnectFromDom();
  }

  @Override
  public void reconnectToDom(JSString containedId) {
    window.connectToDom(containedId);
  }

  @Override
  public void focus() {
    if (1<0)
      JsHelper.consoleInfo("setting focus to ", window.canvasDivId());
    window.focus();
  }

  @Override
  public void setReadonly(boolean flag) {
    folderDiff.setReadonly(flag);
  }

  @Override
  public void setFontFamily(JSString fontFamily) {
//    diff.setFontFamily(fontFamily.stringValue());
  }

  @Override
  public void setFontSize(float fontSize) {
//    diff.setFontSize(fontSize);
  }

  @Override
  public void setTheme(JSString theme) {
    ThemeControl themeControl = folderDiff.themeControl();
    themeControl.setTheme(theme.stringValue());
  }

  public static Promise<JsFolderDiff> newDiff(EditArgs arguments) {
    if (!JsCanvas.checkFontMetricsAPI())
      return Promise.reject(FireFoxWarning.message);

    return Promise.create((postResult, postError) -> {
      boolean loadCodicon = arguments.hasCodiconUrl();
      var l = new JsLauncher(loadCodicon) {
        @Override
        public void launch(JsArray<WorkerContext> workers) {
          start(arguments, workers, postResult, postError);
        }
      };

      if (loadCodicon) {
        var url = arguments.getCodiconUrl().stringValue();
        FontFace.loadFonts(codiconFontConfig(url))
            .then(l::onFontsLoaded,
                e -> postError.f(e.cast()));
      }
      WorkerContext.start(
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
