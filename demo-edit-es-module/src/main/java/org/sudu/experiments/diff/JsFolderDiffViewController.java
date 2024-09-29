package org.sudu.experiments.diff;

import org.sudu.experiments.js.JsArray;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

public interface JsFolderDiffViewController extends JsViewController {
  // returns 'folderDiff'
  JSString getViewType();

  // getSelection(): FolderDiffSelection | undefined
  JsFolderDiffSelection getSelection();

  // getDiffFilter(): DiffType[]
  JsArray<JSNumber> getDiffFilter();

  // applyDiffFilter(filters: DiffType[]): void
  void applyDiffFilter(JsArray<JSNumber> diffFilter);
}
