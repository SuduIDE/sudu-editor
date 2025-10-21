package org.sudu.experiments.diff;

import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsCodeReview implements JsCodeReviewView {

  public final WebWindow window;
  private FileDiffWindow w;
  private JsFileDiffViewController controller;
  private IDiffSizeChangeCallback sizeListener;
  private int eventNumLines, eventLineHeight;
  private float eventCssLineHeight;

  public JsCodeReview(
      FileDiffWindow fileDiff, WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = fileDiff;
    controller = new JsFileDiffViewController0(w);
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      setReadonly(args.getReadonly(), args.getReadonly());

    w.rootView.setOnDocumentSizeChange(this::onDocumentSizeChange);
  }

  void onDocumentSizeChange() {
    if (sizeListener != null && w.rootView.dpr != 0) {
      int numLines = Math.max(
          w.rootView.editor1.getNumLines(),
          w.rootView.editor2.getNumLines());
      int lineHeight = Math.max(
          w.rootView.editor1.lineHeight(),
          w.rootView.editor2.lineHeight());
      float cssLineHeight = lineHeight / w.rootView.dpr;
      if (numLines != eventNumLines
          || lineHeight != eventLineHeight
          || cssLineHeight != eventCssLineHeight) {
        sizeListener.f(
            eventNumLines = numLines,
            eventLineHeight = lineHeight,
            eventCssLineHeight = cssLineHeight);
      }
    }
  }

  @Override
  public void setDiffSizeListener(IDiffSizeChangeCallback listener) {
    sizeListener = listener;
    eventNumLines = eventLineHeight = 0;
    eventCssLineHeight = 0;
  }

  @Override
  public final void dispose() {
    window.dispose();
    w = null;
  }

  @Override
  public JsViewController getController() {
    return controller;
  }

  @Override
  public JsDisposable onControllerUpdate(JsFunctions.Consumer<JsViewController> callback) {
    return JsDisposable.empty();
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
    window.focus();
  }

  @Override
  public boolean hasFocus() {
    return window.hasFocus();
  }

  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    JsHelper.consoleInfo("JsCodeDiff0.setReadonly");
    w.rootView.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public void setModel(JsITextModel modelL, JsITextModel modelR) {
    if (!(modelL instanceof JsTextModel jsModelL)) {
      boolean undefined = JSObjects.isUndefined(modelL);
      throw new IllegalArgumentException(
          undefined ? "left model is undefined" : "bad left model");
    }

    if (!(modelR instanceof JsTextModel jsModelR)) {
      boolean undefined = JSObjects.isUndefined(modelR);
      throw new IllegalArgumentException(
          undefined ? "right model is undefined" : "bad right model");
    }

    w.rootView.setModel(jsModelL.javaModel, jsModelR.javaModel);
  }

  @Override
  public JsITextModel getLeftModel() {
    return JsTextModel.fromJava(w.rootView.getLeftModel());
  }

  @Override
  public JsITextModel getRightModel() {
    return JsTextModel.fromJava(w.rootView.getRightModel());
  }

  @Override
  public void setExternalMessageBar(JsExternalMessageBar emb) {}

  @Override
  public void setExternalContextMenuProvider(JsContextMenuProvider p) {}

  @Override
  public void setJsNotificationsProvider(JsNotificationsProvider p) {}

  @Override
  public void executeMenuAction(JSString action) {}

  public static JsCodeReviewView newCodeReview(EditArgs arguments) {
    if (!JsCanvas.checkFontMetricsAPI())
      throw new RuntimeException(FireFoxWarning.message);

    var w = new WebWindow(arguments.getContainerId(),
        EditArgs.getPool(arguments));

    if (w.api() == null)
      throw new RuntimeException(WebGLError.text);

    var fileDiff = new FileDiff(w.api(), arguments.getDisableParserOrDefault());
    w.setScene(fileDiff);
    return new JsCodeReview(fileDiff.w, w, arguments);
  }
}
