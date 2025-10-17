package org.sudu.experiments.editor;

import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class Model0 extends CodeLines {
  int caretLine, caretCharPos;
  final Selection selection = new Selection();

  double vScrollLine = 0;

  EditorToModel editor;
  WorkerJobExecutor executor;

  final List<CodeElement> usages = new ArrayList<>();
  CodeElement definition = null;

  boolean highlightResolveError, printResolveTime;

  abstract int length();

  abstract boolean hasDiffModel();
  abstract LineDiff lineDiff(int i);

  abstract void update(double timestamp);

  void setEditor(EditorToModel editor, WorkerJobExecutor executor) {
    this.editor = editor;
    this.executor = executor;
    if (editor == null)
      documentInvalidateMeasure();
  }

  abstract void documentInvalidateMeasure();

  abstract Document document();

  abstract Uri uri();

  abstract CodeLineMapping defaultMapping();

  abstract void invalidateFont();

  // editing

  // why diff ?
  abstract void updateDocumentDiffTimeStamp();
  abstract void selectAll();
  abstract void saveToNavStack();
  abstract CodeLine caretCodeLine();

  void setCaretPos(int charPos, boolean shift) {
    caretCharPos = Numbers.clamp(0, charPos, caretCodeLine().totalStrLength);
  }

  void moveCaret(Pos pos) {
    caretLine = pos.line;
    caretCharPos = pos.pos;
  }

  void setSelectionToCaret() {
    selection.isSelectionStarted = false;
    selection.startPos.set(caretLine, caretCharPos);
    selection.endPos.set(caretLine, caretCharPos);
  }

  void setUndoBuffer(UndoBuffer undoBuffer) {
    document().setUndoBuffer(undoBuffer);
  }

  // parsing
  abstract void parseFullFile();
  abstract void debugPrintDocumentIntervals();

  interface EditorToModel {
    void useDocumentHighlightProvider(int line, int column);

    void fireFileLexed();

    void fireFileIterativeParsed(int start, int stop);

    void updateModelOnDiff(Diff diff, boolean isUndo);

    void onDiffMade();

    boolean isDisableParser();

    double timeNow();
  }

  // jsInterop

  abstract Model jsExportModel();
}
