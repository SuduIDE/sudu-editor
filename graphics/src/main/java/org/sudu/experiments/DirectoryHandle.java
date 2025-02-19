package org.sudu.experiments;

import java.util.function.Consumer;

public interface DirectoryHandle extends FsItem {

  interface Reader {
    default void onDirectory(DirectoryHandle dir) {}
    default void onFile(FileHandle file) {}
    default void onComplete() {}
    default void onError(String error) {
      System.err.println(getClass().getSimpleName() +
          " read directory error: " + error);
      onComplete();
    }
  }
  void read(Reader reader);

  @Deprecated
  void copyTo(String path, Runnable onComplete, Consumer<String> onError);

  void remove(Runnable onComplete, Consumer<String> onError);
}
