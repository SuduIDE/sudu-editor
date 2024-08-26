package org.sudu.experiments.ui;

import org.sudu.experiments.diff.folder.FolderDiffModel;

import java.util.Comparator;

public class FileTreeNode extends TreeNode {

  public static final FileTreeNode[] ch0 = new FileTreeNode[0];
  public static final Comparator<FileTreeNode> cmp = FileTreeNode::compare;

  protected FileTreeNode[] children = ch0;

  public FileTreeNode(String v, int d) {
    super(v, d);
  }

  public FileTreeNode(String v, int d, FileTreeNode[] ch) {
    super(v, d);
    setContent(ch);
  }

  public String name() {
    return value();
  }

  static int compare(FileTreeNode o1, FileTreeNode o2) {
    return o1.name().compareTo(o2.name());
  }

  protected void setContent(FileTreeNode[] ch) {
    children = ch;
  }

  TreeNode[] getModel(FolderDiffModel model, FileTreeNode another, int filter) {
    int cnt = count(model, another, filter);
    TreeNode[] lines = new TreeNode[cnt];
    int idx = getModel(lines, model, another, filter, 0);
    if (idx != lines.length) throw new RuntimeException("Wrong number of lines: " + idx + ", expected: " + lines.length);
    return lines;
  }

  public FileTreeNode child(int ind) {
    return children[ind];
  }

  public int childrenLength() {
    return children.length;
  }

  private int count() {
    int n = 1;
    if (isOpened()) {
      for (FileTreeNode child : children)
        n += child.count();
    }
    return n;
  }

  public int countAll() {
    int n = 1;
    for (FileTreeNode child : children)
      n += child.countAll();
    return n;
  }

  public int count(FolderDiffModel model, FileTreeNode another, int filter) {
    int n = 1;
    if (isOpened()) {
      if (model.children == null) return count();

      int i = 0, j = 0;
      for (var child : model.children) {
        if (child.matchFilter(filter)) {
          if (!child.isBoth()) n += children[i++].count(child, null, filter);
          else n += children[i++].count(child, childOrNull(another, j++), filter);
        } else {
          var anotherChild = childOrNull(another, j);
          if (anotherChild == null) n++;
          else n += anotherChild.count();
        }
      }
    }
    return n;
  }

  private int getModel(TreeNode[] t, FolderDiffModel model, FileTreeNode another, int filter, int idx) {
    this.diffType = model.getDiffType();
    t[idx++] = this;
    setIcon(this, model);
    if (isOpened()) {
      if (model.children == null) {
        for (FileTreeNode child: children) {
          idx = child.getModel(t, model.getDiffType(), idx, model.isCompared());
        }
      } else {
        int i = 0, j = 0;
        for (FolderDiffModel child: model.children) {
          if (child.matchFilter(filter)) {
            if (!child.isBoth()) {
              idx = children[i++].getModel(t, child, null, filter, idx);
            } else {
              idx = children[i++].getModel(t, child, childOrNull(another, j++), filter, idx);
            }
          } else {
            var anotherChild = childOrNull(another, j);
            if (anotherChild == null) {
              t[idx++] = empty(child.getDiffType());
              j++;
            } else {
              idx = another.child(j++).getEmptyModel(t, child.getDiffType(), idx);
            }
          }
        }
      }
    }
    return idx;
  }

  private int getModel(TreeNode[] t, int diffType, int idx, boolean compared) {
    this.diffType = diffType;
    t[idx++] = this;
    setIcon(this, compared);
    if (isOpened()) {
      for (var child: children) {
        idx = child.getModel(t, diffType, idx, compared);
      }
    }
    return idx;
  }

  private int getEmptyModel(TreeNode[] t, int diffType, int idx) {
    t[idx++] = empty(diffType);
    if (isOpened()) {
      for (var child: children) {
        idx = child.getEmptyModel(t, diffType, idx);
      }
    }
    return idx;
  }

  private static FileTreeNode empty(int diffType) {
    FileTreeNode empty = new FileTreeNode("", 0);
    empty.diffType = diffType;
    return empty;
  }

  private static FileTreeNode childOrNull(FileTreeNode another, int j) {
    return another == null || j >= another.childrenLength() ? null : another.child(j);
  }

  private static void setIcon(FileTreeNode node, FolderDiffModel model) {
    setIcon(node, model.isCompared());
  }

  private static void setIcon(FileTreeNode node, boolean compared) {
    if (node.childrenLength() == 0 && !compared) node.iconRefresh();
    else node.defaultIcon();
  }

  public void open() {
    arrowDown();
    iconFolderOpened();
  }

  public void close() {
    arrowRight();
    iconFolder();
  }

  public static <T extends FileTreeNode> T bs(T[] a, String key) {
    return bs(a, 0, a.length, key);
  }

  public static <T extends FileTreeNode> T bs(T[] a, int from, int to, String key) {
    int low = from;
    int high = to - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      T midNode = a[mid];
      int cmp = midNode.name().compareTo(key);

      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return midNode;
    }
    return null;
  }

  protected void defaultIcon() {
  }
}
