package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.core.JSBoolean;

public interface JsIFileDiffView extends JsTwoPanelDiff {
  JsITextModel getLeftModel();
  JsITextModel getRightModel();
  void setRequestSemanticHighlight(JsFunctions.Consumer<JSBoolean> listener);
}
