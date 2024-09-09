package org.sudu.experiments;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface FileHandle extends FsItem {
  interface SyncAccess {
    void close();
    double getSize();
    double read(byte[] buf, double filePos);
  }

  // web does not provide sync access to host fs ;/
  default boolean hasSyncAccess() { return true; }

  void syncAccess(
      Consumer<SyncAccess> consumer,
      Consumer<String> onError);

  void getSize(IntConsumer result);

  void readAsText(Consumer<String> consumer, Consumer<String> onError);

  void writeText(String text,  Runnable onComplete, Consumer<String> onError);
  void copyTo(String path,  Runnable onComplete, Consumer<String> onError);

  default void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
    readAsBytes(consumer, onError, 0, -1);
  }

  void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length);
}
