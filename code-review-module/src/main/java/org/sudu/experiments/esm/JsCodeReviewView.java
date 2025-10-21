package org.sudu.experiments.esm;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public interface JsCodeReviewView extends JsICodeReviewView {
  void setModel(JsITextModel modelL, JsITextModel modelR);

  @JSFunctor
  interface IDiffSizeChangeCallback extends JSObject {
    void f(int numLines, int lineHeight, float cssLineHeight);
  }

  void setDiffSizeListener(IDiffSizeChangeCallback listener);
}
