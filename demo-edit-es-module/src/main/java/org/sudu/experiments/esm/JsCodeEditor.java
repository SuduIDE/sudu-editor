package org.sudu.experiments.esm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public interface JsCodeEditor extends JsDisposable {
  void focus();
  void setText(JSString t);
  JSString getText();
  void setFontFamily(JSString fontFamily);
  void setFontSize(int fontSize);
  void setTheme(JSString theme);
  void setModel(JsITextModel model);
  void setPosition(JSObject selectionOrPosition);
  JsITextModel getModel();
  JsDisposable registerDefinitionProvider(JSObject languageSelector, JsDefinitionProvider provider);
  JsDisposable registerReferenceProvider(JSObject languageSelector, JsReferenceProvider provider);
  JsDisposable registerDocumentHighlightProvider(JSObject languageSelector, JsDocumentHighlight provider);
  JsDisposable registerEditorOpener(JsCodeEditorOpener opener);

  interface EditArguments extends JSObject {
    @JSProperty JSString getContainerId();

    String workerUrlProperty = "workerUrl";
    @JSProperty JSString getWorkerUrl();

    String themeProperty = "theme";
    @JSProperty JSString getTheme();

    default boolean hasTheme() {
      return JSObjects.hasProperty(this, themeProperty);
    }

    default JSString workerUrl() {
      return JSObjects.hasProperty(this, workerUrlProperty)
          ? getWorkerUrl() : JSString.valueOf("worker.js");
    }
  }
}
