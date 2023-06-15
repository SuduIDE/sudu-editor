package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public class JsTextModel implements JsITextModel {

  @JSProperty native JsUri getUri();
  @JSProperty native JSString getLanguage();

  @JSBody(params = {"model", "uri"}, script = "model.uri = uri;")
  public static native void setUri(JSObject model, JsUri uri);

  @JSBody(params = {"model", "language"}, script = "model.language = language;")
  public static native void setLanguage(JSObject model, String language);

  @Override
  public void dispose() {}

  @Override
  public int getOffsetAt(JsPosition position) {
    return 0;
  }

  @Override
  public JsPosition getPositionAt(int offset) {
    return JsPosition.create(0, 0);
  }

  public JsTextModel(String[] text, String language, JsUri uri) {
    setLanguage(this, language);
    setUri(this, uri);
  }

  public static JsITextModel newTextModel(JSString text, JSString language, JsUri uri) {
    return new JsTextModel(splitText(text), language.stringValue(), uri);
  }

  static String[] splitText(JSString jsText) {
    return new String[]{};
  }
}
