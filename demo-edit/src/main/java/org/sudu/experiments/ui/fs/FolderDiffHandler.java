package org.sudu.experiments.ui.fs;

import org.sudu.experiments.diff.CommonRange;
import org.sudu.experiments.diff.Diff;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LCS;
import org.sudu.experiments.diff.folder.DiffStatus;
import org.sudu.experiments.diff.folder.PropTypes;
import org.sudu.experiments.diff.folder.RangeCtx;
import org.sudu.experiments.diff.folder.TreeS;
import org.sudu.experiments.ui.TreeNode;
import java.util.function.BiConsumer;

public class FolderDiffHandler {
  DirectoryNode left, right;
  TreeNode[] leftChildren, rightChildren;
  BiConsumer<TreeNode, TreeNode> compare;
  RangeCtx ctx;

  public FolderDiffHandler(BiConsumer<TreeNode, TreeNode> compare, RangeCtx ctx) {
    this.compare = compare;
    this.ctx = ctx;
  }

  public void sendLeft(DirectoryNode left, TreeNode[] children) {
    this.left = left;
    this.left.status.children = mkStatus(children, this.left.status);
    for (int i = 0; i < children.length; i++)
      children[i].status = this.left.status.children[i];
    this.leftChildren = children;
    if (right != null) compare();
  }

  public void sendRight(DirectoryNode right, TreeNode[] children) {
    this.right = right;
    this.right.status.children = mkStatus(children, this.right.status);
    for (int i = 0; i < children.length; i++)
      children[i].status = this.right.status.children[i];
    this.rightChildren = children;
    if (left != null) compare();
  }

  public void compare() {
    var leftC = mkChildren(leftChildren);
    var rightC = mkChildren(rightChildren);

    var lcs = new LCS<>(leftC, rightC);
    lcs.countAll();
    boolean changed = false;
    for (var range : lcs.ranges) {
      if (range instanceof CommonRange<TreeS> common) {
        int rangeId = ctx.nextId();
        for (int i = 0; i < common.length; i++) {
          int l = common.fromL + i,
              r = common.fromR + i;
          leftChildren[l].status.rangeId = rangeId;
          rightChildren[r].status.rangeId = rangeId;
          compare.accept(leftChildren[l], rightChildren[r]);
        }
      } else if (range instanceof Diff<TreeS> diff) {
        changed = true;
        int rangeId = ctx.nextId();
        for (int i = 0; i < diff.lengthL(); i++) {
          var status = leftChildren[diff.fromL + i].status;
          status.diffType = DiffTypes.DELETED;
          status.propagation = PropTypes.PROP_DOWN;
          status.rangeId = rangeId;
        }
        rangeId = ctx.nextId();
        for (int i = 0; i < diff.lengthR(); i++) {
          var status = rightChildren[diff.fromR + i].status;
          status.diffType = DiffTypes.INSERTED;
          status.propagation = PropTypes.PROP_DOWN;
          status.rangeId = rangeId;
        }
      }
    }
    if (changed) {
      int rangeId = ctx.nextId();
      left.status.markUp(DiffTypes.EDITED, ctx);
      ctx.set(rangeId + 1);
      right.status.markUp(DiffTypes.EDITED, ctx);
    }
  }

  private DiffStatus[] mkStatus(TreeNode[] tree, DiffStatus parent) {
    DiffStatus[] result = new DiffStatus[tree.length];
    for (int i = 0; i < tree.length; i++) {
      result[i] = new DiffStatus(parent);
      if (parent.propagation == PropTypes.PROP_DOWN) {
        result[i].propagation = PropTypes.PROP_DOWN;
        result[i].diffType = parent.diffType;
        result[i].rangeId = parent.rangeId;
      }
    }
    return result;
  }

  private TreeS[] mkChildren(TreeNode[] tree) {
    TreeS[] result = new TreeS[tree.length];
    for (int i = 0; i < tree.length; i++) {
      var node = tree[i];
      if (node instanceof DirectoryNode dir)
        result[i] = new TreeS(dir.name(), true);
      else if (node instanceof FileNode file)
        result[i] = new TreeS(file.name(), false);
    }
    return result;
  }

}