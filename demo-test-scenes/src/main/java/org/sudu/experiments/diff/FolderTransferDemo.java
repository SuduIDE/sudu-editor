package org.sudu.experiments.diff;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.editor.worker.TestWalker;
import org.sudu.experiments.editor.worker.WorkerTest;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.ui.DprChangeListener;
import org.sudu.experiments.ui.FileTreeNode;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.WindowDemo;
import org.sudu.experiments.ui.window.View;

public class FolderTransferDemo extends WindowDemo implements DprChangeListener {

  public static final String toWorker = "- to worker ";
  public static final String toEdt    = "- to edt ";

  EditorColorScheme theme = EditorColorScheme.darkIdeaColorScheme();
  FolderDiffRootView rootView;

  public FolderTransferDemo(SceneApi api) {
    super(api);
    clearColor.set(new Color(43));

    api.input.onContextMenu.add(this::onContextMenu);
    api.input.onKeyPress.add(this::onKeyPress);
  }

  private boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.SPACE) {
      return true;
    }
    return false;
  }

  private boolean onContextMenu(MouseEvent event) {
    windowManager.showPopup(
        theme.dialogItem, theme.popupMenuFont,
        event.position,
        ArrayOp.supplier(
            new ToolbarItem(
                windowManager.hidePopupMenuThen(this::selectFileToWorker),
                "file " + toWorker),
            new ToolbarItem(
                windowManager.hidePopupMenuThen(this::selectDirToWorker),
                "dir " + toWorker),
            new ToolbarItem(
                windowManager.hidePopupMenuThen(this::selectFileToEdt),
                "file " + toEdt),
            new ToolbarItem(
                windowManager.hidePopupMenuThen(this::selectDirToEdt),
                "dir " + toEdt)
        ));
    return true;
  }

  @Override
  protected View createContent() {
    rootView = new FolderDiffRootView(uiContext);
    rootView.applyTheme(theme);

    FileTreeNode folderToWork = new FileTreeNode(toWorker, 1);
    folderToWork.onClick = this::selectDirToWorker;
    folderToWork.iconFolderOpened();

    FileTreeNode folderToEdt = new FileTreeNode(toEdt, 1);
    folderToEdt.onClick = this::selectDirToEdt;
    folderToEdt.iconFolderOpened();

    FileTreeNode fileToWork = new FileTreeNode(toWorker, 1);
    fileToWork.onClick = this::selectFileToWorker;
    fileToWork.iconFolderOpened();

    FileTreeNode fileToEdt = new FileTreeNode(toEdt, 1);
    fileToEdt.onClick = this::selectFileToEdt;
    fileToEdt.iconFolderOpened();

    var modelLeft = new FileTreeNode("folder", 0,
        ArrayOp.array(folderToWork, folderToEdt));
    modelLeft.open();

    var modelRight = new FileTreeNode("file", 0,
        ArrayOp.array(fileToWork, fileToEdt));
    modelRight.open();

    rootView.left.setRoot(modelLeft);
    rootView.right.setRoot(modelRight);
    return rootView;
  }

  private void selectDirToWorker() {
    windowManager.uiContext.window.showDirectoryPicker(this::openDir);
  }

  private void selectDirToEdt() {
    windowManager.uiContext.window.showDirectoryPicker(this::openDirEdt);
  }

  private void selectFileToWorker() {
    windowManager.uiContext.window.showOpenFilePicker(this::openFile);
  }

  private void selectFileToEdt() {
    windowManager.uiContext.window.showOpenFilePicker(this::openFileEdt);
  }

  private void openDir(DirectoryHandle dir) {
    System.out.println("dir = " + dir);
    uiContext.window.sendToWorker(
        this::dirResult, TestJobs.asyncWithDir, dir
    );
  }

  private void openDirEdt(DirectoryHandle dir) {
    System.out.println("dir on edt = " + dir);
    dir.read(new TestWalker(dir, this::dirResult));
  }

  private void dirResult(Object[] objects) {
    for (int i = 0; i < objects.length; i++) {
      System.out.println(
          "objects[" + i + "] = " +
              objects[i].getClass().getSimpleName() + ": " +
              objects[i]);
    }
  }

  private void openFile(FileHandle file) {
    System.out.println("openFile: " + file);
    uiContext.window.sendToWorker(
        WorkerTest::fileResult, TestJobs.asyncWithFile, file
    );
  }

  private void openFileEdt(FileHandle file) {
    System.out.println("openFileEdt: " + file);
    file.readAsBytes(
        bytes -> WorkerTest.printFileResult(file.toString(), file, bytes),
        System.err::println
    );
  }
}
