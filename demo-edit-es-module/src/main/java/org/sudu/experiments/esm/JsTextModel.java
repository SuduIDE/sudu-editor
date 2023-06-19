package org.sudu.experiments.esm;

import org.sudu.experiments.demo.Model;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public class JsTextModel implements JsITextModel {

  public final Model javaModel;

  @JSProperty native JsUri getUri();

  @JSProperty native JSString getLanguage();

  @JSBody(params = {"model", "uri"}, script = "model.uri = uri;")
  public static native void setUri(JsITextModel model, JsUri uri);

  @JSBody(params = {"model", "language"}, script = "model.language = language;")
  public static native void setLanguage(JsITextModel model, JSString language);

  @Override
  public void dispose() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public int getOffsetAt(JsPosition position) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public JsPosition getPositionAt(int offset) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public JsTextModel(JSString text, JSString language, JsUri uri) {
    String[] split = SplitJsText.split(text, '\n');
    this.javaModel = new Model(split, language.stringValue(), uri.toJava(), this);
    setLanguage(this, language);
    setUri(this, uri);
  }

  public JsTextModel(Model javaModel) {
    this.javaModel = javaModel;
    setLanguage(this, JSString.valueOf(javaModel.language));
    setUri(this, JsUri.fromJava(javaModel.uri));
  }

  public static JsITextModel fromJava(Model model) {
    if (model.platformObject == null) {
      model.platformObject = new JsTextModel(model);
    }
    return (JsTextModel)model.platformObject;
  }
}
