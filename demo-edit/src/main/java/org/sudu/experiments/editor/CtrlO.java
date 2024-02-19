package org.sudu.experiments.editor;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.Window;
import org.sudu.experiments.input.InputListeners;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;

import java.util.function.Consumer;

public class CtrlO implements InputListeners.KeyHandler {
  final Window window;
  final Consumer<DirectoryHandle> openDirectory;
  final Consumer<FileHandle> openFile;

  public CtrlO(SceneApi api, Consumer<FileHandle> openFile) {
    this(api, null, openFile);
  }

  public CtrlO(SceneApi api, Consumer<DirectoryHandle> openDirectory, Consumer<FileHandle> openFile) {
    window = api.window;
    this.openDirectory = openDirectory;
    this.openFile = openFile;
  }

  @Override
  public boolean test(KeyEvent event) {
    if (event.ctrl && event.keyCode == KeyCode.O) {
      if (openDirectory != null && event.shift) {
        window.showDirectoryPicker(openDirectory);
      } else {
        window.showOpenFilePicker(openFile);
      }
      return true;
    }
    return false;
  }
}
