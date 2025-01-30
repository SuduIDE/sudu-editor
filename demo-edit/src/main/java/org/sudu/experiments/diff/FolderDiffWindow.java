package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.FolderDiffSide;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.editor.test.MergeButtonsModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.FileTreeNode;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.TreeNode;
import org.sudu.experiments.ui.*;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.fs.FileNode;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.update.DiffModelUpdater;

import java.util.function.Supplier;

public class FolderDiffWindow extends ToolWindow0 {

  private static final boolean PRINT_STAT = true;
  private static final boolean dummyMergeButtons = true;

  Window window;
  Focusable focusSave;
  FolderDiffRootView rootView;
  DirectoryNode leftRoot, rightRoot;
  FolderDiffModel root;
  boolean finished;

  private int updateCnt = 0;
  private double startTime;

  public FolderDiffWindow(
      EditorColorScheme theme,
      WindowManager wm,
      Supplier<String[]> fonts
  ) {
    super(wm, theme, fonts);
    rootView = new FolderDiffRootView(windowManager.uiContext);
    rootView.applyTheme(theme);
    var modelLeft = new FileTreeNode(UiText.selectLeftText, 0);
    var modelRight = new FileTreeNode(UiText.selectRightText, 0);
    modelLeft.iconFolderOpened();
    modelRight.iconFolderOpened();
    modelLeft.onClick = () -> selectFolder(true);
    modelRight.onClick = () -> selectFolder(false);
    rootView.left.setRoot(modelLeft);
    rootView.right.setRoot(modelRight);
//    rootView.setDiffModel(DiffMiddleDemo.testModel());
    window = createWindow(rootView);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);
    root = FolderDiffModel.DEFAULT;
  }

  @Override
  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
    rootView.applyTheme(theme);
  }

  protected void dispose() {
    window = null;
    rootView = null;
    leftRoot = rightRoot = null;
  }

  private void onBlur() {
    var f = windowManager.uiContext.focused();
    if (rootView.left == f || rootView.right == f)
      focusSave = f;
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
  }

  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    if (rootView.left.hitTest(pos)) {
      return select(true, UiText.selectLeftText);
    } else if (rootView.right.hitTest(pos)) {
      return select(false, UiText.selectRightText);
    }
    return selectLR();
  }

  private Supplier<ToolbarItem[]> select(boolean left, String t) {
    return ArrayOp.supplier(
        new ToolbarItem(() -> selectFolder(left), t));
  }

  private Supplier<ToolbarItem[]> selectLR() {
    return ArrayOp.supplier(
        new ToolbarItem(() ->
            selectFolder(true), UiText.selectLeftText),
        new ToolbarItem(() ->
            selectFolder(false), UiText.selectRightText));
  }

  private void open(DirectoryHandle dir, boolean left) {
    FileTreeView treeView = left ? rootView.left : rootView.right;
    windowManager.hidePopupMenu();
    System.out.println("open dir = " + dir.getFullPath());

    DirectoryNode.Handler handler = getHandler(left);
    var root = new DirectoryNode(dir, handler);
    if (left) leftRoot = root; else rightRoot = root;
    root.onClick.run();
    treeView.setRoot(root);

    if (leftRoot != null && rightRoot == null)
      window.setTitle(leftRoot.name());
    if (leftRoot == null && rightRoot != null)
      window.setTitle(rightRoot.name());
    if (leftRoot != null && rightRoot != null)
      compareRootFolders();
  }

  protected DirectoryNode.Handler getHandler(boolean left) {
    return new DirectoryNode.Handler() {
      @Override
      public void openFile(FileNode node) {
        System.out.println("opening file ... " +
            node.file.getFullPath());
        FileNode oppositeFile = findOppositeFile(node.file);
        if (oppositeFile != null) {
          setOppositeSel(oppositeFile);
          var window = new FileDiffWindow(windowManager, theme, fonts);
          window.open(node.file, left);
          window.open(oppositeFile.file, !left);
        } else {
          var window = new EditorWindow(windowManager, theme, fonts);
          window.open(node.file);
          window.focus();
        }
      }

      @Override
      public void folderOpened(DirectoryNode node) {
        node.closeOnClick();
        System.out.println("folderOpened " + node.dir.toString());
        DirectoryNode oppositeDir = findOppositeDir(node);

        setOppositeSel(oppositeDir);
        if (oppositeDir != null && oppositeDir.isClosed()) {
          oppositeDir.onClick.run();
        }
        if (node.childrenLength() > 0) updateModel();
        updateDiffInfo(new int[]{0, 0});
        if (node.folders().length == 1 && node.files().length == 0) {
          node.folders()[0].onClick.run();
        }
      }

      private void setOppositeSel(TreeNode oppositeDir) {
        (left ? rootView.right : rootView.left).setSelected(oppositeDir);
      }

      @Override
      public void folderClosed(DirectoryNode node) {
        if (node.childrenLength() > 0) updateModel();
        node.readOnClick();
        DirectoryNode oppositeDir = findOppositeDir(node);
        setOppositeSel(oppositeDir);
        if (oppositeDir != null && oppositeDir.isOpened()) {
          oppositeDir.onClick.run();
        }
        updateDiffInfo(new int[]{0, 0});
      }

      DirectoryNode findOppositeDir(DirectoryNode node) {
        if ((left && node == leftRoot) || (!left && node == rightRoot))
          return left ? rightRoot : leftRoot;
        return findOppositeDir(node.dir);
      }

      DirectoryNode findOppositeDir0(String[] path) {
        DirectoryNode dir = left ? rightRoot : leftRoot;
        if (dir == null) return null;
        for (String s : path) {
          var subDir = dir.findSubDir(s);
          if (subDir == null) return null;
          dir = subDir;
        }
        return dir;
      }

      DirectoryNode findOppositeDir(DirectoryHandle handle) {
        var dir = findOppositeDir0(handle.getPath());
        return dir != null ? dir.findSubDir(handle.getName()) : null;
      }

      FileNode findOppositeFile(FileHandle handle) {
        var dir = findOppositeDir0(handle.getPath());
        return dir != null ? dir.findFile(handle.getName()) : null;
      }
    };
  }

  protected void compareRootFolders() {
    startTime = window.context.window.timeNow();
    System.out.println("startTime = " + startTime);

    var title = leftRoot.name() + " ↔ " + rightRoot.name() + " - scan in progress ...";
    window.setTitle(title);
    finished = false;

    root = new FolderDiffModel(null);
    DiffModelUpdater updateHandler = new DiffModelUpdater(
        root,
        leftRoot.dir, rightRoot.dir,
        window.context.window.worker(),
        this::updateDiffInfo
    );
    updateHandler.beginCompare();
  }

  protected void updateDiffInfo(int[] stat) {
    if (rootView.left == null || rootView.right == null) return;
    if (leftRoot == null || rightRoot == null) return;
    updateCnt++;
    updateModel();
    var left = rootView.left.model();
    var right = rootView.right.model();
    rootView.setDiffModel(DiffModelBuilder.getDiffInfo(left, right));
    rootView.setMergeButtons(null);
    window.context.window.repaint();
    if (root.isCompared()) {
      double dT = window.context.window.timeNow() - startTime;
      if (!finished) {
        var title = leftRoot.name() + " ↔ " + rightRoot.name()
            + " - finished in " + Numbers.iRnd(dT) + "s, " +
            "foldersCompared: " + stat[0] + ", filesCompared: " + stat[1];
        window.setTitle(title);
        finished = true;
        rootView.fireFinished();
      }
      if (PRINT_STAT) {
        System.out.println("Compared in " + Numbers.iRnd(1000 * dT) + " ms");
        System.out.println("Total updates " + updateCnt);
      }
    }
  }

  private void updateModel() {
    rootView.left.updateModel(root, rightRoot, FolderDiffSide.LEFT);
    rootView.right.updateModel(root, leftRoot, FolderDiffSide.RIGHT);
    rootView.setDiffModel(DiffModelBuilder.getDiffInfo(
        rootView.left.model(),
        rootView.right.model()
    ));
    rootView.setMergeButtons(null);
    if (dummyMergeButtons) {
      dummyMergeButtons(rootView.left, true);
      dummyMergeButtons(rootView.right, false);
    }
  }

  void dummyMergeButtons(FileTreeView treeView, boolean left) {
    int length = treeView.model().length;
    var m = new MergeButtonsModel.TestModel(length);
    treeView.enableMergeButtons(
        m.actions, m.lines, new byte[length], left
    );
  }

  private void selectFolder(boolean left) {
    windowManager.uiContext.window.showDirectoryPicker(
        dir -> open(dir, left));
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    rootView.setReadonly(leftReadonly, rightReadonly);
  }
}
