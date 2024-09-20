package org.sudu.experiments.diff;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

// base class for JS FolderDiffViewController or FileDiffViewController
public interface JsDiffViewController extends JSObject {
  // returns 'folderDiff' | 'fileDiff'
  JSString getViewType();

  // getSelection(): FolderDiffSelection | FileDiffSelection | undefined
  JsDiffSelection getSelection();

  boolean canNavigateUp();
  void navigateUp();

  boolean canNavigateDown();
  void navigateDown();
}
