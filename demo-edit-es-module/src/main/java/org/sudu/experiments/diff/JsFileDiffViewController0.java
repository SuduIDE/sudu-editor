package org.sudu.experiments.diff;

import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsFileDiffViewController0 implements JsFileDiffViewController {

  final FileDiffWindow w;

  public JsFileDiffViewController0(FileDiffWindow w) {
    this.w = w;
  }

  @Override
  public JSString getViewType() {
    return JSString.valueOf("fileDiff");
  }

  @Override
  public JsDiffSelection getSelection() {
    return JSObjects.undefined().cast();
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
  public void refresh() {
    w.rootView.refresh();
  }
}
