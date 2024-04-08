package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.editor.EditorWindow;
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
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.fs.FileNode;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;
import org.sudu.experiments.worker.ArrayView;
import java.util.Arrays;
import java.util.function.Supplier;

public class FolderDiffWindow extends ToolWindow0 {

  Window window;
  Focusable focusSave;
  FolderDiffRootView rootView;
  DirectoryNode leftRoot, rightRoot;
  FolderDiffModel leftModel, rightModel;
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
    leftModel = FolderDiffModel.getDefault();
    rightModel = FolderDiffModel.getDefault();
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
          window.focus();
        }
      }

      @Override
      public void folderOpened(DirectoryNode node, FolderDiffModel model) {
        node.closeOnClick(model);
        System.out.println("folderOpened " + node.dir.toString());
        DirectoryNode oppositeDir = findOppositeDir(node);

        setOppositeSel(oppositeDir);
        if (oppositeDir != null && oppositeDir.isClosed()) {
          oppositeDir.onClick.run();
        }
        if (node.childrenLength() > 0) {
          treeView.updateModel(model);
        }
        updateDiffInfo();
        if (node.folders().length == 1 && node.files().length == 0) {
          node.folders()[0].onClick.run();
        }
      }

      private void setOppositeSel(TreeNode oppositeDir) {
        (left ? rootView.right : rootView.left).setSelected(oppositeDir);
      }

      @Override
      public void folderClosed(DirectoryNode node, FolderDiffModel model) {
        if (node.childrenLength() > 0) {
          treeView.updateModel(model);
        }
        node.readOnClick(model);
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

  private void compareRootFolders() {
    if (leftRoot == null || rightRoot == null) return;
    ctx.clear();
    leftModel = FolderDiffModel.getDefault();
    rightModel = FolderDiffModel.getDefault();
    if (!leftRoot.name().equals(rightRoot.name())) {
      leftModel.diffType = DiffTypes.EDITED;
      rightModel.diffType = DiffTypes.EDITED;
      rootView.left.updateModel(leftModel);
      rootView.right.updateModel(rightModel);
    }
    compare(leftRoot, rightRoot, leftModel, rightModel);
  }

  private void compare(
      TreeNode left, TreeNode right,
      FolderDiffModel leftModel, FolderDiffModel rightModel
  ) {
    if (left instanceof DirectoryNode leftDir &&
        right instanceof DirectoryNode rightDir
    ) {
      compareFolders(leftDir, rightDir, leftModel, rightModel);
    } else if (left instanceof FileNode leftFile
        && right instanceof FileNode rightFile
    ) {
      compareFiles(leftFile, rightFile, leftModel, rightModel);
    } else throw new IllegalArgumentException("TreeNodes left & right should have same type");
    updateDiffInfo();
  }

  private void updateDiffInfo() {
    if (rootView.left == null || rootView.right == null) return;
    rootView.left.updateModel(leftModel);
    rootView.right.updateModel(rightModel);
    rootView.setDiffModel(getDiffInfo());
  }

  public DiffInfo getDiffInfo() {
    var left = rootView.left.model();
    var right = rootView.right.model();
    DiffRange[] ranges = new DiffRange[1];
    int ptr = 0;
    int lP = 0, rP = 0;
    while (lP < left.length && rP < right.length) {
      if (left[lP].diffType == right[rP].diffType &&
          left[lP].rangeId == right[rP].rangeId
      ) {
        int diffType = left[lP].diffType;
        int rangeId = left[lP].rangeId;
        int lenL = 0, lenR = 0;
        while (lP < left.length && rP < right.length
            && left[lP].rangeId == rangeId
            && right[rP].rangeId == rangeId
        ) {
          lP++;
          lenL++;
          rP++;
          lenR++;
        }
        while (lP < left.length && left[lP].rangeId == rangeId) {
          lP++;
          lenL++;
        }
        while (rP < right.length && right[rP].rangeId == rangeId) {
          rP++;
          lenR++;
        }
        var range = new DiffRange(lP - lenL, lenL, rP - lenR, lenR, diffType);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
        continue;
      }

      boolean leftDepth = left[lP].depth > right[rP].depth;
      if (leftDepth) {
        DiffRange range = handleDeleted(left, lP, rP);
        if (range != null) {
          lP += range.lenL;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
        range = handleInserted(right, rP, lP);
        if (range != null) {
          rP += range.lenR;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
      } else {
        DiffRange range = handleInserted(right, rP, lP);
        if (range != null) {
          rP += range.lenR;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
        range = handleDeleted(left, lP, rP);
        if (range != null) {
          lP += range.lenL;
          ranges = ArrayOp.addAt(range, ranges, ptr++);
          continue;
        }
      }

      if (left[lP].diffType == DiffTypes.DEFAULT || left[lP].diffType == DiffTypes.EDITED) {
        var range = new DiffRange(lP, 1, rP, 0, left[lP].diffType);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
        lP++;
      } else if (right[rP].diffType == DiffTypes.DEFAULT || right[rP].diffType == DiffTypes.EDITED) {
        var range = new DiffRange(lP, 0, rP, 1, right[rP].diffType);
        ranges = ArrayOp.addAt(range, ranges, ptr++);
        rP++;
      } else {
        throw new IllegalStateException();
      }
    }
    while (lP < left.length) {
      var range = new DiffRange(lP, 1, rP, 0, left[lP].diffType);
      ranges = ArrayOp.addAt(range, ranges, ptr++);
      lP++;
    }
    while (rP < right.length) {
      var range = new DiffRange(lP, 0, rP, 1, right[rP].diffType);
      ranges = ArrayOp.addAt(range, ranges, ptr++);
      rP++;
    }
    return new DiffInfo(null, null, Arrays.copyOf(ranges, ptr));
  }

  private DiffRange handleDeleted(TreeNode[] left, int lP, int rP) {
    if (left[lP].diffType == DiffTypes.DELETED) {
      int rangeId = left[lP].rangeId;
      int len = 0;
      while (lP < left.length && left[lP].rangeId == rangeId) {
        lP++;
        len++;
      }
      return new DiffRange(lP - len, len, rP, 0, DiffTypes.DELETED);
    }
    return null;
  }

  private DiffRange handleInserted(TreeNode[] right, int rP, int lP) {
    if (right[rP].diffType == DiffTypes.INSERTED) {
      int rangeId = right[rP].rangeId;
      int len = 0;
      while (rP < right.length && right[rP].rangeId == rangeId) {
        rP++;
        len++;
      }
      return new DiffRange(lP, 0, rP - len, len, DiffTypes.INSERTED);
    }
    return null;
  }

  private void selectFolder(boolean left) {
    windowManager.uiContext.window.showDirectoryPicker(
        dir -> open(dir, left));
  }

  private void compareFiles(FileNode left, FileNode right, FolderDiffModel leftModel, FolderDiffModel rightModel) {
    left.iconRefresh();
    right.iconRefresh();
    windowManager.uiContext.window.sendToWorker(
        result -> onFilesCompared(left, right, leftModel, rightModel, result),
        DiffUtils.CMP_FILES,
        left.file, right.file
    );
  }

  private void compareFolders(
      DirectoryNode left, DirectoryNode right,
      FolderDiffModel leftModel, FolderDiffModel rightModel
  ) {
    left.iconRefresh();
    right.iconRefresh();
    windowManager.uiContext.window.sendToWorker(
        result -> onFoldersCompared(left, right, leftModel, rightModel, result),
        DiffUtils.CMP_FOLDERS,
        left.dir, right.dir
    );
  }

  private void onFilesCompared(
      FileNode left, FileNode right,
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      Object[] result
  ) {
    left.iconFile();
    right.iconFile();
    if (result.length != 1) return;
    boolean equals = ((ArrayView) result[0]).ints()[0] == 1;
    if (!equals) {
      int rangeId = ctx.nextId();
      leftModel.rangeId = rangeId;
      rightModel.rangeId = rangeId;
      ctx.markUp(leftModel, rightModel);
      updateDiffInfo();
    }
  }

  private void onFoldersCompared(
      DirectoryNode left, DirectoryNode right,
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      Object[] result
  ) {
    if (result.length == 0) return;
    int[] ints = ((ArrayView) result[0]).ints();
    int leftLen = ints[0], rightLen = ints[1];
    int[] leftTypes = Arrays.copyOfRange(ints, 2, 2 + leftLen);
    int[] rightTypes = Arrays.copyOfRange(ints, 2 + leftLen, 2 + leftLen + rightLen);
    FsItem[] leftItems = Arrays.copyOfRange(result, 1, 1 + leftLen, FsItem[].class);
    FsItem[] rightItems = Arrays.copyOfRange(result, 1 + leftLen, 1 + leftLen + rightLen, FsItem[].class);

    leftModel.setChildren(leftLen);
    rightModel.setChildren(rightLen);

    int lP = 0, rP = 0;
    boolean changed = true;
    while (changed) {
      changed = false;
      while (lP < leftLen && rP < rightLen &&
          leftTypes[lP] == DiffTypes.DEFAULT &&
          rightTypes[rP] == DiffTypes.DEFAULT
      ) {
        int id = ctx.nextId();
        changed = true;
        leftModel.child(lP).rangeId = id;
        rightModel.child(rP).rangeId = id;
        sendCompare(left, right, leftModel, rightModel, leftItems, lP++, rightItems, rP++);
      }
      if (changed) continue;
      int id = ctx.nextId();
      while (lP < leftLen && leftTypes[lP] == DiffTypes.DELETED) {
        changed = true;
        leftModel.child(lP).diffType = DiffTypes.DELETED;
        leftModel.child(lP).rangeId = id;
        leftModel.child(lP).markDown(DiffTypes.DELETED);
        lP++;
      }
      if (changed) {
        ctx.markUp(leftModel, rightModel);
        continue;
      }
      while (rP < rightLen && rightTypes[rP] == DiffTypes.INSERTED) {
        changed = true;
        rightModel.child(rP).diffType = DiffTypes.INSERTED;
        rightModel.child(rP).rangeId = id;
        rightModel.child(rP).markDown(DiffTypes.INSERTED);
        rP++;
      }
      if (changed) ctx.markUp(leftModel, rightModel);
    }
    updateDiffInfo();
    if (left.childrenLength() == 0) left.iconFolder();
    else left.iconFolderOpened();
    if (right.childrenLength() == 0) right.iconFolder();
    else right.iconFolderOpened();
  }

  private void sendCompare(
      DirectoryNode left, DirectoryNode right,
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      FsItem[] leftItems, int lP,
      FsItem[] rightItems, int rP
  ) {
    if (leftItems[lP] instanceof DirectoryHandle leftDir &&
        rightItems[rP] instanceof DirectoryHandle rightDir
    ) {
      var leftNode = getDirNode(left, leftDir, lP);
      var rightNode = getDirNode(right, rightDir, rP);
      compare(leftNode, rightNode, leftModel.child(lP), rightModel.child(rP));
    } else if (leftItems[lP] instanceof FileHandle leftFile
        && rightItems[rP] instanceof FileHandle rightFile) {
      var leftNode = getFileNode(left, leftFile, lP);
      var rightNode = getFileNode(right, rightFile, rP);
      compare(leftNode, rightNode, leftModel.child(lP), rightModel.child(rP));
    } else throw new IllegalStateException();
  }

  private DirectoryNode getDirNode(DirectoryNode node, DirectoryHandle dir, int p) {
    return node.childrenLength() > 0
        ? node.folders()[p]
        : new DirectoryNode(dir, node.handler);
  }

  private FileNode getFileNode(DirectoryNode node, FileHandle handle, int p) {
    return node.childrenLength() > 0
        ? node.files()[p - node.folders().length]
        : new FileNode(handle.getName(), node.depth + 1, handle);
  }
}
