package org.sudu.experiments.esm;

import org.teavm.jso.JSProperty;

public interface JsSideDiffView extends JsView {
  void setModel(JsITextDiffModel model);

  @JSProperty
  boolean getLeft();

  int getVScrollPos();
  void setVScrollPos(int vScrollPos);
  void setVScrollListener(JsIEditorView.VScrollListener listener);
}
