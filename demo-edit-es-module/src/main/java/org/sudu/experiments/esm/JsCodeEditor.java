package org.sudu.experiments.esm;

import org.sudu.experiments.Debug;
import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.diff.JsEditorViewController;
import org.sudu.experiments.diff.JsEditorViewController0;
import org.sudu.experiments.diff.JsViewController;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.js.*;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.text.SplitJsText;
import org.sudu.experiments.utils.LanguageSelectorUtils;
import org.sudu.experiments.js.PromiseUtils;
import org.sudu.experiments.utils.ProviderUtils;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class JsCodeEditor implements JsEditorView {

  public static final String errorNotArray = "provided result is not an array";

  public final WebWindow window;
  private final EditorComponent editor;
  JsEditorViewController controller;

  public JsCodeEditor(EditArgs args, JsArray<WebWorkerContext> workers) {
    window = new WebWindow(Editor0::new, WebGLError::onWebGlError,
        args.getContainerId(), workers);
    editor = demoEdit0().editor();
    editor.setDisableParser(args.getDisableParserOrDefault());
    controller = new JsEditorViewController0();
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      setReadonly(args.getReadonly());
  }

  @Override
  public void disconnectFromDom() {
    window.disconnectFromDom();
  }

  @Override
  public void reconnectToDom(JSString containedId) {
    window.connectToDom(containedId);
  }


  @Override
  public JsEditorViewController getController() {
    return controller;
  }

  @Override
  public JsDisposable onControllerUpdate(JsFunctions.Consumer<JsViewController> callback) {
    return JsDisposable.empty();
  }

  @Override
  public void focus() {
    window.focus();
  }

  @Override
  public final void dispose() {
    window.dispose();
  }

  private Editor0 demoEdit0() {
    return (Editor0) window.scene();
  }

  @Override
  public void setText(JSString t) {
    Model prevModel = editor.model();
    Model model = new Model(SplitJsText.split(t), prevModel.docLanguage(), prevModel.uri);
    editor.setModel(model);
  }

  @Override
  public JSString getText() {
    char[] chars = editor.model().document.getChars();
    return TextDecoder.decodeUTF16(chars);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      demoEdit0().applyTheme(t);
    window.repaint();
  }

  @Override
  public void setModel(JsITextModel model) {
    if (model instanceof JsTextModel jsTextModel) {
      editor.setModel(jsTextModel.javaModel);
    } else if (JSObjects.isUndefined(model)) {
      throw new IllegalArgumentException("editor model is undefined");
    } else {
      throw new IllegalArgumentException("bad editor model");
    }
  }

  @Override
  public void setPosition(JsPosition pos) {
    editor.setPosition(pos.getColumn() - 1, pos.getLineNumber() - 1);
  }

  @Override
  public JsPosition getPosition() {
    return JsPosition.fromJava(editor.caretCharPos(), editor.caretLine());
  }

  private void setSelection(JsRange sel) {
    editor.setSelection(
        sel.getEndColumn() - 1,
        sel.getEndLineNumber() - 1,
        sel.getStartColumn() - 1,
        sel.getStartLineNumber() - 1
    );
  }

  @Override
  public JsITextModel getModel() {
    return JsTextModel.fromJava(editor.model());
  }

  @Override
  public JsDisposable registerDefinitionProvider(JSObject languageSelector, JsDefinitionProvider provider) {
    return rDefProv(editor, languageSelector, provider);
  }

  static JsDisposable rDefProv(
      EditorComponent editor, JSObject languageSelector, JsDefinitionProvider provider
  ) {
    var defProvider = new DefDeclProvider(
        LanguageSelectorUtils.toSelectors(languageSelector),
        convert(provider));

    return JsDisposable.of(editor.registrations()
        .definitionProviders.disposableAdd(defProvider));
  }

  @Override
  public JsDisposable registerDeclarationProvider(JSObject languageSelector, JsDeclarationProvider provider) {
    return rDeclProv(editor, languageSelector, provider);
  }

  static JsDisposable rDeclProv(
      EditorComponent editor, JSObject languageSelector, JsDeclarationProvider provider
  ) {
    var defProvider = new DefDeclProvider(
        LanguageSelectorUtils.toSelectors(languageSelector),
        convert(provider));

    return JsDisposable.of(editor.registrations()
        .declarationProviders.disposableAdd(defProvider));
  }

  @Override
  public JsDisposable registerReferenceProvider(JSObject languageSelector, JsReferenceProvider provider) {
    return rRefProv(editor, languageSelector, provider);
  }

  static JsDisposable rRefProv(
      EditorComponent editor, JSObject languageSelector, JsReferenceProvider provider
  ) {
    var referenceProvider = new ReferenceProvider(
        LanguageSelectorUtils.toSelectors(languageSelector), convert(provider));
    return JsDisposable.of(editor.registrations()
        .referenceProviders.disposableAdd(referenceProvider));
  }

  @Override
  public JsDisposable registerDocumentHighlightProvider(JSObject languageSelector, JsDocumentHighlightProvider provider) {
    return rDocHlProv(editor, languageSelector, provider);
  }

  static JsDisposable rDocHlProv(
      EditorComponent editor, JSObject languageSelector, JsDocumentHighlightProvider provider
  ) {
    var hlProvider = new DocumentHighlightProvider(
        LanguageSelectorUtils.toSelectors(languageSelector),
        convert(provider));

    return JsDisposable.of(editor.registrations()
        .documentHighlightProviders.disposableAdd(hlProvider));
  }

  static DefDeclProvider.Provider convert(JsDefinitionProvider provider) {
    return (model, line, column, onResult, onError) ->
        PromiseUtils.<JsArrayReader<JsLocation>>promiseOrT(
            provider.provideDefinition(
                JsTextModel.fromJava(model),
                JsPosition.fromJava(column, line),
                JsCancellationToken.create()),
            jsArr -> acceptLocations(jsArr, onResult, onError),
            onError);
  }

  static DefDeclProvider.Provider convert(JsDeclarationProvider provider) {
    return (model, line, column, onResult, onError) ->
        PromiseUtils.<JsArrayReader<JsLocation>>promiseOrT(
            provider.provideDeclaration(
                JsTextModel.fromJava(model),
                JsPosition.fromJava(column, line),
                JsCancellationToken.create()),
            jsArr -> acceptLocations(jsArr, onResult, onError),
            onError);
  }

  static ReferenceProvider.Provider convert(JsReferenceProvider provider) {
    return (model, line, column, includeDecl, onResult, onError) ->
        PromiseUtils.<JsArrayReader<JsLocation>>promiseOrT(
            provider.provideReferences(
                JsTextModel.fromJava(model),
                JsPosition.fromJava(column, line),
                JsReferenceProvider.Context.create(includeDecl),
                JsCancellationToken.create()),
            jsArr -> acceptLocations(jsArr, onResult, onError),
            onError);
  }

  static DocumentHighlightProvider.Provider convert(JsDocumentHighlightProvider provider) {
    return (model, line, column, onResult, onError) ->
        PromiseUtils.<JsArrayReader<JsDocumentHighlight>>promiseOrT(
            provider.provideDocumentHighlights(
                JsTextModel.fromJava(model),
                JsPosition.fromJava(column, line),
                JsCancellationToken.create()),
            jsArr -> acceptHighlight(jsArr, onResult, onError),
            onError);
  }

  static void acceptLocations(
      JsArrayReader<JsLocation> jsArr,
      Consumer<Location[]> c, Consumer<String> onError
  ) {
    if (JsHelper.jsIf(jsArr)) {
      if (JsArray.isArray(jsArr)) {
        c.accept(ProviderUtils.toLocations(jsArr));
      } else {
        onError.accept(errorNotArray);
      }
    } else {
      Debug.consoleInfo("provider result is undefined");
    }
  }

  static void acceptHighlight(
      JsArrayReader<JsDocumentHighlight> jsArr,
      Consumer<DocumentHighlight[]> c,
      Consumer<String> onError
  ) {
    if (JsHelper.jsIf(jsArr)) {
      if (JsArray.isArray(jsArr)) {
        c.accept(ProviderUtils.toHighlights(jsArr));
      } else {
        onError.accept(errorNotArray);
      }
    } else {
      Debug.consoleInfo("provider result is undefined");
    }
  }

  @Override
  public JsDisposable registerEditorOpener(JsCodeEditorOpener opener) {
    return rEdOpener(opener, editor, this);
  }

  static JsDisposable rEdOpener(
      JsCodeEditorOpener opener, EditorComponent editor, JsIEditorView source
  ) {
    return JsDisposable.of(editor.registrations().openers.disposableAdd(
        (uri, selection, pos) -> opener.openCodeEditor(
            source, JsUri.fromJava(uri),
            selectionOrPositionToJs(selection, pos))));
  }

  static JSObject selectionOrPositionToJs(Range range, Pos pos) {
    if (range != null) return JsRange.fromJava(range);
    if (pos != null) return JsPosition.fromJava(pos);
    return JSObjects.undefined();
  }

  @Override
  public void revealLineInCenter(int line) {
    editor.revealLineInCenter(line - 1);
  }

  @Override
  public void revealLine(int line) {
    editor.revealLine(line - 1);
  }

  @Override
  public void revealPosition(JsPosition position) {
    revealLine(position.getLineNumber());
    // todo: also set hScroll position here
  }

  @Override
  public void setReadonly(boolean flag) {
    editor.readonly = flag;
  }

  @Override
  public JsDisposable onDidChangeModel(JsFunctions.Consumer<JsIModelChangedEvent> f) {
    var listener = convert(f);
    return JsDisposable.of(editor.registrations()
            .modelChangeListeners.disposableAdd(listener));
  }

  static BiConsumer<Model, Model> convert(JsFunctions.Consumer<JsIModelChangedEvent> jsCallback) {
    return (oldModel, newModel) -> jsCallback.f(
        JsIModelChangedEvent.create(
            JsUri.fromJava(oldModel.uri),
            JsUri.fromJava(newModel.uri)
        ));
  }

  public static Promise<JsIEditorView> newEdit(EditArgs arguments) {
    if (JsCanvas.checkFontMetricsAPI()) {
      return Promise.create((postResult, postError) ->
          WebWorkerContext.start(
              worker -> postResult.f(new JsCodeEditor(arguments, worker)),
              postError,
              arguments.workerUrl(),
              arguments.numWorkerThreads()));
    } else {
      return Promise.reject(FireFoxWarning.message);
    }
  }

  @Override
  public void setExternalDialogProvider(JsDialogProvider opener) {}

  @Override
  public void setExternalMessageBar(JsExternalMessageBar emb) {}

  @Override
  public void setExternalContextMenuProvider(JsContextMenuProvider p) {}

  @Override
  public void setNotificationsProvider(JsNotificationsProvider p) {}

  @Override
  public void executeMenuAction(JSString action) {}
}
