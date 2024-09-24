package org.sudu.experiments.diff;

import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsEditorViewController0 implements JsFileDiffViewController {

  @Override
  public JSString getViewType() {
    return JSString.valueOf("editor");
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

  @Override
  public void refresh() {
    // TODO
  }
}
