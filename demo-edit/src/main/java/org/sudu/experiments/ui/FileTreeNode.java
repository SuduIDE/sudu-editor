package org.sudu.experiments.ui;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;

import java.util.Comparator;

import static org.sudu.experiments.diff.folder.PropTypes.PROP_DOWN;

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

  TreeNode[] getModel(FolderDiffModel model, int filter) {
    int cnt = count();
    TreeNode[] lines = new TreeNode[cnt];
    int idx = getModel(lines, model, filter, 0);
    if (idx != lines.length) throw new RuntimeException();
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

  private int getModel(TreeNode[] t, FolderDiffModel model, int filter, int idx) {
    boolean noChildren = model.children == null;
    boolean isDownProp = model.getPropagation() == PROP_DOWN;
    this.diffType = model.getDiffType();
    t[idx++] = this;
    setIcon(this, model);
    if (isOpened()) {
      int mP = 0;
      for (FileTreeNode child: children) {
        if (isDownProp && noChildren) {
          idx = child.getModel(t, model.getDiffType(), idx, model.isCompared());
        } else if (noChildren) {
          idx = child.getModel(t, DiffTypes.DEFAULT, idx, model.isCompared());
        } else {
          mP = model.nextInd(mP, filter);
          idx = child.getModel(t, model.child(mP), filter, idx);
          mP++;
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
