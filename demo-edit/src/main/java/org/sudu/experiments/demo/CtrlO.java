package org.sudu.experiments.demo;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.Window;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;

import java.util.function.Consumer;

public class CtrlO implements InputListener {
  final Window window;
  final Consumer<FileHandle> openDirectory;
  final Consumer<FileHandle> openFile;

  public CtrlO(SceneApi api, Consumer<FileHandle> openDirectory, Consumer<FileHandle> openFile) {
    window = api.window;
    this.openDirectory = openDirectory;
    this.openFile = openFile;
  }

  @Override
  public boolean onKey(KeyEvent event) {
    if (event.singlePress() && event.ctrl && event.keyCode == KeyCode.O) {
      if (event.shift) {
        window.showDirectoryPicker(openDirectory);
      } else {
        window.showOpenFilePicker(openFile);
      }
      return true;
    }
    return false;
  }
}
