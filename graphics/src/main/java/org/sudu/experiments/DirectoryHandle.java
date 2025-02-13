package org.sudu.experiments;

import java.util.function.Consumer;

public interface DirectoryHandle extends FsItem {

  interface Reader {
    default void onDirectory(DirectoryHandle dir) {}
    default void onFile(FileHandle file) {}
    default void onComplete() {}
  }
  void read(Reader reader);


  boolean canCopyTo(DirectoryHandle dir);

  void copyTo(DirectoryHandle dir, Runnable onComplete, Consumer<String> onError);

  void remove(Runnable onComplete, Consumer<String> onError);

  default void createDirectory(
      String name,
      Consumer<DirectoryHandle> onComplete,
      Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }
}
