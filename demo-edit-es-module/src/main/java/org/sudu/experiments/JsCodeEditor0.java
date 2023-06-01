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

public class JsCodeEditor0 implements JsCodeEditor {

  private final EditArguments args;
  private final WebWindow window;
  private final DemoEdit0 demoEdit;

  public JsCodeEditor0(EditArguments args, WorkerContext worker) {
    this.args = args;

    this.window = new WebWindow(
        DemoEdit0::new,
        JsCodeEditor0::onWebGlError,
        args.getContainerId().stringValue(),
        worker);
    demoEdit = (DemoEdit0) window.scene();
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
    demoEdit.editor().setText(buffer);
  }

  @Override
  public JSString getText() {
    char[] chars = demoEdit.document().getChars();
    return TextDecoder.fromCharArray(chars);
  }

  @Override
  public void setFontFamily(JSString fontFamily) {
    EditorComponent editor = demoEdit.editor();
    editor.changeFont(fontFamily.stringValue(), editor.getFontVirtualSize());
  }

  @Override
  public void setFontSize(int fontSize) {
    EditorComponent editor = demoEdit.editor();
    editor.changeFont(editor.getFontFamily(), fontSize);
  }

  @Override
  public void setTheme(JSString theme) {
    demoEdit.editor().setTheme(theme.stringValue());
  }

  @Override
  public void setModel(JsITextModel model) {
    EditorComponent editor = demoEdit.editor();
    editor.setModel(((JsTextModel) model).javaModel);
  }

  @Override
  public void setPosition(JSObject selectionOrPosition) {
    EditorComponent editor = demoEdit.editor();
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
    return JsTextModel.fromJava(demoEdit.editor().getModel());
  }

  @Override
  public JsDisposable registerDefinitionProvider(JSObject languageSelector, JsDefinitionProvider provider) {
    EditorComponent editor = demoEdit.editor();
    LanguageSelector[] strLanguages = LanguageSelectorUtils.getLanguageSelectorValues(languageSelector);
    editor.registerDefinitionProvider(
        new DefinitionProvider(
            strLanguages,
            (m, line, column) -> {
              JSObject res = provider.provideDefinition(
                  JsTextModel.fromJava(m),
                  JsPosition.create(column, line),
                  // Currently I don't want to implement cancellation token, so we accept it in API but throw away.
                  JsCancellationToken.create()
              );
              PromiseUtils.<JSArray<JsLocation>>promiseOrT(
                  res,
                  (jsArr) -> {
                    Location[] locs = ProviderUtils.transformJsArrToJavaLocationArr(jsArr);
                    editor.gotoDefinition(locs[0]);
                  }
              );
            }
        )
    );
    return JsDisposable.empty();
  }

  @Override
  public JsDisposable registerReferenceProvider(JSObject languageSelector, JsReferenceProvider provider) {
    EditorComponent editor = demoEdit.editor();
    LanguageSelector[] strLanguages = LanguageSelectorUtils.getLanguageSelectorValues(languageSelector);
    editor.registerReferenceProvider(
        new ReferenceProvider(
            strLanguages,
            (m, line, column, includeDecl) -> {
              JSObject res = provider.provideReferences(
                  JsTextModel.fromJava(m),
                  JsPosition.create(column, line),
                  JsReferenceProvider.Context.create(includeDecl),
                  // Currently I don't want to implement cancellation token, so we accept it in API but throw away.
                  JsCancellationToken.create()
              );
              PromiseUtils.<JSArray<JsLocation>>promiseOrT(
                  res,
                  (jsArr) -> {
                    Location[] locs = ProviderUtils.transformJsArrToJavaLocationArr(jsArr);
                    // TODO: make something like this in the future:
                    // editor.provideUsages(locs);
                  }
              );
            }
        )
    );
    return JsDisposable.empty();
  }

  @Override
  public JsDisposable registerDocumentHighlightProvider(JSObject languageSelector, JsDocumentHighlight provider) {
    return JsDisposable.empty();
  }

  @Override
  public JsDisposable registerEditorOpener(JsCodeEditorOpener opener) {
    return JsDisposable.empty();
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
