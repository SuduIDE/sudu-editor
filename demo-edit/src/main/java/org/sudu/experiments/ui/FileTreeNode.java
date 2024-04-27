package org.sudu.experiments.ui;

import org.sudu.experiments.diff.DiffTypes;
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

  TreeNode[] getModel(FolderDiffModel model) {
    int cnt = count();
    TreeNode[] lines = new TreeNode[cnt];
    int idx = getModel(lines, model, 0);
    if (idx != lines.length) throw new RuntimeException();
    return lines;
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

  private int getModel(TreeNode[] t, FolderDiffModel model, int idx) {
    boolean noChildren = model.children == null;
    boolean isDownProp = model.propagation == PROP_DOWN;
    this.rangeId = model.rangeId;
    this.diffType = model.diffType;
    t[idx++] = this;
    chooseIcon(this, model);
    if (isOpened()) {
      for (int i = 0; i < children.length; i++) {
        if (isDownProp) idx = children[i].getModel(t, model.rangeId, model.diffType, idx);
        else if (noChildren) idx = children[i].getModel(t, model.rangeId, DiffTypes.DEFAULT, idx);
        else idx = children[i].getModel(t, model.child(i), idx);
      }
    }
    return idx;
  }

  private static void chooseIcon(FileTreeNode node, FolderDiffModel model) {
    if (node.childrenLength() == 0 && !model.compared) node.iconRefresh();
    else if (node.childrenLength() != 0) node.iconFolderOpened();
    else if (!model.isFile()) node.iconFolder();
    else node.iconFile();
  }

  private int getModel(TreeNode[] t, int rangeId, int diffType, int idx) {
    this.rangeId = rangeId;
    this.diffType = diffType;
    t[idx++] = this;
    if (isOpened()) {
      for (var child: children) {
        idx = child.getModel(t, rangeId, diffType, idx);
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
