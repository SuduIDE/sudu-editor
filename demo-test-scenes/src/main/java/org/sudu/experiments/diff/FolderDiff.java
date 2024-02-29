package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ProjectViewDemo;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.*;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.fs.FileNode;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;

import java.util.function.Supplier;

public class FolderDiff extends WindowScene implements DprChangeListener {

  public static final String selectRightText = "Select right...";
  public static final String selectLeftText = "Select left...";

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();
  DiffRootView rootView;
  DirectoryNode leftRoot, rightRoot;

  public FolderDiff(SceneApi api) {
    super(api);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));

    api.input.onContextMenu.add(this::onContextMenu);
  }

  private boolean onContextMenu(MouseEvent event) {
    PopupMenu popup = windowManager.newPopup(theme.dialogItem, theme.popupMenuFont);
    if (rootView != null) {
      popup.setItems(event.position, actions(event.position));
    } else {
      popup.setItems(event.position, newWindowMenu());
    }
    windowManager.setPopupMenu(popup);
    return true;
  }

  private Supplier<ToolbarItem[]> newWindowMenu() {
    return ArrayOp.supplier(
        new ToolbarItem(() -> {
          windowManager.hidePopupMenu();
          newDiffWindow();
        }, "new Diff window"));
  }

  private Supplier<ToolbarItem[]> actions(V2i pos) {
    if (rootView.left.hitTest(pos)) {
      return select(true, selectLeftText);
    } else if (rootView.right.hitTest(pos)) {
      return select(false, selectRightText);
    }
    return selectLR();

  }
  private Supplier<ToolbarItem[]> select(boolean left, String t) {
    return ArrayOp.supplier(
        new ToolbarItem(() -> selectFolder(left), t));
  }

  private Supplier<ToolbarItem[]> selectLR() {
    return ArrayOp.supplier(
        new ToolbarItem(() ->
            selectFolder(true), selectLeftText),
        new ToolbarItem(() ->
            selectFolder(false), selectRightText));
  }

  private void open(DirectoryHandle dir, boolean left) {
    FileTreeView treeView = left ? rootView.left : rootView.right;
    windowManager.hidePopupMenu();
    System.out.println("open dir = " + dir.getFullPath());

    DirectoryNode.Handler handler = new DirectoryNode.Handler() {
      @Override
      public void openFile(FileNode node) {
        System.out.println("opening file ... " +
            node.file.getFullPath());
      }

      @Override
      public void folderOpened(DirectoryNode node) {
        node.closeOnClick();
        System.out.println("folderOpened " + node.dir.toString());
        DirectoryNode oppositeDir = findOppositeDir(node.dir);
        if (oppositeDir != null && oppositeDir.isClosed()) {
          oppositeDir.onClick.run();
        }
        if (node.childrenLength() > 0) {
          treeView.updateModel();
        }
        // update diff model
      }

      @Override
      public void folderClosed(DirectoryNode node) {
        if (node.childrenLength() > 0) {
          treeView.updateModel();
        }
        node.readOnClick();
        DirectoryNode oppositeDir = findOppositeDir(node.dir);
        if (oppositeDir != null && oppositeDir.isOpened()) {
          oppositeDir.onClick.run();
        }
      }

      DirectoryNode findOppositeDir(DirectoryHandle handle) {
        var dir = left ? rightRoot : leftRoot;
        if (dir == null) return null;
        for (String s : handle.getPath()) {
          var subDir = dir.findSubDir(s);
          if (subDir == null) return null;
          dir = subDir;
        }
        return dir.findSubDir(handle.getName());
      }
    };
    var root = new DirectoryNode(dir, handler);
    if (left) leftRoot = root; else rightRoot = root;
    root.onClick.run();
    treeView.setRoot(root);
  }

  private void selectFolder(boolean left) {
    api.window.showDirectoryPicker(dir -> open(dir, left));
  }

  private void newDiffWindow() {
    Window window = new Window(uiContext, createContent());
    DialogItemColors theme1 = Themes.darculaColorScheme();
    window.setTheme(theme1);
    window.setTitle(getClass().getSimpleName());
    ProjectViewDemo.largeWindowLayout(window);
    windowManager.addWindow(window);
    window.setOnClose(() -> destroyWindow(window));
  }

  private void destroyWindow(Window window) {
    windowManager.removeWindow(window);
    window.dispose();
    rootView = null;
    leftRoot = rightRoot = null;
  }

  @Override
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) newDiffWindow();
  }

  protected View createContent() {
    rootView = new DiffRootView(uiContext);
    rootView.setTheme(theme);
    var modelLeft = new FileTreeNode(selectLeftText, 0);
    var modelRight = new FileTreeNode(selectRightText, 0);
    modelLeft.iconFolderOpened();
    modelRight.iconFolderOpened();
    modelLeft.onClick = () -> selectFolder(true);
    modelRight.onClick = () -> selectFolder(false);
    rootView.left.setRoot(modelLeft);
    rootView.right.setRoot(modelRight);
    rootView.setDiffModel(DiffMiddleDemo.testModel());
    return rootView;
  }
}
