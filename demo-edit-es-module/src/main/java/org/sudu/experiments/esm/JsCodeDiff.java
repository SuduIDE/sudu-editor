package org.sudu.experiments.esm;

public interface JsCodeDiff extends JsBaseControl {
    void setLeftModel(JsITextModel model);
    void setRightModel(JsITextModel model);
    JsITextModel getLeftModel();
    JsITextModel getRightModel();
}
