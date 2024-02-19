package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;

public class SelectFileTest extends Scene0 {
  public SelectFileTest(SceneApi api) {
    super(api);
    api.input.onKeyPress.add(this::onKeyPress);
    Debug.consoleInfo("press Ctrl-Shift-O to open folder");
    Debug.consoleInfo("press Ctrl-O to open file");
  }

  void takeDirectory(DirectoryHandle dir) {
    Debug.consoleInfo("dir: " + dir);
    dir.read(new DirectoryHandle.Reader() {
      int d;
      @Override
      public void onDirectory(DirectoryHandle dir) {
        Debug.consoleInfo("  sub dir: " + dir);
        d++;
        dir.read(this);
      }

      @Override
      public void onFile(FileHandle file) {
        Debug.consoleInfo("  file: " + file);
      }

      @Override
      public void onComplete() {
        if (--d == 0) System.out.println("complete");
      }
    });
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
