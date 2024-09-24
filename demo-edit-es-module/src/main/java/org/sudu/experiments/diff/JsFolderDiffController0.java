package org.sudu.experiments.diff;

import org.sudu.experiments.js.JsArray;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

public class JsFolderDiffController0 implements JsFolderDiffController {

  RemoteFolderDiffWindow w;

  public JsFolderDiffController0(RemoteFolderDiffWindow controller) {
    this.w = controller;
  }

  @Override
  public JSString getViewType() {
    return JSString.valueOf("folderDiff");
  }

  @Override
  public JsFolderDiffSelection getSelection() {
    var selected = w.getSelected();
    return JsFolderDiffSelection.H.create(selected);
  }

  @Override
  public boolean canNavigateUp() {
    return w.canNavigateUp();
  }

  @Override
  public void navigateUp() {
    w.navigateUp();
  }

  @Override
  public boolean canNavigateDown() {
    return w.canNavigateDown();
  }

  @Override
  public void navigateDown() {
    w.navigateDown();
  }

  @Override
  public JsArray<JSNumber> getDiffFilter() {
    return JsHelper.toJs(w.getDiffFilter());
  }

  @Override
  public void applyDiffFilter(JsArray<JSNumber> diffFilter) {
    w.applyDiffFilter(JsHelper.toJavaIntArray(diffFilter));
  }

  @Override
  public void refresh() {
    w.refresh();
  }
}
