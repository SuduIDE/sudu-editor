package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

public interface JsCodeEditor extends JsBaseControl {
  void setText(JSString t);
  JSString getText();
  void setModel(JsITextModel model);
  void setPosition(JsPosition selectionOrPosition);
  JsPosition getPosition();
  JsITextModel getModel();

  JsDisposable registerDefinitionProvider(JSObject languageSelector, JsDefinitionProvider provider);
  JsDisposable registerDeclarationProvider(JSObject languageSelector, JsDeclarationProvider provider);
  JsDisposable registerReferenceProvider(JSObject languageSelector, JsReferenceProvider provider);
  JsDisposable registerDocumentHighlightProvider(JSObject languageSelector, JsDocumentHighlightProvider provider);
  JsDisposable registerEditorOpener(JsCodeEditorOpener opener);
  void revealLineInCenter(int line);
  void revealPosition(JsPosition position);
  void revealLine(int line);

  JsDisposable onDidChangeModel(JsFunctions.Consumer<JsIModelChangedEvent> f);

}
