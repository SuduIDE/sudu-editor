package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.test.MergeButtonsTestModel;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;

public class Diff1 extends Diff0 {
  public Diff1(SceneApi api) {
    super(api);
  }

  @Override
  protected void onDiffResult(DiffInfo result) {
    super.onDiffResult(result);

    var pair = MergeButtonsTestModel.getModels(result, this::applyDiff);
    MergeButtonsTestModel m1 = pair[0], m2 = pair[1];
    editor1.setMergeButtons(m1.actions, m1.lines);
    editor2.setMergeButtons(m2.actions, m2.lines);
  }

  private void applyDiff(DiffRange range, boolean isL) {
    if (range.type == DiffTypes.DEFAULT) return;
    var fromModel = isL ? editor1.model() : editor2.model();
    int fromStartLine = isL ? range.fromL : range.fromR;
    int fromEndLine = isL ? range.toL() : range.toR();
    var lines = fromModel.document.getLines(fromStartLine, fromEndLine);

    int toStartLine = !isL ? range.fromL : range.fromR;
    int toEndLine = !isL ? range.toL() : range.toR();

    var toModel = !isL ? editor1.model() : editor2.model();
    toModel.document.applyChange(toStartLine, toEndLine, lines);
    sendToDiff();
  }
}
