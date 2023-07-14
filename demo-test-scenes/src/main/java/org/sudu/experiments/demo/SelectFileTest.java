package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;

public class SelectFileTest extends Scene0 {
  public SelectFileTest(SceneApi api) {
    super(api);
    api.input.onKeyPress.add(this::onKeyPress);
  }

  void takeDirectory(FileHandle dir) {
    Debug.consoleInfo("showDirectoryPicker -> " + dir);
  }

  void takeFile(FileHandle file) {
    Debug.consoleInfo("showOpenFilePicker -> " + file);
    file.readAsBytes(bytes -> openFile(file, bytes), this::onError);
  }

  void onError(String error) {
    Debug.consoleInfo(error);
  }

  void openFile(FileHandle file, byte[] content) {
    System.out.println("file = " + file);
    System.out.println("file.content.length = " + content.length);
  }

  boolean onKeyPress(KeyEvent event) {
    if (event.ctrl && event.keyCode == KeyCode.O) {
      if (event.shift) {
        api.window.showDirectoryPicker(this::takeDirectory);
      } else {
        api.window.showOpenFilePicker(this::takeFile);
      }
      return true;
    }
    return false;
  }
}
