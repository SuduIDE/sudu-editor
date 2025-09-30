package org.sudu.experiments.diff;

import org.teavm.jso.core.JSString;

public interface JsBinaryDiffViewController extends JsViewController {
  // returns 'binaryDiff'
  JSString getViewType();
}
