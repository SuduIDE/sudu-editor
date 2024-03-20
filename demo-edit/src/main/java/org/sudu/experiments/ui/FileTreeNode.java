package org.sudu.experiments.ui;

import org.sudu.experiments.diff.folder.DiffStatus;

import java.util.Comparator;

import static org.sudu.experiments.diff.folder.PropTypes.PROP_DOWN;

public class FileTreeNode extends TreeNode {

  public static final FileTreeNode[] ch0 = new FileTreeNode[0];
  public static final Comparator<FileTreeNode> cmp = FileTreeNode::compare;

  public FileTreeNode[] children = ch0;

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

  TreeNode[] getModel() {
    TreeNode[] model = new TreeNode[count()];
    int idx = getModel(model, 0);
    if (idx != model.length) throw new RuntimeException();
    return model;
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

  private int getModel(TreeNode[] t, int idx) {
    t[idx++] = this;
    if (isOpened()) {
      for (FileTreeNode child : children) {
        idx = child.getModel(t, idx);
      }
    }
    return idx;
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
    int low = 0;
    int high = a.length - 1;

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
  public void markDown(int diffType) {
    status.diffType = diffType;
    status.propagation = PROP_DOWN;
    if (status.parent != null) status.rangeId = status.parent.rangeId;
    for (var child : children) child.markDown(diffType);
  }

  public void updStatus(DiffStatus[] statuses) {
    for (int i = 0; i < children.length; i++) children[i].status = statuses[i];
  }
}
