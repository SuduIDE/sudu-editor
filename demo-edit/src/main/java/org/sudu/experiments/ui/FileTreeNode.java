package org.sudu.experiments.ui;

import org.sudu.experiments.diff.folder.DiffStatus;
import org.sudu.experiments.diff.folder.FolderDiffModel;

import java.util.Comparator;

import static org.sudu.experiments.diff.folder.PropTypes.PROP_DOWN;

public class FileTreeNode extends TreeNode {

  public static final FileTreeNode[] ch0 = new FileTreeNode[0];
  public static final Comparator<FileTreeNode> cmp = FileTreeNode::compare;

  FileTreeNode[] children = ch0;

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

  TreeView.TreeModel getModel(FolderDiffModel model) {
    int cnt = count();
    TreeNode[] lines = new TreeNode[cnt];
    DiffStatus[] statuses = new DiffStatus[cnt];
    int idx = getModel(lines, statuses, model, 0);
    if (idx != lines.length) throw new RuntimeException();
    return new TreeView.TreeModel(lines, statuses);
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

  private int getModel(TreeNode[] t, DiffStatus[] s, FolderDiffModel model, int idx) {
    DiffStatus status = model != null ? model.status : null;
    boolean isDownProp = model == null || model.children == null || status == null || status.propagation == PROP_DOWN;
    t[idx] = this;
    s[idx] = status;
    idx++;
    if (isOpened()) {
      for (int i = 0; i < children.length; i++) {
        idx = isDownProp
            ? children[i].getModel(t, s, status, idx)
            : children[i].getModel(t, s, model.child(i), idx);
      }
    }
    return idx;
  }

  private int getModel(TreeNode[] t, DiffStatus[] s, DiffStatus prop, int idx) {
    t[idx] = this;
    s[idx] = prop;
    idx++;
    if (isOpened()) {
      for (var child: children) {
        idx = child.getModel(t, s, prop, idx);
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
}
