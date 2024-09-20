package org.sudu.experiments.diff;

import org.sudu.experiments.esm.JsBaseControl;
import org.sudu.experiments.esm.JsITextModel;

// interface CodeDiffView from editor.d.ts
public interface JsCodeDiff extends JsBaseControl {
    void setLeftModel(JsITextModel model);
    void setRightModel(JsITextModel model);
    JsITextModel getLeftModel();
    JsITextModel getRightModel();
}
