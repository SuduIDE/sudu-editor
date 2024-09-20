package org.sudu.experiments.diff;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsFolderDiffViewController0 implements JsFolderDiffViewController {

  @Override
  public JSString getViewType() {
    return JSString.valueOf("folderDiff");
  }

  @Override
  public JsFolderDiffSelection getSelection() {
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
  public JsArray<JSNumber> getDiffFilter() {
    return JsArray.create();
  }

  @Override
  public void applyDiffFilter(JsArray<JSNumber> diffFilter) {}
}
