package org.sudu.experiments.esm;

import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class JsTextDiffModel implements JsITextDiffModel {

  public final JsTextModel modelL, modelR;
  public final FileDiffModel javaModel;

  public JsTextDiffModel(
      JSString text1, JSString text2,
      JsUri uri1, JsUri uri2,
      JSString language
  ) {
    modelL = new JsTextModel(text1, language, uri1);
    modelR = new JsTextModel(text2, language, uri2);
    javaModel = new FileDiffModel(modelL.javaModel, modelR.javaModel);
  }

  @JSFunctor
  public interface Api extends JSObject {
    JsTextDiffModel create(
        JSString text1, JSString text2,
        JsUri uri1, JsUri uri2,
        JSString language
    );

    class Setter {
      @JSBody(params = {"f"}, script = "newDiffModel = f;")
      static native void set(JsTextDiffModel.Api f);
    }

    static void install() {
      JsTextDiffModel.Api.Setter.set(JsTextDiffModel::new);
    }
  }

  @Override
  public JsITextModel getLeftModel() {
    return modelL;
  }

  @Override
  public JsITextModel getRightModel() {
    return modelR;
  }

  @Override
  public Promise<JsLinesInfo> getLinesInfo() {
    return Promise.create((onResult, onError) ->
        javaModel.getLinesInfo(ints ->
            onResult.f(JsLinesInfo.create(ints[0], ints[1], ints[2])))
    );
  }

  @Override
  public void dispose() {
    modelL.dispose();
    modelR.dispose();
  }
}
