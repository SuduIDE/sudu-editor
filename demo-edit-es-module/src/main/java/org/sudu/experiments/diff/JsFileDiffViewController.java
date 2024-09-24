package org.sudu.experiments.diff;

import org.teavm.jso.core.JSString;

public interface JsFileDiffViewController extends JsDiffViewController {
  // returns 'fileDiff'
  JSString getViewType();

  //  getSelection(): FileDiffSelection | undefined
  JsDiffSelection getSelection();
}
