package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.WindowLayouts;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.text.SplitText;
import org.sudu.experiments.ui.FileTreeNode;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.WindowDemo;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.fs.FileNode;
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

  String selectedFile;
  Map<String, Model> modelMap = new HashMap<>();
  Map<String, String> requestMap = new HashMap<>();

  public ProjectViewDemo(SceneApi api) {
    super(api);

    api.input.onContextMenu.add(this::onContextMenu);
  }

  @Override
  protected View createContent() {
    view = new ProjectView(windowManager, this, true);
    var model = new FileTreeNode("Open project...", 0);
    model.iconFolderOpened();
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

  void clearRequestMap() {
    for (Map.Entry<String, String> e : requestMap.entrySet()) {
      requestMap.replace(e.getKey(), null);
    }
  }

  private void openDirectory(DirectoryHandle dir) {
    view.ui.hidePopup();
    modelMap.clear();
    clearRequestMap();
    System.out.println("openDirectory: " + dir);
    var root = new DirectoryNode(dir, this);
    view.treeView.setRoot(root);
    root.onClickArrow.run();
    setWindowTitle(dir.getFullPath().concat(" - project view"));
  }

  @Override
  protected void initialWindowLayout(Window window) {
    WindowLayouts.largeWindowLayout(window, 0);
    toggleDark();
  }

  public void applyTheme(EditorColorScheme theme) {
    setWindowTheme(theme.dialogItem);
    view.applyTheme(theme);
  }

  @Override
  public void openFile(FileNode node) {
    FileHandle file = node.file;
    String fullPath = file.getFullPath();
    selectedFile = fullPath;
    Model model = modelMap.get(selectedFile);
    if (model != null) {
      doSetModel(model);
    } else {
      System.out.println("request new model, file = " + fullPath);
      if (requestMap.containsKey(fullPath)) {
        Debug.consoleInfo("request in progress " + fullPath);
      } else {
        fetchModel(file, fullPath);
      }
    }
  }

  private void putModel(String fullPath, String text) {
    SplitInfo splitInfo = SplitText.splitInfo(text);
    var model = new Model(splitInfo.lines, new Uri(fullPath));
    modelMap.put(fullPath, model);
    if (Objects.equals(selectedFile, fullPath)) {
      doSetModel(model);
    }
  }

  private void fetchModel(FileHandle file, String fullPath) {
    requestMap.put(fullPath, fullPath);
    file.readAsText(
        text -> {
          requestMap.remove(fullPath);
          putModel(fullPath, text);
        },
        error -> {
          requestMap.remove(fullPath);
          System.err.println(
              "Error fetching file " + fullPath + ": " + error);
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
    if (node.childrenLength() > 0) {
      view.treeView.updateModel();
    }
    if (node.folders().length == 1 && node.files().length == 0)
      node.folders()[0].onClick.run();
  }

  @Override
  public void folderClosed(DirectoryNode node) {
    if (node.childrenLength() > 0) {
      view.treeView.updateModel();
    }
    node.readOnClick();
  }

  @SuppressWarnings("StringEquality")
  @Override
  public void applyFileIcon(FileTreeNode f, String fileName) {
    String language = Languages.languageFromFilename(fileName);
    if (language == Languages.TEXT) {
      f.iconFile();
    } else {
      f.iconFileCode();
    }
  }
}
