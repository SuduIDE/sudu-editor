package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.TreeNode;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.fs.FileNode;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.Arrays;

public class DiffModelBuilder {

  public Runnable updateDiffInfo;
  public RangeCtx rangeCtx = new RangeCtx();
  public WorkerJobExecutor executor;
  final boolean scanFileContent;

  public DiffModelBuilder(Runnable updateDiffInfo, WorkerJobExecutor executor) {
    this(updateDiffInfo, executor, true);
  }

  public DiffModelBuilder(
      Runnable updateDiffInfo,
      WorkerJobExecutor executor,
      boolean scanFileContent) {
    this.updateDiffInfo = updateDiffInfo;
    this.executor = executor;
    this.scanFileContent = scanFileContent;
  }

  public void compareRoots(
      DirectoryNode leftRoot, DirectoryNode rightRoot,
      FolderDiffModel leftModel, FolderDiffModel rightModel
  ) {
    rangeCtx.clear();
    if (!leftRoot.name().equals(rightRoot.name())) {
      leftModel.diffType = DiffTypes.EDITED;
      rightModel.diffType = DiffTypes.EDITED;
    }
    compare(leftRoot, rightRoot, leftModel, rightModel);
  }

  void compare(
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
  }

  DiffInfo getDiffInfo(
      TreeNode[] left, TreeNode[] right
  ) {
    DiffRange[] ranges = new DiffRange[1];
    int ptr = 0;
    int lP = 0, rP = 0;
    while (lP < left.length && rP < right.length) {
      boolean changed = false;
      int diffType = left[lP].diffType;
      int leftDiff;
      int lenL = 0, lenR = 0;
      while (lP < left.length && rP < right.length
          && (leftDiff = left[lP].diffType) == right[rP].diffType
          && leftDiff == diffType) {
        changed = true;
        lP++;
        lenL++;
        rP++;
        lenR++;
      }
      if (changed) {
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

  DiffRange handleDeleted(TreeNode[] left, int lP, int rP) {
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

  DiffRange handleInserted(TreeNode[] right, int rP, int lP) {
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

  void compareFiles(FileNode left, FileNode right, FolderDiffModel leftModel, FolderDiffModel rightModel) {
    if (scanFileContent) {
      executor.sendToWorker(
          result -> onFilesCompared(leftModel, rightModel, result),
          DiffUtils.CMP_FILES,
          left.file, right.file
      );
    } else {
      new SizeScanner(left.file, right.file) {
        @Override
        protected void onComplete(int sizeL, int sizeR) {
          leftModel.itemCompared();
          rightModel.itemCompared();
          onFilesCompared(leftModel, rightModel, sizeL == sizeR);
        }
      };
    }
  }

  void compareFolders(
      DirectoryNode left, DirectoryNode right,
      FolderDiffModel leftModel, FolderDiffModel rightModel
  ) {
    executor.sendToWorker(
        result -> onFoldersCompared(left, right, leftModel, rightModel, result),
        DiffUtils.CMP_FOLDERS,
        left.dir, right.dir
    );
  }

  void onFilesCompared(
      FolderDiffModel leftModel, FolderDiffModel rightModel,
      Object[] result
  ) {
    leftModel.itemCompared();
    rightModel.itemCompared();
    if (result.length != 1) return;
    boolean equals = ((ArrayView) result[0]).ints()[0] == 1;
    onFilesCompared(leftModel, rightModel, equals);
  }

  void onFilesCompared(FolderDiffModel leftModel, FolderDiffModel rightModel, boolean equals) {
    if (!equals) {
      int rangeId = rangeCtx.nextId();
      leftModel.rangeId = rangeId;
      rightModel.rangeId = rangeId;
      rangeCtx.markUp(leftModel, rightModel);
    }
    updateDiffInfo.run();
  }

  void onFoldersCompared(
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
        int id = rangeCtx.nextId();
        changed = true;
        leftModel.child(lP).rangeId = id;
        rightModel.child(rP).rangeId = id;
        sendCompare(left, right, leftModel, rightModel, leftItems, lP++, rightItems, rP++);
      }
      if (changed) continue;
      int id = rangeCtx.nextId();
      while (lP < leftLen && leftTypes[lP] == DiffTypes.DELETED) {
        changed = true;
        leftModel.child(lP).diffType = DiffTypes.DELETED;
        leftModel.child(lP).rangeId = id;
        leftModel.child(lP).markDown(DiffTypes.DELETED);
        leftModel.child(lP).itemCompared();
        lP++;
      }
      if (changed) {
        rangeCtx.markUp(leftModel, rightModel);
        continue;
      }
      while (rP < rightLen && rightTypes[rP] == DiffTypes.INSERTED) {
        changed = true;
        rightModel.child(rP).diffType = DiffTypes.INSERTED;
        rightModel.child(rP).rangeId = id;
        rightModel.child(rP).markDown(DiffTypes.INSERTED);
        rightModel.child(rP).itemCompared();
        rP++;
      }
      if (changed) rangeCtx.markUp(leftModel, rightModel);
    }
    updateDiffInfo.run();
  }

  void sendCompare(
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

  DirectoryNode getDirNode(DirectoryNode node, DirectoryHandle dir, int p) {
    return node.childrenLength() > 0
        ? node.folders()[p]
        : new DirectoryNode(dir, node.handler);
  }

  FileNode getFileNode(DirectoryNode node, FileHandle handle, int p) {
    return node.childrenLength() > 0
        ? node.files()[p - node.folders().length]
        : new FileNode(handle.getName(), node.depth + 1, handle);
  }
}
