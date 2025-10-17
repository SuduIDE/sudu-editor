package org.sudu.experiments.editor;

import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class UniDiffModel extends Model0 {
  Model model1 = new Model();
  Model model2 = new Model();

  DiffInfo diffInfo;
  // document lines to view mapping
  int[] docLines = new int[0];
  boolean[] docIndex = new boolean[0];
  // line number values
  int[] ln1Values, ln2Values;

  public CodeLine line(int i) {
    var model = docIndex[i] ? model2 : model1;
    return model.document.lines[docLines[i]];
  }

  @Override
  CodeLine caretCodeLine() {
    return line(caretLine);
  }

  @Override
  boolean hasDiffModel() {
    return model1.hasDiffModel() && model2.hasDiffModel();
  }

  @Override
  LineDiff lineDiff(int i) {
    var model = docIndex[i] ? model2 : model1;
    LineDiff[] diffModel = model.diffModel;
    return diffModel == null ? null : diffModel[docLines[i]];
  }

  @Override
  public void update(double timestamp) {
    // todo: see org.sudu.experiments.editor.Model.update
  }

  @Override
  void setEditor(Model.EditorToModel editor, WorkerJobExecutor executor) {
    super.setEditor(editor, executor);
    // todo: see org.sudu.experiments.editor.Model.setEditor
  }

  @Override
  void documentInvalidateMeasure() {
    model1.documentInvalidateMeasure();
    model2.documentInvalidateMeasure();
  }

  @Override
  Document document() {
    return model2.document;
  }

  @Override
  Uri uri() {
    return model2.uri;
  }

  @Override
  public int length() {
    return docLines.length;
  }

  @Override
  CodeLineMapping defaultMapping() {
    // todo implement
    return new CodeLineMapping.CodeLineMapping0() {
      @Override
      public int length() {
        return docLines.length;
      }
    };
  }

  @Override
  void invalidateFont() {
    model1.invalidateFont();
    model2.invalidateFont();
  }

  // editing
  @Override
  void updateDocumentDiffTimeStamp() {
    double timestamp = editor.timeNow();
    model1.document.setLastDiffTimestamp(timestamp);
    model2.document.setLastDiffTimestamp(timestamp);
  }

  @Override
  void selectAll() {
    model2.selectAll();
  }

  @Override
  void saveToNavStack() {
    // todo:
    model2.saveToNavStack();
  }

  // parsing

  @Override
  void parseFullFile() {
    model1.parseFullFile();
    model2.parseFullFile();
  }

  @Override
  void debugPrintDocumentIntervals() {
    model1.debugPrintDocumentIntervals();
    model2.debugPrintDocumentIntervals();
  }

  // js interop
  @Override
  Model jsExportModel() {
    return model2;
  }
}
