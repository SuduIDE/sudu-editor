package org.sudu.experiments.editor;

import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.worker.WorkerJobExecutor;

public class UniDiffModel extends Model0 {
  Model model1 = new Model();
  Model model2 = model1;

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
    // todo: see org.sudu.experiments.editor.Model.setEditor
  }

  @Override
  Document document() {
    return model2.document;
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
}
