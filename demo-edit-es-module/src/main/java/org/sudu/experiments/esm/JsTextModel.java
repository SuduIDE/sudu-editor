package org.sudu.experiments.esm;

import org.sudu.experiments.demo.Document;
import org.sudu.experiments.demo.Model;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.core.JSString;

public class JsTextModel implements JsITextModel {

  public final Model javaModel;
  public final JsUri jsUri;
  public final JSString jsLanguage;

  public JsTextModel(JSString text, JSString language, JsUri uri) {
    String[] split = SplitJsText.split(text, Document.newLine);
    String lang = JsHelper.toString(language, null);
    javaModel = new Model(split, lang, uri.toJava());
    javaModel.platformObject = this;
    jsLanguage = language;
    jsUri = uri;
  }

  private JsTextModel(Model model) {
    javaModel = model;
    javaModel.platformObject = this;
    jsUri = null;
    jsLanguage = null;
  }

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

  @Override
  public JsUri getUri() {
    return jsUri != null ? jsUri : JsUri.fromJava(javaModel.uri);
  }

  @Override
  public JSString getLanguage() {
    return jsLanguage != null ? jsLanguage : JSString.valueOf(javaModel.docLanguage());
  }

  public static JsTextModel fromJava(Model model) {
    if (model.platformObject == null) {
      return new JsTextModel(model);
    }
    return (JsTextModel)model.platformObject;
  }
}
