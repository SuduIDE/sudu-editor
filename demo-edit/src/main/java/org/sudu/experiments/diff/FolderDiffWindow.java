package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.EditorWindow;
import org.sudu.experiments.diff.folder.DiffStatus;
import org.sudu.experiments.diff.folder.PropTypes;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
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
import org.sudu.experiments.ui.fs.*;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.worker.ArrayView;
import java.util.Arrays;
import java.util.function.Supplier;

public class FolderDiffWindow extends ToolWindow0 {

  Window window;
  FolderDiffRootView rootView;
  DirectoryNode leftRoot, rightRoot;
  RangeCtx ctx = new RangeCtx();

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
    compareRootFolders();
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
        updateDiffInfo();
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
        updateDiffInfo();
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

  private void compareRootFolders() {
    if (leftRoot == null || rightRoot == null) return;
    ctx.clear();
    if (leftRoot.name().equals(rightRoot.name())) {
      leftRoot.status = new DiffStatus(null);
      rightRoot.status = new DiffStatus(null);
      compare(leftRoot, rightRoot);
    } else {
      // left & right roots are not equals
      leftRoot.status = new DiffStatus(null);
      leftRoot.status.diffType = DiffTypes.EDITED;
      leftRoot.status.propagation = PropTypes.PROP_DOWN;
      rightRoot.status = new DiffStatus(null);
      rightRoot.status.diffType = DiffTypes.EDITED;
      rightRoot.status.propagation = PropTypes.PROP_DOWN;
      rootView.left.updateModel();
      rootView.right.updateModel();
    }
  }

  private void compare(TreeNode left, TreeNode right) {
    if (left instanceof DirectoryNode leftDir &&
        right instanceof DirectoryNode rightDir
    ) {
      var handler = new FolderDiffHandler(this::compare, ctx);
      var leftReader = new DirectoryNode.DiffReader(leftDir, handler, true);
      var rightReader = new DirectoryNode.DiffReader(rightDir, handler, false);
      leftDir.dir.read(leftReader);
      rightDir.dir.read(rightReader);
    } else if (left instanceof FileNode leftFile
        && right instanceof FileNode rightFile
    ) {
      compareFiles(leftFile, rightFile);
    } else throw new IllegalArgumentException("TreeNodes left & right should have same type");
    updateDiffInfo();
  }

  private void updateDiffInfo() {
    if (rootView.left == null || rootView.right == null) return;
    rootView.setDiffModel(getDiffInfo());
  }

  public DiffInfo getDiffInfo() {
    var left = rootView.left.model();
    var right = rootView.right.model();
    DiffRange[] ranges = new DiffRange[1];
    int ptr = 0;
    int lP = 0, rP = 0;
    while (lP < left.length && rP < right.length) {
      // handle DEFAULT or EDITED
      if (left[lP].status.diffType == right[rP].status.diffType &&
          left[lP].status.rangeId == right[rP].status.rangeId
      ) {
        int diffType = left[lP].status.diffType;
        int rangeId = left[lP].status.rangeId;
        int lenL = 0, lenR = 0;
        while (lP < left.length && rP < right.length
            && left[lP].status.rangeId == rangeId
            && right[rP].status.rangeId == rangeId
        ) {
          lP++; lenL++;
          rP++; lenR++;
        }
        while (lP < left.length && left[lP].status.rangeId == rangeId) {
          lP++; lenL++;
        }
        while (rP < right.length && right[rP].status.rangeId == rangeId) {
          rP++; lenR++;
        }
        var range = new DiffRange(lP - lenL, lenL, rP - lenR, lenR, diffType);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
      }
      // handle DELETED
      else if (left[lP].status.diffType == DiffTypes.DELETED) {
        int rangeId = left[lP].status.rangeId;
        int len = 0;
        while (lP < left.length && left[lP].status.rangeId == rangeId) {
          lP++;
          len++;
        }
        var range = new DiffRange(lP - len, len, rP, 0, DiffTypes.DELETED);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
      }
      // handle INSERTED
      else if (right[rP].status.diffType == DiffTypes.INSERTED) {
        int rangeId = right[rP].status.rangeId;
        int len = 0;
        while (rP < right.length && right[rP].status.rangeId == rangeId) {
          rP++;
          len++;
        }
        var range = new DiffRange(lP, 0, rP - len, len, DiffTypes.INSERTED);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
      } else {
        if (left[lP].status.diffType == DiffTypes.DEFAULT || left[lP].status.diffType == DiffTypes.EDITED) {
          var range = new DiffRange(lP, 1, rP, 0, left[lP].status.diffType);
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          lP++;
        } else if (right[rP].status.diffType == DiffTypes.DEFAULT || right[rP].status.diffType == DiffTypes.EDITED) {
          var range = new DiffRange(lP, 0, rP, 1, right[rP].status.diffType);
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          rP++;
        } else {
          throw new IllegalStateException();
        }
      }
    }
    return new DiffInfo(null, null, Arrays.copyOf(ranges, ptr));
  }

  private void selectFolder(boolean left) {
    windowManager.uiContext.window.showDirectoryPicker(
        dir -> open(dir, left));
  }

  private void compareFiles(FileNode left, FileNode right) {
    windowManager.uiContext.window.sendToWorker(
        result -> onFilesCompares(left, right, result),
        DiffUtils.CMP_FILES,
        left.file, right.file
    );
  }

  private void onFilesCompares(FileNode left, FileNode right, Object[] result) {
    if (result.length != 1) return;
    boolean equals = ((ArrayView) result[0]).ints()[0] == 1;
    if (!equals) {
      int rangeId = ctx.nextId();
      left.status.rangeId = rangeId;
      right.status.rangeId = rangeId;
      rangeId = ctx.nextId();
      left.status.markUp(DiffTypes.EDITED, ctx);
      ctx.set(rangeId + 1);
      right.status.markUp(DiffTypes.EDITED, ctx);
    }
  }
}
