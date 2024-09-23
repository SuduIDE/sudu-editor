package org.sudu.experiments.diff;

import org.sudu.experiments.Subscribers;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.ui.FileTreeDiffRef;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.ScrollView;

import java.util.function.IntConsumer;

class FolderDiffRootView extends DiffRootView implements ThemeControl {

  public final Subscribers<IntConsumer> stateListeners =
      new Subscribers<>(new IntConsumer[0]);

  public final Subscribers<SelectionListener> selectionListeners =
      new Subscribers<>(new SelectionListener[0]);

  FileTreeView left, right;
  ScrollView leftScrollView, rightScrollView;
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
    middleLine.setTheme(theme);
    left.setTheme(theme);
    left.applyTheme(leftScrollView);
    right.setTheme(theme);
    right.applyTheme(rightScrollView);
  }

  public void setDiffModel(DiffInfo diffInfo) {
    diffSync.setModel(diffInfo);
    middleLine.setModel(diffInfo);
  }

  public void fireFinished() {
    for (IntConsumer listener : stateListeners.array())
      listener.accept(1);
  }

  void fireSelectionChanged(FolderDiffSelection s) {
    for (SelectionListener listener : selectionListeners.array())
      listener.accept(s);
  }

  public interface SelectionListener {
    void accept(FolderDiffSelection selection);
  }
}
