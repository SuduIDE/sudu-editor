package org.sudu.experiments;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface FileHandle extends FsItem {
  interface SyncAccess {
    void close();
    double getSize();
    double read(byte[] buf, double filePos);
  }

  void syncAccess(
      Consumer<SyncAccess> consumer,
      Consumer<String> onError);

  void getSize(IntConsumer result);

  void readAsText(Consumer<String> consumer, Consumer<String> onError);

  default void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
    readAsBytes(consumer, onError, 0, -1);
  }

  void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length);
}
