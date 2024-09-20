package org.sudu.experiments.diff;

import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsFileDiffViewController0 implements JsFileDiffViewController {
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
    return false;
  }

  @Override
  public void navigateUp() {}

  @Override
  public boolean canNavigateDown() {
    return false;
  }

  @Override
  public void navigateDown() {}
}
