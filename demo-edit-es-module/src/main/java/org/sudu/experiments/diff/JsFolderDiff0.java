package org.sudu.experiments.diff;

import org.sudu.experiments.Debug;
import org.sudu.experiments.JsLauncher;

import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

public class JsFolderDiff0 implements JsFolderDiff {

  public final WebWindow window;
  protected FolderDiffScene scene;
  private float overrideFontSize = 0;

  protected JsFolderDiff0(WebWindow window, EditArgs args) {
    this.window = window;
    this.scene = (FolderDiffScene) window.scene();
    if (args.hasTheme()) setTheme(args.getTheme());
  }

  @Override
  public final void dispose() {
    window.dispose();
    System.out.println("debug: JsFolderDiff disposed");
    scene = null;
  }

  @Override
  public boolean isReady() {
    return scene.w.finished;
  }

  @Override
  public JsDisposable onReadyChanged(JsFunctions.Consumer<JSBoolean> callback) {
    var d = scene.w.rootView.stateListeners.disposableAdd(
        JsFolderDiff.toJava(callback));
    return JsDisposable.of(d);
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
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    scene.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void setFontFamily(JSString fontFamily) {
//    diff.setFontFamily(fontFamily.stringValue());
  }


  @Override
  public void setFontSize(float fontSize) {
    overrideFontSize = fontSize;
    var theme = scene.w.getTheme().withFontModified(fontSize);
    scene.applyTheme(theme);
  }

  @Override
  public void setTheme(JSObject jsTheme) {
    var theme = ThemeImport.fromJs(jsTheme);
    if (theme != null) {
      if (overrideFontSize > 0)
        theme = theme.withFontModified(overrideFontSize);
      scene.applyTheme(theme);
    } else {
      Debug.consoleInfo("unknown theme: " + theme);
    }
  }

  public static Promise<JsFolderDiff> newDiff(EditArgs arguments) {
    return JsLauncher.start(
        arguments,
        FolderDiffScene::new,
        JsFolderDiff0::new);
  }
}
