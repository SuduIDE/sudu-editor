package org.sudu.experiments.esm;

public interface JsITextModel extends JsDisposable {
  int getOffsetAt(JsPosition position);
  JsPosition getPositionAt(int offset);
}
