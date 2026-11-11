package org.sudu.experiments.diff;

import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsSideDiff implements JsSideDiffView {

  public final WebWindow window;
  private SideDiffWindow w;
  private final JsEditorViewController controller;

  public JsSideDiff(
      SideDiffWindow sideDiff, WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = sideDiff;
    controller = new JsEditorViewController0();
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      w.rootView.setReadonly(args.getReadonly(), args.getReadonly());
  }

  @Override
  @JSProperty
  public boolean getLeft() {
    return w.rootView.left;
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
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public void setModel(JsITextDiffModel model) {
    if (!(model instanceof JsTextDiffModel jsModel)) {
      boolean undefined = JSObjects.isUndefined(model);
      throw new IllegalArgumentException(
          undefined ? "Model is undefined" : "Bad model: " + JsHelper.stringify(model));
    }
    w.rootView.setModel(jsModel.javaModel);
  }

  @Override
  public int getVScrollPos() {
    return w.rootView.editor.getVScrollPos();
  }

  @Override
  public void setVScrollPos(int vScrollPos) {
    if (w.rootView.editor.setVScrollPos(vScrollPos)) {
      window.repaint();
    }
  }

  @Override
  public void setVScrollListener(JsIEditorView.VScrollListener listener) {
    if (listener == null) {
      w.rootView.editor.setVScrollObserver(null);
    } else {
      w.rootView.editor.setVScrollObserver(listener::onVScroll);
    }
  }

  @Override
  public void setExternalMessageBar(JsExternalMessageBar emb) {}

  @Override
  public void setExternalContextMenuProvider(JsContextMenuProvider p) {}

  @Override
  public void setJsNotificationsProvider(JsNotificationsProvider p) {}

  @Override
  public void executeMenuAction(JSString action) {}

  public static JsSideDiffView newSideDiff(EditArgs arguments, boolean left) {
    if (!JsCanvas.checkFontMetricsAPI())
      throw new RuntimeException(FireFoxWarning.message);

    var w = new WebWindow(arguments.getContainerId(),
        EditArgs.getPool(arguments), false);

    if (w.api() == null)
      throw new RuntimeException(WebGLError.text);

    var sideDiff = new SideDiff(w.api(), arguments.getDisableParserOrDefault(), left);
    w.setScene(sideDiff);
    return new JsSideDiff(sideDiff.w, w, arguments);
  }
}
