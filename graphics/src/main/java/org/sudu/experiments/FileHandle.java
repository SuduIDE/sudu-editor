package org.sudu.experiments;

import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.TextDecoder;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface FileHandle extends FsItem {
  interface SyncAccess {
    boolean close();
    double getSize();
    double read(byte[] buf, double filePos);
  }

  // web does not provide sync access to host fs ;/
  default boolean hasSyncAccess() { return true; }

  void syncAccess(
      Consumer<SyncAccess> consumer,
      Consumer<String> onError);

  void getSize(IntConsumer result);

  // parameter text can be string-kind, writer use encoding parameter
  //  - JSString
  //  - java String
  //  - char[]
  // or binary byte[], writer ignore encoding parameter
  default void writeText(
      Object text, String encoding,
      Runnable onComplete, Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }

  default void writeAppend(
      int filePosition, byte[] data,
      Runnable onComplete, Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }

  default boolean canCopyTo(FsItem dst) {
    return false;
  }

  default void copyTo(FsItem dst, Runnable onComplete, Consumer<String> onError) {
    onError.accept("not implemented");
  }

  default void remove(Runnable onComplete, Consumer<String> onError) {
    onError.accept("not implemented");
  }

  default void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
    readAsBytes(consumer, onError, 0, -1);
  }

  void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError,
      int begin, int length);

  static void readTextFile(
      FileHandle fileHandle,
      BiConsumer<String, String> peer,
      Consumer<String> onError
  ) {
    fileHandle.readAsBytes(bytes -> {
      boolean utf8 = FileEncoding.isUtf8(bytes, true);
      boolean gbk = !utf8 && FileEncoding.isGBK(bytes);
      if (gbk) {
        String text = TextDecoder.decodeGbk(bytes);
        peer.accept(text, FileEncoding.gbk);
      } else {
        String text = TextDecoder.decodeUtf8(bytes);
        peer.accept(text, FileEncoding.utf8);
      }
    }, onError);
  }
}
