package org.sudu.experiments.esm;

import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

public interface JsIFileDiffView extends JsTwoPanelDiff {
  JsITextModel getLeftModel();
  JsITextModel getRightModel();
  void setAutoSave(JSString autoSave, JSNumber autoSaveDelay);
}
