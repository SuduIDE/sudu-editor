package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.ArrayOp;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class FolderDiffWindow extends ToolWindow0 {

  Window window;
  Focusable focusSave;
  FolderDiffRootView rootView;
  DirectoryNode leftRoot, rightRoot;
  RemoteFolderDiffModel leftModel, rightModel;
  private static final boolean PRINT_STAT = true;
  private int updateCnt = 0;
  private double startTime;
  private Set<TreeNode> leftModelSet, rightModelSet;

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
    leftModel = RemoteFolderDiffModel.REMOTE_DEFAULT;
    rightModel = RemoteFolderDiffModel.REMOTE_DEFAULT;
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

    DirectoryNode.Handler handler = getHandler(left, treeView);
    var root = new DirectoryNode(dir, handler);
    if (left) leftRoot = root; else rightRoot = root;
    root.onClick.run();
    treeView.setRoot(root);

    if (leftRoot != null && rightRoot == null) window.setTitle(leftRoot.name());
    if (leftRoot == null && rightRoot != null) window.setTitle(rightRoot.name());
    if (leftRoot != null && rightRoot != null)
      window.setTitle(leftRoot.name() + " ↔ " + rightRoot.name());
    root.folders();
    compareRootFolders();
  }

  protected DirectoryNode.Handler getHandler(boolean left, FileTreeView treeView) {
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
        if (node.childrenLength() > 0) updateModel(treeView);
        updateDiffInfo();
        if (node.folders().length == 1 && node.files().length == 0) {
          node.folders()[0].onClick.run();
        }
      }

      private void setOppositeSel(TreeNode oppositeDir) {
        (left ? rootView.right : rootView.left).setSelected(oppositeDir);
      }

      @Override
      public void folderClosed(DirectoryNode node) {
        if (node.childrenLength() > 0) updateModel(treeView);
        node.readOnClick();
        DirectoryNode oppositeDir = findOppositeDir(node);
        setOppositeSel(oppositeDir);
        if (oppositeDir != null && oppositeDir.isOpened()) {
          oppositeDir.onClick.run();
        }
        updateDiffInfo();
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
    if (leftRoot == null || rightRoot == null) return;
    startTime = window.context.window.timeNow();
    System.out.println("startTime = " + startTime);
    leftModel = new RemoteFolderDiffModel(null, "");
    rightModel = new RemoteFolderDiffModel(null, "");
    DiffModelUpdater updateHandler = new DiffModelUpdater(
        leftModel, rightModel,
        leftRoot.dir, rightRoot.dir,
        window.context.window.worker(), this::updateDiffInfo
    );
    updateHandler.beginCompare();
  }

  private boolean needUpdate(TreeNode left, TreeNode right) {
    return leftModelSet.contains(left) || rightModelSet.contains(right);
  }

  private void checkedUpdate(
      boolean needUpdate,
      TreeNode leftNode, TreeNode rightNode
  ) {
    if (rootView.left == null || rootView.right == null) return;
    if (needUpdate || needUpdate(leftNode, rightNode)) updateDiffInfo();
  }


  protected void updateDiffInfo() {
    if (rootView.left == null || rootView.right == null) return;
    updateCnt++;
    rootView.left.updateModel(leftModel);
    rootView.right.updateModel(rightModel);
    var left = rootView.left.model();
    var right = rootView.right.model();
    rootView.setDiffModel(DiffModelBuilder.getDiffInfo(left, right));
    window.context.window.repaint();
    if (leftModel.isCompared() && rightModel.isCompared()) {
      if (PRINT_STAT) {
        int ms = (int) (1000. * (window.context.window.timeNow() - startTime));
        System.out.println("Compared in " + ms + " ms");
        System.out.println("Total updates " + updateCnt);
      }
    }
  }

  private void updateModel(FileTreeView fileTreeView) {
    fileTreeView.updateModel();
    leftModelSet = new HashSet<>(Arrays.asList(rootView.left.model()));
    rightModelSet = new HashSet<>(Arrays.asList(rootView.right.model()));
  }

  private void selectFolder(boolean left) {
    windowManager.uiContext.window.showDirectoryPicker(
        dir -> open(dir, left));
  }
}
