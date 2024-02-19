package org.sudu.experiments.ui;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;

public class FileViewDemo extends WindowDemo implements DprChangeListener {

  static final float filesAverage = 4;
  static final float foldersAverage = 3;
  static final int depth = 4;

  static final boolean folderDoubleClick = false;

  FileTreeView treeView;
  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  public FileViewDemo(SceneApi api) {
    super(api);
    clearColor.set(new Color(43));
  }

  @Override
  protected View createContent() {
    treeView = new FileTreeView(uiContext);
    var root = randomFolder("Project root", depth, treeView::updateModel);
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

  public static FileTreeNode randomFolder(String n, int maxD, Runnable update) {
    return makeFolder(n, 0, maxD, update, new XorShiftRandom());
  }

  static FileTreeNode makeFolder(String n, int d, int maxD, Runnable update, XorShiftRandom r) {
    int folders = d < maxD ? 1 + r.poissonNumber(foldersAverage - 1) : 0;
    int files = d <= maxD ? 1 + r.poissonNumber(filesAverage - 1) : 0;

    FileTreeNode[] ch = new FileTreeNode[folders + files];
    for (int i = 0; i < folders; i++) {
      ch[i] = makeFolder("Folder " + i, d + 1, maxD, update, r);
    }

    for (int i = 0; i < files; i++) {
      FileTreeNode f = new FileTreeNode("ClassFile " + i, d);
      f.onDblClick(() ->
          System.out.println("open file " + f.name())
      );

      if (r.nextFloat() < .25f) f.setBold(true);
      ch[folders + i] = f;
    }

    FileTreeNode folder = new FileTreeNode(n, d, ch);
    folder.toggleOnCLick(update, folderDoubleClick);

    if (d + d <= maxD) folder.open();
    else folder.close();

    return folder;
  }
}
