package org.sudu.experiments.esm;

import org.teavm.jso.JSProperty;

public interface JsSideDiffView extends JsView {
  void setModel(JsITextDiffModel model);

  @JSProperty
  boolean getLeft();
}
