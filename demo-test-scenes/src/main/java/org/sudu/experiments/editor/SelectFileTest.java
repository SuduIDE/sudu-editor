package org.sudu.experiments.editor;

import org.sudu.experiments.*;
import org.sudu.experiments.editor.ui.colors.DialogItemColors;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.ToolbarItem;

import java.util.Arrays;

public class SelectFileTest extends WindowScene {
  static final int pageIndex = 4000;
  static final int[] pageHash = {
      0x4432b752, 0x37bcd99e, 0x91b88800, 0xda2f647d,
      0x7415140a, 0x15c0e5b8, 0xc0ee22ed, 0x423b8328
  };

  static final String readPageText = "read file at 16k page #4000";
  static final String openFolderT = "open folder ...";
  static final String openFileT = "open file ...";

  DialogItemColors theme = Themes.darkColorScheme();

  int passCnt;
  int failCnt;

  public SelectFileTest(SceneApi api) {
    super(api);
    api.input.onContextMenu.add(e -> showPopup(e.position));
    uiContext.dprListeners.add(this::onDprChanged);
  }

  private void onDprChanged(float oldDpr, float newDpr) {
    if (oldDpr == 0) {
      var w = windowManager.uiContext.windowSize;
      showPopup(new V2i(w.x / 3, w.y / 3));
    }
  }

  private boolean showPopup(V2i position) {
    windowManager.showPopup(
        theme, theme.windowTitleFont, position,
        ArrayOp.supplier(
            new ToolbarItem(this::openFolder, openFolderT),
            new ToolbarItem(this::openFile, openFileT),
            new ToolbarItem(this::readAtPage4k, readPageText))
    );
    return true;
  }

  void readAtPage4k() {
    System.out.println("pageIndex = " + pageIndex);
    api.window.showOpenFilePicker(this::takeFileAtPage4k);
  }

  private void takeFileAtPage4k(FileHandle file) {
    Debug.consoleInfo("takeFileAtPage4k -> " + file);
    byte[] b16k = new byte[16 * 1024];
    for (int i = 0; i < pageHash.length; i++) {
      int pageI = pageIndex + i;
      int start = pageI * b16k.length;
      file.readAsBytes(
          bytes -> {
            int hashCode = Arrays.hashCode(bytes);
            if (pageHash[pageI - pageIndex] == hashCode) {
              passCnt++;
            } else {
              failCnt++;
            }
            System.out.println("[" + pageI +
                "]: hashCode = 0x" + Integer.toHexString(hashCode));
            if (passCnt + failCnt == pageHash.length) {
              if (failCnt == 0) {
                System.out.println("test passed");
              } else {
                System.out.println("passCnt = " + passCnt);
                System.out.println("failCnt = " + failCnt);
              }
            }
          }, System.err::println, start, b16k.length
      );
    }
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

  private void openFile() {
    api.window.showOpenFilePicker(this::takeFile);
  }

  private void openFolder() {
    api.window.showDirectoryPicker(this::takeDirectory);
  }
}
