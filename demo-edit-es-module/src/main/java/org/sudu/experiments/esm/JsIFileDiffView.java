package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsFunctions;

public interface JsIFileDiffView extends JsTwoPanelDiff {
  JsITextModel getLeftModel();
  JsITextModel getRightModel();
  void setRequestSemanticHighlight(JsFunctions.Consumer<JsITextModel> listener);
}
