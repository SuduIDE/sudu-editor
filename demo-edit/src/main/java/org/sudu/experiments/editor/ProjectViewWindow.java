package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.text.SplitText;
import org.sudu.experiments.ui.FileTreeNode;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ToolWindow0;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.fs.DirectoryNode;
import org.sudu.experiments.ui.fs.FileNode;
import org.sudu.experiments.ui.window.Window;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ProjectViewWindow extends ToolWindow0
    implements DirectoryNode.Handler
{
  Window window;
  ProjectView view;
  String selectedFile;
  Map<String, Model> modelMap = new HashMap<>();
  Map<String, String> requestMap = new HashMap<>();
  Focusable focusSave;

  public ProjectViewWindow(
      WindowManager windowManager,
      EditorColorScheme theme,
      Supplier<String[]> fonts
  ) {
    super(windowManager, theme, fonts);
    view = new ProjectView(windowManager, this, true);
    view.applyTheme(theme);
    var model = new FileTreeNode("Open project...", 0);
    model.iconFolderOpened();
    model.onClick = this::openFolder;
    view.treeView.setRoot(model);
    window = createWindow(view, "Project view", 0);
    window.onFocus(this::onFocus);
    window.onBlur(this::onBlur);
    windowManager.addWindow(window);
  }

  private void onFocus() {
    windowManager.uiContext.setFocus(focusSave);
  }

  private void onBlur() {
    var f = windowManager.uiContext.focused();
    focusSave = view.editor == f ? f : null;
  }

  @Override
  protected Supplier<ToolbarItem[]> popupActions(V2i pos) {
    if (view.editor.hitTest(pos)) {
      return view.ui.builder(
          view.editor, fonts,
          this,
          view.editor,
          windowManager::enableCleartype).build(pos);
    } else {
      return ArrayOp.supplier(
          new ToolbarItem(this::openFolder, "Open project ...")
      );
    }
  }

  private void openFolder() {
    windowManager.uiContext.window.showDirectoryPicker(this::openDirectory);
  }

  void clearRequestMap() {
    for (Map.Entry<String, String> e : requestMap.entrySet()) {
      requestMap.replace(e.getKey(), null);
    }
  }

  private void openDirectory(DirectoryHandle dir) {
    windowManager.hidePopupMenu();
    modelMap.clear();
    clearRequestMap();
    System.out.println("openDirectory: " + dir);
    var root = new DirectoryNode(dir, this);
    view.treeView.setRoot(root);
    root.onClickArrow.run();
    window.setTitle(dir.getFullPath().concat(" - project view"));
  }

  @Override
  protected void dispose() {
    window = null;
    view = null;
    modelMap = null;
    requestMap = null;
  }

  public void applyTheme(EditorColorScheme theme) {
    super.applyTheme(theme);
    window.setTheme(theme.dialogItem);
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
