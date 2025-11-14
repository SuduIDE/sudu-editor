package org.sudu.experiments.editor;

import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
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

  // editor settings
  String tabIndent = "  ";

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

  public abstract Document document();

  abstract Uri uri();

  abstract CodeLineMapping defaultMapping();

  abstract void invalidateFont();

  // editing

  abstract void updateDocumentDiffTimeStamp();
  abstract void selectAll();
  abstract void saveToNavStack();
  abstract void navStackPop();
  abstract void navigateBack();
  abstract void navigateForward();

  abstract CodeLine caretCodeLine();

  void setCaretPos(int charPos, boolean shift) {
    caretCharPos = Numbers.clamp(0, charPos, caretCodeLine().totalStrLength);
  }

  void setCaretLinePos(int line, int pos) {
    caretCharPos = pos;
    caretLine = line;
    editor.recomputeCaretPosX();
    editor.recomputeCaretPosY();
  }

  void setCaretPos(int pos) {
    caretCharPos = pos;
    editor.recomputeCaretPosX();
  }

  // call from mouse drags, no need to clamp to valid range
  void moveCaret(Pos pos) {
    caretLine = pos.line;
    caretCharPos = pos.pos;
  }

  void setSelectionToCaret() {
    selection.isSelectionStarted = false;
    selection.startPos.set(caretLine, caretCharPos);
    selection.endPos.set(caretLine, caretCharPos);
  }

  void setSelectionRange(Range range) {
    selection.startPos.set(range.startLineNumber, range.startColumn);
    selection.endPos.set(range.endLineNumber, range.endColumn);
    setCaretLinePos(selection.startPos.line, selection.startPos.charInd);
  }

  void setUndoBuffer(UndoBuffer undoBuffer) {
    document().setUndoBuffer(undoBuffer);
  }

  abstract void handleInsert(String s);
  abstract void newLine();
  abstract void handleDelete();
  abstract void handleBackspace();
  abstract void handleTab(boolean shiftPressed);

  String calculateTabIndent(CodeLine codeLine, int tabLength) {
    int count = Numbers.clamp(0, tabLength, codeLine.getBlankStartLength());
    return count == 0 ? null : " ".repeat(count);
  }

  abstract void undoLastDiff(boolean isRedo);

  abstract String onCopy(boolean isCut);

  // parsing
  abstract void parseFullFile();
  abstract void parseViewport(int firstLine, int lastLine);
  abstract void debugPrintDocumentIntervals();
  void resolveAll() {}

  void computeUsages() {}

  void clearUsages() {
    definition = null;
    usages.clear();
  }

  interface EditorToModel {
    void useDocumentHighlightProvider(int line, int column);

    void fireFileLexed();

    void fireFileIterativeParsed(int start, int stop);

    void updateModelOnDiff(Diff diff, boolean isUndo);

    void onDiffMade();

    boolean isDisableParser();

    double timeNow();

    void recomputeCaretPosX();
    void recomputeCaretPosY();

    CodeLineColorScheme getColorScheme();
  }

  // jsInterop

  public abstract Model jsExportModel();
}
