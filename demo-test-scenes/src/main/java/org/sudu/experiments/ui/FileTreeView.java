package org.sudu.experiments.ui;

import org.sudu.experiments.ui.window.ScrollView;

public class FileTreeView extends TreeView {
  FileTreeNode root;

  public FileTreeView(UiContext uiContext) {
    super(uiContext);
  }

  public void updateModel() {
    setModel(root.getModel());
  }

  public void setRoot(FileTreeNode root) {
    this.root = root;
    updateModel();
  }

  public ScrollView applyTheme(ScrollView view) {
    view.setScrollColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    return view;
  }
}
