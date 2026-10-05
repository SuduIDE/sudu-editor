package org.sudu.experiments.esm;

import org.sudu.experiments.diff.JsEditorViewController;
import org.sudu.experiments.js.JsDisposable;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsIEditorView extends JsView {
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

  JsEditorViewController getController();
  void setReadonly(boolean flag);

  /**
   * Called with the absolute vertical scroll position, in device pixels,
   * on every user driven vertical scroll: mouse wheel, scrollbar drag,
   * keyboard paging, caret reveal and {@link #setVScrollPos}.
   *
   * Note the editor is rendered to a canvas and has no DOM scrollbar,
   * so this callback is the only way to observe its vertical scroll.
   */
  @JSFunctor
  interface VScrollListener extends JSObject {
    void onVScroll(int vScrollPos);
  }

  /** Registers a vertical scroll observer, replacing any previous one. */
  void setVScrollListener(VScrollListener listener);

  /**
   * Called with the caret's line, 1-based to match {@link #getPosition},
   * whenever the caret moves to a different line: arrow keys, Home/End,
   * Page Up/Down, mouse click and drag, goto definition/declaration and
   * {@link #setPosition}.
   *
   * Column-only moves within one line are not reported.
   */
  @JSFunctor
  interface CaretListener extends JSObject {
    void onCaretLine(int lineNumber);
  }

  /** Registers a caret line observer, replacing any previous one. */
  void setCaretListener(CaretListener listener);

  /** Current vertical scroll position, in device pixels. */
  int getVScrollPos();

  /** Sets the vertical scroll position, in device pixels. Notifies the listener. */
  void setVScrollPos(int vScrollPos);

  /** Height of a single line, in device pixels. */
  int getLineHeight();

  /** Number of lines that fit in the editor viewport. */
  int getViewportRows();

  /** Document line count. */
  int getNumLines();

  /** Maximum vertical scroll position, in device pixels. */
  int getMaxVScrollPos();

  /** Editor height in device pixels. */
  int getEditorHeight();

  /** Window.devicePixelRatio, used to convert device pixels to CSS pixels. */
  double getDevicePixelRatio();
}
