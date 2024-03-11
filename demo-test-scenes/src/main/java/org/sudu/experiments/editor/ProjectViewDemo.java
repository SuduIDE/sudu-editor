package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.diff.UiText;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.ToolbarItem;

public class ProjectViewDemo extends WindowScene implements DprChangeListener {
  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();

  public ProjectViewDemo(SceneApi api) {
    super(api, true);
    uiContext.dprListeners.add(this);
    clearColor.set(new Color(43));
    api.input.onContextMenu.add(this::onContextMenu);
  }

  private boolean onContextMenu(MouseEvent event) {
    var actions = ArrayOp.supplier(
        new ToolbarItem(
            windowManager.hidePopupMenuThen(this::newProjectView),
            UiText.newProjectView)
    );
    windowManager.showPopup(
        theme.dialogItem, theme.popupMenuFont,
        event.position, actions);
    return true;
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
    updateView(node);
  }

  @Override
  public void updateView(DirectoryNode node) {
    if (node.childrenLength() > 0) {
      view.treeView.updateModel();
    }
    if (node.folders().length == 1 && node.files().length == 0)
      node.folders()[0].onClick.run();
  public void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) newProjectView();
  }

  private void newProjectView() {
    new ProjectViewWindow(windowManager, theme, ProjectViewDemo::menuFonts);
  }

  static String[] menuFonts() { return Fonts.editorFonts(true); }
}
