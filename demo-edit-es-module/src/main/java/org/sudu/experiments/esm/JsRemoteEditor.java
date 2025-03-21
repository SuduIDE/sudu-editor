package org.sudu.experiments.esm;

import org.sudu.experiments.Channel;
import org.sudu.experiments.Debug;
import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.diff.JsEditorViewController;
import org.sudu.experiments.diff.JsEditorViewController0;
import org.sudu.experiments.diff.JsViewController;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.js.*;
import org.sudu.experiments.protocol.JsCast;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class JsRemoteEditor implements JsRemoteEditorView {
  public final WebWindow window;
  private final EditorComponent editor;
  JsEditorViewController controller;
  Channel channel;

  public JsRemoteEditor(
      EditArgs args, JsArray<WebWorkerContext> workers, Channel channel
  ) {
    this.channel = channel;
    window = new WebWindow(Editor0::new, WebGLError::onWebGlError,
        args.getContainerId(), workers);
    editor = demoEdit0().editor();
    if (args.hasTheme()) setTheme(args.getTheme());
    controller = new JsEditorViewController0();
    channel.setOnMessage(this::onMessage);
    editor.setOnDiffMadeListener(this::onEdit);
  }

  private void onMessage(JsArray<JSObject> jsArray) {
    String source = JsCast.string(jsArray, 0);
    String encoding = JsCast.string(jsArray, 1);
    String name = JsCast.string(jsArray, 2);
    editor.openFile(source, name, encoding);
  }

  private void onEdit(EditorComponent editor) {
    JSString source = JsCast.jsString(editor.model().document.makeString());
    JSString encoding = JSString.valueOf(editor.model().encoding());
    JsArray<JSObject> jsArray = JsArray.create();
    jsArray.set(0, source);
    jsArray.set(1, encoding);
    channel.sendMessage(jsArray);
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
  public JSObject getState() {
    return JSString.valueOf("state");
  }

  @Override
  public void applyState(JSObject state) {
    Debug.consoleInfo("JsRemoteCodeEditor0.applyState: ", state);
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
  public void setFontFamily(JSString fontFamily) {
    editor.changeFont(fontFamily.stringValue(), editor.getFontVirtualSize());
  }

  @Override
  public void setFontSize(float fontSize) {
    editor.changeFont(editor.getFontFamily(), fontSize);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      demoEdit0().applyTheme(t);
    window.repaint();
  }

  @Override
  public void setPosition(JsPosition pos) {
    editor.setPosition(pos.getColumn() - 1, pos.getLineNumber() - 1);
  }

  @Override
  public JsPosition getPosition() {
    return JsPosition.fromJava(editor.caretCharPos(), editor.caretLine());
  }

  @Override
  public JsITextModel getModel() {
    return JsTextModel.fromJava(editor.model());
  }

  @Override
  public JsDisposable registerDefinitionProvider(
      JSObject languageSelector, JsDefinitionProvider provider
  ) {
    return JsCodeEditor.rDefProv(editor, languageSelector, provider);
  }

  @Override
  public JsDisposable registerDeclarationProvider(
      JSObject languageSelector, JsDeclarationProvider provider
  ) {
    return JsCodeEditor.rDeclProv(editor, languageSelector, provider);
  }

  @Override
  public JsDisposable registerReferenceProvider(
      JSObject languageSelector, JsReferenceProvider provider
  ) {
    return JsCodeEditor.rRefProv(editor, languageSelector, provider);
  }

  @Override
  public JsDisposable registerDocumentHighlightProvider(
      JSObject languageSelector, JsDocumentHighlightProvider provider
  ) {
    return JsCodeEditor.rDocHlProv(editor, languageSelector, provider);
  }

  @Override
  public JsDisposable registerEditorOpener(JsCodeEditorOpener opener) {
    return JsCodeEditor.rEdOpener(opener, editor, this);
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

  public static Promise<JsRemoteEditorView> create(EditArgs arguments, Channel channel) {
    if (JsCanvas.checkFontMetricsAPI()) {
      return Promise.create((postResult, postError) ->
          WebWorkerContext.start(
              worker -> postResult.f(new JsRemoteEditor(arguments, worker, channel)),
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
  public void setReadonly(boolean flag) {
    editor.readonly = flag;
  }
}
