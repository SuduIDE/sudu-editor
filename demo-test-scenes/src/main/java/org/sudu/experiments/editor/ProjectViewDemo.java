package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.text.SplitText;
import org.sudu.experiments.ui.FileTreeNode;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.WindowDemo;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.window.View;
import org.sudu.experiments.ui.window.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProjectViewDemo extends WindowDemo implements
    ThemeControl,
    DirectoryNode.Handler
{
  ProjectView view;
  FileHandle selectedFile = null;
  Map<FileHandle, Model> modelMap = new HashMap<>();
  Map<FileHandle, String> requestMap = new HashMap<>();

  public ProjectViewDemo(SceneApi api) {
    super(api);

    api.input.onContextMenu.add(this::onContextMenu);
  }

  @Override
  protected View createContent() {
    view = new ProjectView(windowManager, this, true);
    var model = new FileTreeNode("Open project...", 0);
    model.onClick = this::openFolder;
    view.treeView.setRoot(model);
    return view;
  }

  private boolean onContextMenu(MouseEvent event) {
    if (view == null) return true;
    if (view.editor.hitTest(event.position)) {
      view.showContextMenu(event.position);
    } else {
//      if (!windowFrameHitTest(event.position))
        openProjectMenu(event);
    }
    return true;
  }

  private void openProjectMenu(MouseEvent event) {
    view.ui.displayPopup(event.position, ArrayOp.supplier(
        new ToolbarItem(this::openFolder, "Open project ...")
    ));
  }

  private void openFolder() {
    api.window.showDirectoryPicker(this::openDirectory);
  }

  private void openDirectory(DirectoryHandle dir) {
    view.ui.hidePopup();
    System.out.println("fileHandle = " + dir);
    var root = new DirectoryNode(dir, this);
    view.treeView.setRoot(root);
    setWindowTitle(FsItem.fullPath(dir.getPath(), dir.getName()
        .concat(" - project view")));
  }

  @Override
  protected void initialWindowLayout(Window window) {
    super.initialWindowLayout(window);
    toggleDark();
  }

  public void applyTheme(EditorColorScheme theme) {
    setWindowTheme(theme.dialogItem);
    view.applyTheme(theme);
  }

  @Override
  public void openFile(FileHandle file, FileTreeNode node) {
    selectedFile = file;
    Model model = modelMap.get(selectedFile);
    if (model != null) {
      doSetModel(model);
    } else {
      String fullPath = requestMap.get(file);
      if (fullPath != null) {
        Debug.consoleInfo("request in progress " + file);
      } else {
        fetchModel(file);
      }
    }
  }

  private void putModel(FileHandle file, String text, String path) {
    SplitInfo splitInfo = SplitText.splitInfo(text);
    var model = new Model(splitInfo.lines, new Uri(path));
    modelMap.put(file, model);
    if (Objects.equals(selectedFile, file)) {
      doSetModel(model);
    }
  }

  private void fetchModel(FileHandle file) {
    requestMap.put(file, file.getFullPath());
    file.readAsText(
        text -> {
          String path = requestMap.remove(file);
          putModel(file, text, path);
        },
        error -> {
          String path = requestMap.remove(file);
          System.err.println("Error fetching file " + path + ": " + error);
        }
    );
  }

  private void doSetModel(Model model) {
    view.editor.setModel(model);
    view.setEditorFocus();
    // window.focus();
  }

  @Override
  public void folderOpened(DirectoryNode node) {
    node.closeOnClick();
    if (node.childrenLength() > 0)
      view.treeView.updateModel();
  }

  @Override
  public void folderClosed(DirectoryNode node) {
    if (node.childrenLength() > 0) {
      view.treeView.updateModel();
    }
    node.readOnClick();
  }
}
