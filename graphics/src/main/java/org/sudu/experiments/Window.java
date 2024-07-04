package org.sudu.experiments;

import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Window {
  void setTitle(String title);
  void setCursor(String cursor);

  double timeNow();

  void repaint();

  boolean hasFocus();

  boolean addChild(String title, Function<SceneApi, Scene> sf);

  Host getHost();

  void showDirectoryPicker(Consumer<DirectoryHandle> onResult);
  void showOpenFilePicker(Consumer<FileHandle> onResult);

  WorkerJobExecutor worker();

  void runLater(Runnable r);

  void readClipboardText(Consumer<String> success, Consumer<Throwable> onError);
  void writeClipboardText(String text, Runnable success, Consumer<Throwable> onError);

  default boolean isClipboardSupported() { return true; }

  // firefox does not support read clipboard text
  // as result vscode.dev does not render "Paste" command there
  default boolean isReadClipboardTextSupported() { return true; }
}
