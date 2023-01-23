package org.sudu.experiments.js;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.dom.html.HTMLDocument;
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
    @JSProperty JSArrayReader<FileSystemEntry> getWebkitEntries();
    @JSProperty FileList getFiles();
  }

  public static void showDirectoryPicker(Consumer<FileHandle> onResul) {
    if (directoryPickerSupported()) {
      JsFunctions.Consumer<JSError> onError = JsFileDialog::onDirectoryPickerError;
      showDirectoryPicker().then(
          result -> {
            JsHelper.consoleInfo("showDirectoryPicker result: ", result.getName());
            walkDirIterator(result.values(), onResul, onError,
                new String[] { result.getName().stringValue() });
          }, onError);
    } else {
      displayInputElementDialog(onResul, true);
    }
  }

  public static void showOpenFilePicker(Consumer<FileHandle> onResul) {
    if (showOpenFilePickerSupported()) {
      JsFunctions.Consumer<JSError> onError = JsFileDialog::onDirectoryPickerError;
      showOpenFilePicker().then(
        array -> {
          for (int i = 0, l = array.getLength(); i < l; i++) {
            FileSystemFileHandle handle = array.get(i);
            handle.getFile().then(
                jsFile -> onResul.accept(new JsFileHandle(handle, jsFile)),
                onError);
          }
        }, onError
      );
    } else {
      displayInputElementDialog(onResul, false);
    }
  }

  static void walkDirIterator(
      JsAsyncIterator<FileSystemHandle> values,
      Consumer<FileHandle> onResult,
      JsFunctions.Consumer<JSError> onError,
      String[] path
  ) {
    values.next().then(r -> {
      if (!r.getDone()) {
        walkDirIterator(values, onResult, onError, path);
        walkHandle(r.getValue(), onResult, onError, path);
      }
    }, onError);
  }

  static void walkHandle(
      FileSystemHandle handle,
      Consumer<FileHandle> onResult,
      JsFunctions.Consumer<JSError> onError,
      String[] path
  ) {
    if (handle.isFile()) {
      FileSystemFileHandle fileHandle = handle.cast();
      fileHandle.getFile().then(file -> onResult.accept(
          new JsFileHandle(fileHandle, file, path)), onError);
    } else {
      FileSystemDirectoryHandle dir = handle.cast();
      walkDirIterator(dir.values(), onResult, onError,
          ArrayOp.add(path, dir.getName().stringValue()));
    }
  }

  static void onDirectoryPickerError(JSError e) {
    JsHelper.consoleInfo("JsFileDialog: ", e);
  }

  static void displayInputElementDialog(Consumer<FileHandle> onResul, boolean directory) {
    Debug.consoleInfo("openFileDialog....");
    Input input = HTMLDocument.current().createElement("input").cast();
    input.setType("file");
    if (directory) input.setWebkitdirectory(true);
    input.addEventListener("change", e -> {
      readWebkitEntries(onResul, input);
      readFileEntries(onResul, input);
    });
    input.click();
  }

  static void readFileEntries(Consumer<FileHandle> onResul, Input input) {
    FileList files = input.getFiles();

    for (int i = 0; i < files.getLength(); i++) {
      FileHandle file = JsFileHandle.fromWebkitRelativeFile(files.item(i));
      Window.setTimeout(() -> onResul.accept(file), 0);
    }
  }

  static void readWebkitEntries(Consumer<FileHandle> onResul, Input input) {
    JSArrayReader<FileSystemEntry> webkitEntries = input.getWebkitEntries();
    if (webkitEntries.getLength() > 0) {
      Debug.consoleInfo("webkitEntries.length = ", webkitEntries.getLength());
    }
    for (int i = 0; i < webkitEntries.getLength(); i++) {
      FileSystemEntry entry = webkitEntries.get(i);
      // Debug.consoleInfo("[" + i +"] ", entry.getFilePath());
      Window.setTimeout(() -> walkEntry(onResul, entry), 0);
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
    return JSObjects.hasProperty(Window.current(), "showDirectoryPicker")
        && JsHelper.strictEquals(Window.current().getTop(), Window.worker());
  }

  public static boolean showOpenFilePickerSupported() {
    return JSObjects.hasProperty(Window.current(), "showOpenFilePicker")
        && JsHelper.strictEquals(Window.current().getTop(), Window.worker());
  }

  @JSBody(script = "return window.showDirectoryPicker();")
  public static native Promise<FileSystemDirectoryHandle> showDirectoryPicker();

  @JSBody(script = "return window.showOpenFilePicker();")
  public static native Promise<JSArrayReader<FileSystemFileHandle>> showOpenFilePicker();
}
