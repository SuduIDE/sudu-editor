package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsDisposable;
import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsICodeEditorView extends JsView {
  void setText(JSString t);
  JSString getText();
  void setPosition(JsPosition selectionOrPosition);
  JsPosition getPosition();

  JsITextModel getModel();

  JsDisposable registerDefinitionProvider(JSObject languageSelector, JsDefinitionProvider provider);
  JsDisposable registerDeclarationProvider(JSObject languageSelector, JsDeclarationProvider provider);
  JsDisposable registerReferenceProvider(JSObject languageSelector, JsReferenceProvider provider);
  JsDisposable registerDocumentHighlightProvider(JSObject languageSelector, JsDocumentHighlightProvider provider);
  JsDisposable registerEditorOpener(JsCodeEditorOpener opener);
  void revealLineInCenter(int line);
  void revealLine(int line);
  void revealPosition(JsPosition position);
}
