package org.sudu.experiments.esm;

import org.sudu.experiments.demo.Model;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

import java.util.Arrays;

public class JsTextModel implements JsITextModel {

  public final Model javaModel;

  @JSProperty native JsUri getUri();

  @JSProperty native JSString getLanguage();

  @JSBody(params = {"model", "uri"}, script = "model.uri = uri;")
  public static native void setUri(JsITextModel model, JsUri uri);

  @JSBody(params = {"model", "language"}, script = "model.language = language;")
  public static native void setLanguage(JsITextModel model, String language);

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

  public JsTextModel(String[] text, String language, JsUri uri) {
    this.javaModel = new Model(text, language, uri.toJava());
    setLanguage(this, language);
    setUri(this, uri);
  }

  public JsTextModel(Model javaModel) {
    this.javaModel = javaModel;
    setLanguage(this, javaModel.language);
    setUri(this, JsUri.fromJava(javaModel.uri));
  }

  public static JsITextModel newTextModel(JSString text, JSString language, JsUri uri) {
    return new JsTextModel(splitText(text), language.stringValue(), uri);
  }

  static String[] splitText(JSString t) {
    String[] res = new String[8];
    char[] buffer = new char[16];
    int column = 0;
    int line = 0;
    for (int i = 0; i != t.getLength(); ++i) {
      char codeAt = (char) t.charCodeAt(i);
      if (codeAt == '\n') {
        if (res.length == line) {
          res = Arrays.copyOf(res, res.length * 2);
        }
        res[line++] = new String(buffer, 0, column);
        column = 0;
      } else {
        if (buffer.length == column) {
          buffer = Arrays.copyOf(buffer, buffer.length * 2);
        }
        buffer[column++] = codeAt;
      }
    }
    if (column > 0) {
      if (res.length == line) {
        res = Arrays.copyOf(res, res.length + 1);
      }
      res[line++] = new String(buffer, 0, column);
    }
    res = Arrays.copyOf(res, line);
    return res;
  }

  public static JsITextModel fromJava(Model model) {
    return new JsTextModel(model);
  }
}
