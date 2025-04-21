package org.sudu.experiments.esm;

public interface JsTwoPanelDiff extends JsView {
  void setReadonly(boolean leftReadonly, boolean rightReadonly);
  void setCompactView(boolean compact);
}
