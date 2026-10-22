package org.sudu.experiments.esm;

import org.sudu.experiments.diff.JsEditorViewController;
import org.sudu.experiments.js.JsArrayReader;
import org.sudu.experiments.js.JsDisposable;
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

  /**
   * Selects the given range, optionally scrolling it into view.
   *
   * <p>Line numbers and columns are 1-based, matching {@link JsPosition}.
   * The caret is placed at the range start.
   *
   * @param shouldReveal        true to scroll the range start into view
   *                            vertically and horizontally, false to only select
   * @param fireVScrollListener whether to notify the vertical scroll observer,
   *                            if the module exposes one, when the call
   *                            changed the vertical scroll position. Diff pane
   *                            synchronization is not affected.
   */
  void selectAndReveal(JsRange range, boolean shouldReveal, boolean fireVScrollListener);

  void highlightRanges(JsArrayReader<JsRange> ranges);
  void selectNextRange();
  void selectPrevRange();

  JsEditorViewController getController();
  void setReadonly(boolean flag);
}
