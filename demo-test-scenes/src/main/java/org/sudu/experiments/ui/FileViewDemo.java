package org.sudu.experiments.ui;

import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.XorShiftRandom;
import org.sudu.experiments.ui.window.ScrollView;
import org.sudu.experiments.ui.window.Window;

public class FileViewDemo extends WindowScene implements DprChangeListener {

  static final float filesAverage = 4;
  static final float foldersAverage = 3;
  static final int depth = 4;

  static final float titleMargin = 3;
  static final boolean folderDoubleClick = false;

  Window window;
  FileTreeView view;
  EditorColorScheme theme;

  public FileViewDemo(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    window = new Window(uiContext);
    theme = EditorColorScheme.darkIdeaColorScheme();
    window.setTheme(theme.dialogItem);
    UiFont consolas15 = new UiFont("Consolas", 15);
    UiFont segoeUI15 = new UiFont("Segoe UI", 15, false);
    window.setTitle("FileViewDemo", segoeUI15, titleMargin);

    view = new FileTreeView(uiContext);
    var root = makeFolder("Project root", 0, depth,
        view::updateModel, new XorShiftRandom());
    view.setRoot(root);
    view.setTheme(theme, segoeUI15);

    System.out.println("root.countAll() = " + view.root.countAll());
    window.setContent(view.applyTheme(new ScrollView(view, uiContext)));
    windowManager.addWindow(window);
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
      f.onDblClick(() -> System.out.println("open file " + f.value()));
      if (r.nextFloat() < .25f) f.setBold(true);
      ch[folders + i] = f;
    }

    FileTreeNode folder = new FileTreeNode(n, d, ch);
    folder.toggleOnCLick(update, folderDoubleClick);

    if (d + d <= maxD) folder.open();
    else folder.close();

    return folder;
  }


  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) layoutWindows();
  }

  private void layoutWindows() {
    V2i newSize = uiContext.windowSize;
    window.setPosition(
        new V2i(newSize.x / 30, newSize.y / 10),
        new V2i(newSize.x * 3 / 10, newSize.y * 8 / 10)
    );
  }
}
