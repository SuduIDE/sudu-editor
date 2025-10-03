package org.sudu.experiments.diff;

import org.sudu.experiments.LoggingJs;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsBinaryDiffViewController0 implements JsBinaryDiffViewController {

  final BinaryDiffWindow w;

  public JsBinaryDiffViewController0(BinaryDiffWindow w) {
    this.w = w;
  }

  @Override
  public JSString getViewType() {
    return JSString.valueOf("binaryDiff");
  }

  @Override
  public JsDiffSelection getSelection() {
    return JSObjects.undefined().cast();
  }

  @Override
  public boolean canNavigateUp() {
    LoggingJs.debug("JsBinaryDiffViewController0.canNavigateUp");
    return w.canNavigateUp();
  }

  @Override
  public void navigateUp() {
    LoggingJs.debug("JsBinaryDiffViewController0.navigateUp");
    w.navigateUp();
  }

  @Override
  public boolean canNavigateDown() {
    LoggingJs.debug("JsBinaryDiffViewController0.canNavigateDown");
    return w.canNavigateDown();
  }

  @Override
  public void navigateDown() {
    LoggingJs.debug("JsBinaryDiffViewController0.navigateDown");
    w.navigateDown();
  }

  @Override
  public void refresh() {
    LoggingJs.debug("JsBinaryDiffViewController0.refresh");
    w.refresh();
  }
}
