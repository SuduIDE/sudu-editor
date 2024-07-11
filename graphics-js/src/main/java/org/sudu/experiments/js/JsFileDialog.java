package org.sudu.experiments.js;

import org.sudu.experiments.Debug;
import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.dom.html.HTMLInputElement;

import java.util.function.Consumer;

public class JsFileDialog {

  interface FileList extends JSObject {
    @JSProperty int getLength();
    JsFile item(int index);
  }

  @SuppressWarnings("SpellCheckingInspection")
  interface Input extends HTMLInputElement {
    @JSProperty void setWebkitdirectory(boolean value);
    @JSProperty JsArrayReader<FileSystemEntry> getWebkitEntries();
    @JSProperty FileList getFiles();
  }

  public static void showDirectoryPicker(Consumer<DirectoryHandle> onResult) {
    if (directoryPickerSupported()) {
      showDirectoryPicker().then(
          result -> {
            JsHelper.consoleInfo("showDirectoryPicker result: ", result.getName());
            onResult.accept(new JsDirectoryHandle(result));
          },
          error -> JsHelper.consoleInfo("showDirectoryPicker error: ", error)
      );
    } else {
      displayInputElementDialog(null, onResult);
    }
  }

  public static void showOpenFilePicker(Consumer<FileHandle> onResul) {
    if (showOpenFilePickerSupported()) {
      JsFunctions.Consumer<JSError> onError = JsFileDialog::onDirectoryPickerError;
      showOpenFilePicker().then(
        array -> {
          for (int i = 0, l = array.getLength(); i < l; i++) {
            FileSystemFileHandle handle = array.get(i);
            onResul.accept(new JsFileHandle(handle));
          }
        }, onError
      );
    } else {
      displayInputElementDialog(onResul, null);
    }
  }

  static void onDirectoryPickerError(JSError e) {
    JsHelper.consoleInfo("JsFileDialog: ", e);
  }

  static void displayInputElementDialog(Consumer<FileHandle> file, Consumer<DirectoryHandle> dir) {
    Debug.consoleInfo("openFileDialog....");
    Input input = HTMLDocument.current().createElement("input").cast();
    input.setType("file");
    if (dir != null) input.setWebkitdirectory(true);
    input.addEventListener("change", e -> {
//      readWebkitEntries(onResul, input);
//      readFileEntries(onResul, input);
    });
    input.click();
  }

  static void readFileEntries(Consumer<FileHandle> onResul, Input input) {
    FileList files = input.getFiles();

    for (int i = 0; i < files.getLength(); i++) {
      FileHandle file = JsFileHandle.fromWebkitRelativeFile(files.item(i));
      JsWindow.setTimeout(() -> onResul.accept(file), 0);
    }
  }

  static void readWebkitEntries(Consumer<FileHandle> onResul, Input input) {
    JsArrayReader<FileSystemEntry> webkitEntries = input.getWebkitEntries();
    if (webkitEntries.getLength() > 0) {
      Debug.consoleInfo("webkitEntries.length = ", webkitEntries.getLength());
    }
    for (int i = 0; i < webkitEntries.getLength(); i++) {
      FileSystemEntry entry = webkitEntries.get(i);
      // Debug.consoleInfo("[" + i +"] ", entry.getFilePath());
      JsWindow.setTimeout(() -> walkEntry(onResul, entry), 0);
    }
  }

  static void walkEntry(Consumer<FileHandle> onResult, FileSystemEntry entry) {
    if (entry.getIsFile()) {
      JsFile file = entry.<FileSystemFileEntry>cast().file();
      onResult.accept(JsFileHandle.fromWebkitRelativeFile(file));
    } else {
      walkDirectory(onResult, entry.cast());
    }
  }

  static void walkDirectory(Consumer<FileHandle> onResult, FileSystemDirectoryEntry entry) {
    entry.createReader().readEntries(files -> {
      for (int j = 0; j < files.getLength(); j++) {
        walkEntry(onResult, files.get(j));
      }
    });
  }

  public static boolean directoryPickerSupported() {
    return JSObjects.hasProperty(JsWindow.current(), "showDirectoryPicker")
        && JsHelper.strictEquals(JsWindow.current().getTop(), JsWindow.worker());
  }

  public static boolean showOpenFilePickerSupported() {
    return JSObjects.hasProperty(JsWindow.current(), "showOpenFilePicker")
        && JsHelper.strictEquals(JsWindow.current().getTop(), JsWindow.worker());
  }

  @JSBody(script = "return window.showDirectoryPicker();")
  public static native Promise<FileSystemDirectoryHandle> showDirectoryPicker();

  @JSBody(script = "return window.showOpenFilePicker();")
  public static native Promise<JsArrayReader<FileSystemFileHandle>> showOpenFilePicker();
}
