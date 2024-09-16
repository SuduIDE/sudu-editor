package org.sudu.experiments.diff;

import org.sudu.experiments.Subscribers;
import org.sudu.experiments.editor.ThemeControl;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.ui.FileTreeDiffRef;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.SetCursor;
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
    leftScrollView = new ScrollView(left, uiContext);
    leftScrollView.setVerticalScrollVisibility(false);
    rightScrollView = new ScrollView(right, uiContext);
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

  void fireSelectionChanged(Selection s) {
    for (SelectionListener listener : selectionListeners.array())
      listener.accept(s);
  }

  public static class Selection {
    public String path;
    public boolean isLeft;
    public boolean isFolder;
    public boolean isOrphan;

    public Selection(String path, boolean isLeft, boolean isFolder, boolean isOrphan) {
      this.path = path;
      this.isLeft = isLeft;
      this.isFolder = isFolder;
      this.isOrphan = isOrphan;
    }

    @Override
    public String toString() {
      return "{" +
          "\"path\": \"" + path + "\"" +
          ", \"isLeft\": " + isLeft +
          ", \"isFolder\": " + isFolder +
          ", \"isOrphan\": " + isOrphan +
          "}";
    }
  }

  public interface SelectionListener {
    void accept(Selection selection);
  }

  @Override
  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    if (this.left.hitTest(event.position))
      this.left.onMouseMove(event, setCursor);
    else
      this.left.onMouseLeave();

    if (this.right.hitTest(event.position))
      this.right.onMouseMove(event, setCursor);
    else
      this.right.onMouseLeave();
    return true;
  }
}
