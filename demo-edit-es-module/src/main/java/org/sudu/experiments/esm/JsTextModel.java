package org.sudu.experiments.esm;

import org.sudu.experiments.demo.Document;
import org.sudu.experiments.demo.Model;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.parser.common.Pos;
import org.teavm.jso.JSBody;
import org.teavm.jso.core.JSString;

public class JsTextModel implements JsITextModel {

  public final Model javaModel;

  public final JsUri getUri() { return getUri(this); }

  public final JSString getLanguage() { return getLanguage(this); }

  @JSBody(params = {"model", "uri"}, script = "model.uri = uri;")
  public static native void setUri(JsITextModel model, JsUri uri);

  @JSBody(params = {"model"}, script = "return model.uri;")
  public static native JsUri getUri(JsITextModel model);

  @JSBody(params = {"model", "language"}, script = "model.language = language;")
  public static native void setLanguage(JsITextModel model, JSString language);

  @JSBody(params = {"model"}, script = "return model.language;")
  public static native JSString getLanguage(JsITextModel model);

  @Override
  public void dispose() {
    javaModel.document.clear();
  }

  @Override
  public int getOffsetAt(JsPosition position) {
    return javaModel.document.getOffsetAt(
        position.getLineNumber() - 1, position.getColumn() - 1);
  }

  @Override
  public JsPosition getPositionAt(int offset) {
    return JsPosition.fromJava(javaModel.document.getPositionAt(offset));
  }

  public JsTextModel(JSString text, JSString language, JsUri uri) {
    String[] split = SplitJsText.split(text, Document.newLine);
    this.javaModel = new Model(split,
        JsHelper.toString(language, null),
        uri.toJava(), this);
    setLanguage(this, language);
    setUri(this, uri);
  }

  public JsTextModel(Model javaModel) {
    this.javaModel = javaModel;
    setLanguage(this, JSString.valueOf(javaModel.docLanguage()));
    setUri(this, JsUri.fromJava(javaModel.uri));
  }

  public static JsTextModel fromJava(Model model) {
    if (model.platformObject == null) {
      model.platformObject = new JsTextModel(model);
    }
    return (JsTextModel)model.platformObject;
  }
}
