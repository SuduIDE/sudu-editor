package org.sudu.experiments.ui;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.ModelFilter;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;

public class FileTreeDemo extends WindowDemo implements DprChangeListener {

  static final float filesAverage = 4;
  static final float foldersAverage = 3;
  static final float betweenSpaces = 4;
  static final int depth = 4;

  static final boolean folderDoubleClick = false;

  FileTreeView treeView;
  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  public FileTreeDemo(SceneApi api) {
    super(api);
    clearColor.set(new Color(43));
  }

  @Override
  protected View createContent() {
    treeView = new FileTreeView(uiContext);
    var root = MockFileTree.randomFolder(
        "Project root", depth,
        () -> treeView.updateModel(FolderDiffModel.DEFAULT, ModelFilter.NO_FILTER));
    System.out.println("FileTreeView model size = " + root.countAll());
    treeView.setRoot(root);
    treeView.setTheme(theme);
    return treeView.applyTheme(new ScrollView(treeView, uiContext));
  }

  @Override
  protected void initialWindowLayout(Window window) {
    V2i newSize = uiContext.windowSize;
    window.setPosition(
        new V2i(newSize.x / 30, newSize.y / 10),
        new V2i(newSize.x * 3 / 10, newSize.y * 8 / 10)
    );
  }
}
