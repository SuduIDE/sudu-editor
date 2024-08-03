package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.test.MergeButtonsTestModel;
import org.sudu.experiments.editor.worker.diff.DiffInfo;

public class Diff1 extends Diff0 {
  public Diff1(SceneApi api) {
    super(api);
  }

  @Override
  protected void onDiffResult(DiffInfo result) {
    super.onDiffResult(result);

    int l1 = editor1.model().document.length();
    int l2 = editor2.model().document.length();
    var m1 = new MergeButtonsTestModel(l1);
    var m2 = new MergeButtonsTestModel(l2);
    editor1.setMergeButtons(m1.actions, m1.lines);
    editor2.setMergeButtons(m2.actions, m2.lines);
  }
}
