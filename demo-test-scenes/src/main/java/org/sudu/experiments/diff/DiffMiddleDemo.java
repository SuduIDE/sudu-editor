package org.sudu.experiments.diff;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.MockFileTree;
import org.sudu.experiments.ui.WindowDemo;
import org.sudu.experiments.ui.window.View;

public class DiffMiddleDemo extends WindowDemo implements DprChangeListener {

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();
  FolderDiffRootView rootView;

  public DiffMiddleDemo(SceneApi api) {
    super(api);
    clearColor.set(new Color(43));

    api.input.onContextMenu.add(this::onContextMenu);
    api.input.onKeyPress.add(this::onKeyPress);

  }

  private boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.SPACE) {
      rootView.setDiffModel(testModel());
      return true;
    }
    return false;
  }

  static DiffInfo testModel() {
    DiffRange[] ranges = new DiffRange[4];
    ranges[0] = new DiffRange(1, 3, 4, 5, 1);
    ranges[1] = new DiffRange(5, 6, 10, 2, 2);
    ranges[2] = new DiffRange(12, 2, 13, 3, 3);
    ranges[3] = new DiffRange(15, 3, 17, 2, 4);
    return new DiffInfo(new LineDiff[0], new LineDiff[0], ranges);
  }

  private boolean onContextMenu(MouseEvent event) {
    return true;
  }

  @Override
  protected View createContent() {
    rootView = new FolderDiffRootView(uiContext);
    rootView.applyTheme(theme);
    var leftDir = MockFileTree.randomFolder(
        "Project root", 4, rootView.left::updateModel);
    var rightDir = MockFileTree.randomFolder(
        "Project root", 4, rootView.right::updateModel);
    rootView.left.setRoot(leftDir);
    rootView.right.setRoot(rightDir);
    rootView.setDiffModel(testModel());
    return rootView;
  }
}
