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

  default boolean canCopyTo(DirectoryHandle dir) {
    return false;
  }

  default void copyTo(DirectoryHandle dir, Runnable onComplete, Consumer<String> onError) {
    onError.accept("not implemented");
  }

  void remove(Runnable onComplete, Consumer<String> onError);

  default void createDirectory(
      String name,
      Consumer<DirectoryHandle> onComplete,
      Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }

  default void createFile(
      String name,
      Consumer<FileHandle> onComplete,
      Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }
}
