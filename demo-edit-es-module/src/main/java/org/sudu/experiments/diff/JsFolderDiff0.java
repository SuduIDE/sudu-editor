package org.sudu.experiments.diff;

import org.sudu.experiments.JsLauncher;

import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.core.JSString;

public class JsFolderDiff0 implements JsFolderDiff {

  public final WebWindow window;
  protected FolderDiffScene folderDiff;

  protected JsFolderDiff0(WebWindow window, EditArgs args) {
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
    folderDiff.setTheme(theme.stringValue());
  }

  public static Promise<JsFolderDiff> newDiff(EditArgs arguments) {
    return JsLauncher.start(
        arguments,
        FolderDiffScene::new,
        JsFolderDiff0::new);
  }
}
