package org.sudu.experiments;

import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Window extends WorkerJobExecutor {
  void setTitle(String title);
  void setCursor(String cursor);
//  V2i getClientRect();
//  V2i getScreenRect();
  double timeNow();
//  double devicePixelRatio();
  void repaint();

  boolean hasFocus();

  default boolean addChild(String title, Function<SceneApi, Scene> sf) {
    return false;
  }

  Host getHost();

  void showDirectoryPicker(Consumer<FileHandle> onResult);
  void showOpenFilePicker(Consumer<FileHandle> onResult);

  void sendToWorker(Consumer<Object[]> handler, String method, Object ... args);

  void runLater(Runnable r);

  void readClipboardText(Consumer<String> success, Consumer<Throwable> onError);
  void writeClipboardText(String text, Runnable success, Consumer<Throwable> onError);

  default boolean isClipboardSupported() { return true; }

  // firefox does not support read clipboard text
  // as result vscode.dev does not render "Paste" command there
  default boolean isReadClipboardTextSupported() { return true; }
}
