package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.diff.folder.DiffStatus;
import org.sudu.experiments.diff.folder.PropTypes;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.FileTreeNode;
import org.sudu.experiments.ui.FileTreeView;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.TreeNode;
import org.sudu.experiments.ui.*;
import org.sudu.experiments.ui.fs.FileDiffHandler;
import org.sudu.experiments.ui.fs.FolderDiffHandler;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.fs.FileNode;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;
import java.util.function.Supplier;

public class FolderDiffWindow extends ToolWindow0 {

  Window window;
  FolderDiffRootView rootView;
  DirectoryNode leftRoot, rightRoot;

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
  }

  protected void dispose() {
    window = null;
    rootView = null;
    leftRoot = rightRoot = null;
  }

  private void onBlur() {
//    var f = windowManager.uiContext.focused();
//    if (rootView.editor1 == f || rootView.editor2 == f)
//      focusSave = f;
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(null);
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
    updateDiffModel();
  }

  private DirectoryNode.Handler getHandler(boolean left, FileTreeView treeView) {
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
        }
      }

      @Override
      public void folderOpened(DirectoryNode node) {
        node.closeOnClick();
        System.out.println("folderOpened " + node.dir.toString());
        DirectoryNode oppositeDir = findOppositeDir(node.dir);

        setOppositeSel(oppositeDir);
        if (oppositeDir != null && oppositeDir.isClosed()) {
          oppositeDir.onClick.run();
        }
        updateView(node);
      }

      @Override
      public void updateView(DirectoryNode node) {
        if (node.childrenLength() > 0) {
          treeView.updateModel();
        }
      }

      private void setOppositeSel(TreeNode oppositeDir) {
        (left ? rootView.right : rootView.left).setSelected(oppositeDir);
      }

      @Override
      public void folderClosed(DirectoryNode node) {
        if (node.childrenLength() > 0) {
          treeView.updateModel();
        }
        node.readOnClick();
        DirectoryNode oppositeDir = findOppositeDir(node.dir);
        setOppositeSel(oppositeDir);
        if (oppositeDir != null && oppositeDir.isOpened()) {
          oppositeDir.onClick.run();
        }
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

  private void updateDiffModel() {
    if (leftRoot == null || rightRoot == null) return;
    if (leftRoot.name().equals(rightRoot.name())) {
      leftRoot.status = new DiffStatus(null);
      rightRoot.status = new DiffStatus(null);
      compare(leftRoot, rightRoot);
    } else {
      leftRoot.status = new DiffStatus(null);
      leftRoot.status.diffType = DiffTypes.EDITED;
      leftRoot.status.propagation = PropTypes.PROP_DOWN;
      rightRoot.status = new DiffStatus(null);
      rightRoot.status.diffType = DiffTypes.EDITED;
      rightRoot.status.propagation = PropTypes.PROP_DOWN;
    }
  }

  private void compare(TreeNode left, TreeNode right) {
    if (left instanceof DirectoryNode leftDir &&
        right instanceof DirectoryNode rightDir) {
      var handler = new FolderDiffHandler(this::compare);
      var leftReader = new DirectoryNode.DiffReader(leftDir, handler, true);
      var rightReader = new DirectoryNode.DiffReader(rightDir, handler, false);
      leftDir.dir.read(leftReader);
      rightDir.dir.read(rightReader);
    } else if (left instanceof FileNode leftFile
        && right instanceof FileNode rightFile) {
      var handler = new FileDiffHandler(leftFile, rightFile);
      leftFile.file.readAsBytes(handler::sendLeft, System.err::println);
      rightFile.file.readAsBytes(handler::sendRight, System.err::println);
    } else throw new IllegalArgumentException("TreeNodes left & right should have same type");
  }

  private void selectFolder(boolean left) {
    windowManager.uiContext.window.showDirectoryPicker(
        dir -> open(dir, left));
  }

}
