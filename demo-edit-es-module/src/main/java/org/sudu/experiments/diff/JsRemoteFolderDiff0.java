package org.sudu.experiments.diff;

import org.sudu.experiments.*;
import org.sudu.experiments.esm.EditArgs;
import org.sudu.experiments.esm.JsFolderDiff;
import org.sudu.experiments.js.*;
import org.teavm.jso.core.JSString;

import java.util.function.Function;

public class JsRemoteFolderDiff0 implements JsFolderDiff {

  public final WebWindow window;
  protected RemoteFolderDiffScene folderDiff;

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
//    diff.setFontSize(fontSize);
  }

  @Override
  public void setTheme(JSString theme) {
    folderDiff.setTheme(theme.stringValue());
  }

  static Function<SceneApi, Scene> sf(Channel channel) {
    return api -> new RemoteFolderDiffScene(api, channel);
  }

  public static Promise<JsFolderDiff> newDiff(EditArgs arguments, Channel channel) {
    return JsLauncher.start(arguments,
        sf(channel), JsRemoteFolderDiff0::new);
  }
}
