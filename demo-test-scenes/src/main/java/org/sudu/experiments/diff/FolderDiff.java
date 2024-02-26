package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.*;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.window.View;

import java.util.function.Supplier;

public class FolderDiff extends WindowDemo implements DprChangeListener {

  public static final String selectRightText = "Select right...";
  public static final String selectLeftText = "Select left...";

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();
  DiffRootView rootView;

  public FolderDiff(SceneApi api) {
    super(api);
    clearColor.set(new Color(43));

    api.input.onContextMenu.add(this::onContextMenu);
  }

  private boolean onContextMenu(MouseEvent event) {
    if (rootView != null) {
      PopupMenu popup = newPopup();
      popup.setItems(event.position, actions(event.position));
      windowManager.setPopupMenu(popup);
    }
    return true;
  }

  private PopupMenu newPopup() {
    return windowManager.newPopup(theme.dialogItem, theme.popupMenuFont);
  }

  private Supplier<ToolbarItem[]> actions(V2i pos) {
    if (rootView.left.hitTest(pos)) {
      return select(rootView.left, selectLeftText);
    } else if (rootView.right.hitTest(pos)) {
      return select(rootView.right, selectRightText);
    }
    return selectLR();

  }
  private Supplier<ToolbarItem[]> select(FileTreeView treeView, String t) {
    return ArrayOp.supplier(
        new ToolbarItem(() -> selectFolder(treeView), t));
  }

  private Supplier<ToolbarItem[]> selectLR() {
    return ArrayOp.supplier(
        new ToolbarItem(() ->
            selectFolder(rootView.left), selectLeftText),
        new ToolbarItem(() ->
            selectFolder(rootView.right), selectRightText));
  }

  private void open(DirectoryHandle dir, FileTreeView treeView) {
    windowManager.hidePopupMenu();
    System.out.println("open dir = " + dir.getFullPath());

    DirectoryNode.Handler handler = new DirectoryNode.Handler() {
      @Override
      public void openFile(FileHandle file, FileTreeNode node) {
        System.out.println("opening file ... " + file.getFullPath());
      }

      @Override
      public void folderOpened(DirectoryNode node) {
        node.closeOnClick();
        if (node.childrenLength() > 0) {
          treeView.updateModel();
        }
      }

      @Override
      public void folderClosed(DirectoryNode node) {
        if (node.childrenLength() > 0) {
          treeView.updateModel();
        }
        node.readOnClick();
      }
    };
    var root = new DirectoryNode(dir, handler);
    root.onClick.run();
    treeView.setRoot(root);
  }

  private void selectFolder(FileTreeView treeView) {
    api.window.showDirectoryPicker(dir -> open(dir, treeView));
  }

  private void selectLeft() { selectFolder(rootView.left); }
  private void selectRight() { selectFolder(rootView.right); }

  @Override
  protected View createContent() {
    rootView = new DiffRootView(uiContext);
    rootView.setTheme(theme);
    var modelLeft = new FileTreeNode(selectLeftText, 0);
    var modelRight = new FileTreeNode(selectRightText, 0);
    modelLeft.onClick = this::selectLeft;
    modelRight.onClick = this::selectRight;
    rootView.left.setRoot(modelLeft);
    rootView.right.setRoot(modelRight);
    rootView.setDiffModel(DiffMiddleDemo.testModel());
    return rootView;
  }
}
