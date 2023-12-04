package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsArrayReader;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSBoolean;
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
  void setPosition(JsPosition selectionOrPosition);
  JsPosition getPosition();
  JsITextModel getModel();

  Promise<JsArrayReader<JSString>> executeOnWorker(JSString method, JsArrayReader<JSString> args);

  JSString getProperty(JSString key);
  JsDisposable registerDefinitionProvider(JSObject languageSelector, JsDefinitionProvider provider);
  JsDisposable registerDeclarationProvider(JSObject languageSelector, JsDeclarationProvider provider);
  JsDisposable registerReferenceProvider(JSObject languageSelector, JsReferenceProvider provider);
  JsDisposable registerDocumentHighlightProvider(JSObject languageSelector, JsDocumentHighlightProvider provider);
  JsDisposable registerEditorOpener(JsCodeEditorOpener opener);
  void revealLineInCenter(int line);
  void revealPosition(JsPosition position);
  void revealLine(int line);
  void setReadonly(JSBoolean flag);

  JsDisposable onDidChangeModel(JsFunctions.Consumer<JsIModelChangedEvent> f);

  interface EditArgs extends JSObject {
    @JSProperty JSString getContainerId();

    String workerUrlProperty = "workerUrl";
    @JSProperty JSString getWorkerUrl();

    String themeProperty = "theme";
    @JSProperty JSString getTheme();

    default boolean hasTheme() {
      return JSObjects.hasProperty(this, themeProperty);
    }

    String readonlyProperty = "readonly";
    @JSProperty JSBoolean getReadonly();

    default boolean hasReadonly() {
      return JSObjects.hasProperty(this, readonlyProperty);
    }

    default JSString workerUrl() {
      return JSObjects.hasProperty(this, workerUrlProperty)
          ? getWorkerUrl() : JSString.valueOf("worker.js");
    }

    String numThreadsProperty = "numThreads";
    @JSProperty double getNumThreads();

    default int numWorkerThreads() {
      return JSObjects.hasProperty(this, numThreadsProperty)
          ? (int) getNumThreads() : 2;
    }
  }
}
