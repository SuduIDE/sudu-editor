package org.sudu.experiments.diff;

import org.sudu.experiments.Subscribers;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.editor.test.MergeButtonsModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.ui.FileTreeDiffRef;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.ScrollView;

import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

class FolderDiffRootView extends DiffRootView {

  public final Subscribers<IntConsumer> stateListeners =
      new Subscribers<>(new IntConsumer[0]);

  public final Subscribers<SelectionListener> selectionListeners =
      new Subscribers<>(new SelectionListener[0]);

  FileTreeView left, right;
  ScrollView leftScrollView, rightScrollView;
  boolean leftReadonly, rightReadonly;
  DiffSync diffSync;

  FolderDiffRootView(UiContext uiContext) {
    super(uiContext);

    left = new FileTreeView(uiContext);
    right = new FileTreeView(uiContext);
    leftScrollView = new ScrollView(left);
    leftScrollView.setVerticalScrollVisibility(false);
    rightScrollView = new ScrollView(right);
    var leftDiffRef = new FileTreeDiffRef(leftScrollView, left);
    var rightDiffRef = new FileTreeDiffRef(rightScrollView, right);
    middleLine.setLeftRight(leftDiffRef, rightDiffRef);
    diffSync = new DiffSync(leftDiffRef, rightDiffRef);
    setViews(leftScrollView, rightScrollView, middleLine);
  }

  public void applyTheme(EditorColorScheme theme) {
    middleLine.setTheme(null, theme.fileTreeView.bg);
    left.setTheme(theme);
    left.applyTheme(leftScrollView);
    right.setTheme(theme);
    right.applyTheme(rightScrollView);
  }

  public void setDiffModel(DiffInfo diffInfo) {
    diffSync.setModel(diffInfo);
    middleLine.setModel(diffInfo);
  }

  public void setMergeButtons(BiConsumer<FolderDiffModel, Boolean> applyDiff) {
    var diffInfo = diffSync.model;
    var leftColors = new byte[left.model().length];
    var rightColors = new byte[right.model().length];
    var models = MergeButtonsModel.getFolderModels(
        diffInfo,
        left.diffModel(),
        right.diffModel(),
        leftColors,
        rightColors,
        leftReadonly,
        rightReadonly,
        applyDiff
    );
    int n = Math.min(leftColors.length, rightColors.length);
    byte a = DiffTypes.DELETED, b = DiffTypes.INSERTED;
    for (int i = 0; i < n; i++) {
      byte leftColor = leftColors[i];
      if (leftColor == a) leftColors[i] = b;
      else if (leftColor == b) leftColors[i] = a;
    }
    left.enableMergeButtons(models[0].actions, models[0].lines, leftColors, true);
    right.enableMergeButtons(models[1].actions, models[1].lines, rightColors, false);
  }

  public void fireFinished() {
    for (IntConsumer listener : stateListeners.array())
      listener.accept(1);
  }

  void fireSelectionChanged(FolderDiffSelection s) {
    for (SelectionListener listener : selectionListeners.array())
      listener.accept(s);
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    this.leftReadonly = leftReadonly;
    this.rightReadonly = rightReadonly;
  }

  public interface SelectionListener {
    void accept(FolderDiffSelection selection);
  }
}
