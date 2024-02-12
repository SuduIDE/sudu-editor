package org.sudu.experiments.ui;

public class FileTreeNode extends TreeNode {

  static final FileTreeNode[] ch0 = new FileTreeNode[0];
  FileTreeNode[] children = ch0;

  public FileTreeNode(String v, int d) {
    super(v, d);
  }

  public FileTreeNode(String v, int d, FileTreeNode[] ch) {
    super(v, d);
    setContent(ch);
  }

  private void setContent(FileTreeNode[] ch) {
    children = ch;
  }

  boolean isOpened() {
    return arrow == allowDown;
  }

  TreeNode[] getModel() {
    TreeNode[] model = new TreeNode[count()];
    int idx = getModel(model, 0);
    if (idx != model.length) throw new RuntimeException();
    return model;
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
  }

  public void close() {
    arrowRight();
  }

  public Runnable toggle(Runnable update) {
    return () -> {
      if (isOpened()) close();
      else open();
      update.run();
    };
  }

  public void toggleOnCLick(Runnable updateRoot, boolean doubleClick) {
    Runnable toggle = toggle(updateRoot);
    onClickArrow(toggle);
    if (doubleClick) onDblClick(toggle);
    else onClick(toggle);
  }
}
