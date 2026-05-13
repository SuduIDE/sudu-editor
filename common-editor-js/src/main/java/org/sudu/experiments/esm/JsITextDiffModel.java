package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.Promise;

public interface JsITextDiffModel extends JsDisposable {
  JsITextModel getLeftModel();
  JsITextModel getRightModel();
  Promise<JsLinesInfo> getLinesInfo();
}
