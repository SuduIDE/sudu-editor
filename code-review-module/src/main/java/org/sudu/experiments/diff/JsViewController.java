package org.sudu.experiments.diff;

import org.sudu.experiments.js.JsFunctions;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public interface JsViewController extends JSObject {
  // returns 'folderDiff' | 'fileDiff' | 'editor'
  JSString getViewType();

  // getSelection(): FolderDiffSelection | FileDiffSelection | undefined
  JsDiffSelection getSelection();

  boolean canNavigateUp();
  void navigateUp();

  boolean canNavigateDown();
  void navigateDown();

  void refresh();

  @SuppressWarnings("Convert2MethodRef")
  static ViewEventListener toJava(
      JsFunctions.Consumer<JsViewController> callback
  ) {
    return item -> callback.f(item);
  }
}
