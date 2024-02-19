package org.sudu.experiments;

public interface DirectoryHandle extends FsItem {

  interface Reader {
    default void onDirectory(DirectoryHandle dir) {}
    default void onFile(FileHandle file) {}
    default void onComplete() {}
  }
  void read(Reader reader);
}
