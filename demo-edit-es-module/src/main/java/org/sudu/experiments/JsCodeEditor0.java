package org.sudu.experiments;

import org.sudu.experiments.demo.*;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.sudu.experiments.utils.LanguageSelectorUtils;
import org.sudu.experiments.utils.PromiseUtils;
import org.sudu.experiments.utils.ProviderUtils;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class JsCodeEditor0 implements JsCodeEditor {

  private final EditArguments args;
  private final WebWindow window;
  private final EditorComponent editor;

  public JsCodeEditor0(EditArguments args, WorkerContext worker) {
    this.args = args;

    this.window = new WebWindow(
        DemoEdit0::new,
        JsCodeEditor0::onWebGlError,
        args.getContainerId().stringValue(),
        worker);
    editor = ((DemoEdit0) window.scene()).editor();
    if (args.hasTheme()) setTheme(args.getTheme());
  }

  @Override
  public void dispose() {
    window.dispose();
  }

  @Override
  public void focus() {
    if (1<0) JsHelper.consoleInfo("setting focus to ",
        JsHelper.WithId.get(window.canvasDiv));
    window.focus();
  }

  @Override
  public void setText(JSString t) {
    char[] buffer = TextEncoder.toCharArray(t);
    editor.setText(buffer);
  }

  @Override
  public JSString getText() {
    char[] chars = editor.model().document.getChars();
    return window.decoderUTF16.decode(chars);
  }

  @Override
  public void setFontFamily(JSString fontFamily) {
    editor.changeFont(fontFamily.stringValue(), editor.getFontVirtualSize());
  }

  @Override
  public void setFontSize(int fontSize) {
    editor.changeFont(editor.getFontFamily(), fontSize);
  }

  @Override
  public void setTheme(JSString theme) {
    editor.setTheme(theme.stringValue());
  }

  @Override
  public void setModel(JsITextModel model) {
    if (model instanceof JsTextModel jsTextModel) {
      editor.setModel(jsTextModel.javaModel);
    } else {
      throw new IllegalArgumentException("bad model");
    }
  }

  @Override
  public void setPosition(JSObject selectionOrPosition) {
    if (JsPosition.isInstance(selectionOrPosition)) {
      JsPosition pos = selectionOrPosition.cast();
      editor.setPosition(pos.getColumn(), pos.getLineNumber());
    } else {
      JsRange sel = selectionOrPosition.cast();
      editor.setSelection(
          sel.getEndColumn(),
          sel.getEndLineNumber(),
          sel.getStartColumn(),
          sel.getStartLineNumber()
      );
    }
  }

  @Override
  public JsITextModel getModel() {
    return JsTextModel.fromJava(editor.model());
  }

  @Override
  public JsDisposable registerDefinitionProvider(JSObject languageSelector, JsDefinitionProvider provider) {
    var defProvider = new DefDeclProvider(
        LanguageSelectorUtils.languageSelectors(languageSelector),
        convert(provider));
    editor.registrations().registerDefinitionProvider(defProvider);
    return () -> editor.registrations().removeDefinitionProvider(defProvider);
  }

  @Override
  public JsDisposable registerDeclarationProvider(JSObject languageSelector, JsDeclarationProvider provider) {
    var defProvider = new DefDeclProvider(
        LanguageSelectorUtils.languageSelectors(languageSelector),
        convert(provider));
    editor.registrations().registerDeclarationProvider(defProvider);
    return () -> editor.registrations().removeDeclarationProvider(defProvider);
  }

  @Override
  public JsDisposable registerReferenceProvider(JSObject languageSelector, JsReferenceProvider provider) {
    var referenceProvider = new ReferenceProvider(
        LanguageSelectorUtils.languageSelectors(languageSelector), convert(provider));
    editor.registrations().registerReferenceProvider(referenceProvider);
    return () -> editor.registrations().removeReferenceProvider(referenceProvider);
  }

  static DefDeclProvider.Provider convert(JsDefinitionProvider provider) {
    return (model, line, column, onResult, onError) ->
        PromiseUtils.<JSArray<JsLocation>>promiseOrT(
            provider.provideDefinition(
                JsTextModel.fromJava(model),
                JsPosition.create(column, line),
                JsCancellationToken.create()),
            jsArr -> acceptResult(jsArr, onResult, onError),
            onError);
  }

  static DefDeclProvider.Provider convert(JsDeclarationProvider provider) {
    return (model, line, column, onResult, onError) ->
        PromiseUtils.<JSArray<JsLocation>>promiseOrT(
            provider.provideDeclaration(
                JsTextModel.fromJava(model),
                JsPosition.create(column, line),
                JsCancellationToken.create()),
            jsArr -> acceptResult(jsArr, onResult, onError),
            onError);
  }

  static ReferenceProvider.Provider convert(JsReferenceProvider provider) {
    return (model, line, column, includeDecl, onResult, onError) ->
        PromiseUtils.<JSArray<JsLocation>>promiseOrT(
            provider.provideReferences(
                JsTextModel.fromJava(model),
                JsPosition.create(column, line),
                JsReferenceProvider.Context.create(includeDecl),
                JsCancellationToken.create()),
            jsArr -> acceptResult(jsArr, onResult, onError),
            onError);
  }

  static void acceptResult(JSArray<JsLocation> jsArr, Consumer<Location[]> c, Consumer<String> onError) {
    if (JSArray.isArray(jsArr)) {
      c.accept(ProviderUtils.toLocations(jsArr));
    } else {
      onError.accept("provideDefinition result is not an array");
    }
  }


  @Override
  public JsDisposable registerDocumentHighlightProvider(JSObject languageSelector, JsDocumentHighlight provider) {
    return JsDisposable.empty();
  }

  @Override
  public JsDisposable registerEditorOpener(JsCodeEditorOpener opener) {
    return JsDisposable.empty();
  }

  @Override
  public JsDisposable onDidChangeModel(JsFunctions.Consumer<JsIModelChangedEvent> f) {
    var listener = convert(f);
    editor.registrations().addModelChangeListener(listener);
    return () -> editor.registrations().removeModelChangeListener(listener);
  }

  static BiConsumer<Model, Model> convert(JsFunctions.Consumer<JsIModelChangedEvent> jsCallback) {
    return (oldModel, newModel) -> jsCallback.f(
        JsIModelChangedEvent.create(
            JsTextModel.fromJava(oldModel).getUri(),
            JsTextModel.fromJava(newModel).getUri()
        ));
  }

  static void onWebGlError() {
    JsHelper.consoleInfo("FATAL: WebGL is not enabled in the browser");
  }

  static Promise<JsCodeEditor> newEdit(EditArguments arguments) {
    if (JsCanvas.checkFontMetricsAPI()) {
      return Promise.create((postResult, postError) ->
          WorkerContext.start(
              worker -> postResult.f(new JsCodeEditor0(arguments, worker)),
              postError, arguments.workerUrl()));
    } else {
      return Promise.reject(FireFoxWarning.message);
    }
  }
}
