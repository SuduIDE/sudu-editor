package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

public interface JsCodeDiff extends JsThemeTarget {
    void focus();
    void dispose();
    void setLeftModel(JsITextModel model);
    void setRightModel(JsITextModel model);
    JsITextModel getLeftModel();
    JsITextModel getRightModel();
    void setReadonly(JSBoolean flag);
}
