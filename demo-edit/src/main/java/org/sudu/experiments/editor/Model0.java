package org.sudu.experiments.editor;

import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.worker.WorkerJobExecutor;

public abstract class Model0 extends CodeLines {
  int caretLine, caretCharPos;

  abstract LineDiff lineDiff(int i);

  abstract void update(double timestamp);

  abstract void setEditor(EditorToModel editor, WorkerJobExecutor executor);

  abstract Document document();

  interface EditorToModel {
    void useDocumentHighlightProvider(int line, int column);

    void fireFileLexed();

    void fireFileIterativeParsed(int start, int stop);

    void updateModelOnDiff(Diff diff, boolean isUndo);

    void onDiffMade();

    boolean isDisableParser();
  }
}
