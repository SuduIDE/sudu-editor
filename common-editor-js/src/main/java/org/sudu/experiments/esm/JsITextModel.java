package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;

public interface JsITextModel extends JsDisposable {
  @JSProperty JsUri getUri();
  @JSProperty JSString getLanguage();
  int getOffsetAt(JsPosition position);
  JsPosition getPositionAt(int offset);
  JSString getText();
  // setEditListener(listener: (m: ITextModel) => void): void
  void setEditListener(JsFunctions.Consumer<JsITextModel> listener);

  void setText(JSString newText, boolean fireEvent);
  void setSemanticTokens(JSArray<JSObject> legend, JSArray<JSObject> semanticTokens);
}
