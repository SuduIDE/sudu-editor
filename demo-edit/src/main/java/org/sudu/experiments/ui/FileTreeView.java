package org.sudu.experiments.ui;

import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.ModelFilter;
import org.sudu.experiments.ui.window.ScrollView;

public class FileTreeView extends TreeView {
  FileTreeNode root;

  public FileTreeView(UiContext uiContext) {
    super(uiContext);
  }

  public void updateModel() {
    setModel(root.getModel(FolderDiffModel.DEFAULT, ModelFilter.NO_FILTER));
  }

  public void updateModel(FolderDiffModel model, int filter) {
    setModel(root.getModel(model, filter));
  }

  public void setRoot(FileTreeNode root) {
    this.root = root;
    updateModel();
    setSelected0();
  }

  public ScrollView applyTheme(ScrollView view) {
    view.setScrollColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    return view;
  }
}
