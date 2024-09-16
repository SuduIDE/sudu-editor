package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.esm.EditArgs;
import org.sudu.experiments.esm.JsFolderDiff;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

import java.util.function.Function;

public class JsRemoteFolderDiff0 implements JsRemoteFolderDiff {

  public final WebWindow window;
  protected RemoteFolderDiffScene folderDiff;

  private float overrideFontSize = 0;

  protected JsRemoteFolderDiff0(WebWindow window, EditArgs args) {
    this.window = window;
    this.folderDiff = (RemoteFolderDiffScene) window.scene();
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
    overrideFontSize = fontSize;
    var theme = folderDiff.getTheme().withFontSize(fontSize);
    folderDiff.applyTheme(theme);
  }

  @Override
  public void setTheme(JSString themeStr) {
    var theme = ThemeControl.resolveTheme(themeStr.stringValue());
    if (theme != null) {
      if (overrideFontSize > 0)
        theme = theme.withFontSize(overrideFontSize);
      folderDiff.applyTheme(theme);
    } else {
      Debug.consoleInfo("unknown theme: " + theme);
    }
  }

  @Override
  public JSObject getState() {
    return JSString.valueOf("state");
  }

  @Override
  public void applyState(JSObject state) {
    Debug.consoleInfo("JsRemoteFolderDiff.applyState: ", state);
  }

  @Override
  public boolean isReady() {
    return folderDiff.w.finished;
  }

  @Override
  public JsDisposable onReadyChanged(JsFunctions.Consumer<JSBoolean> callback) {
    var d = rootView().stateListeners.disposableAdd(
        JsFolderDiff.toJava(callback));
    return JsDisposable.of(d);
  }

  @Override
  public JsFolderDiffViewSelection getSelected() {
    var s = folderDiff.w.getSelected();
    return JsFolderDiffViewSelection.H.create(s);
  }

  @Override
  public JsDisposable onSelectionChanged(
      JsFunctions.Consumer<JsFolderDiffViewSelection> callback
  ) {
    var h = JsFolderDiffViewSelection.H.toJava(callback);
    var d = rootView().selectionListeners.disposableAdd(h);
    return JsDisposable.of(d);
  }

  private FolderDiffRootView rootView() {
    return folderDiff.w.rootView;
  }

  static Function<SceneApi, Scene> sf(Channel channel) {
    return api -> new RemoteFolderDiffScene(api, channel);
  }

  public static Promise<JsFolderDiff> newDiff(EditArgs arguments, Channel channel) {
    return JsLauncher.start(arguments,
        sf(channel), JsRemoteFolderDiff0::new);
  }
}
