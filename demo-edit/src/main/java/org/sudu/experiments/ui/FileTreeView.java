package org.sudu.experiments.ui;

import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.ui.window.ScrollView;

public class FileTreeView extends TreeView {
  FileTreeNode root;
  public FolderDiffModel model;

  public FileTreeView(UiContext uiContext) {
    super(uiContext);
  }

  public void updateModel() {
    setModel(root.getModel(model));
  }

  public void setRoot(FileTreeNode root) {
    this.root = root;
    this.model = new FolderDiffModel(null);
    updateModel();
  }

  public ScrollView applyTheme(ScrollView view) {
    view.setScrollColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    return view;
  }
}
