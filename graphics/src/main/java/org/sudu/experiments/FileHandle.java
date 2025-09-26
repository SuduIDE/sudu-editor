package org.sudu.experiments;

import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.TextDecoder;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public interface FileHandle extends FsItem {
  interface SyncAccess {
    boolean close();
    double getSize();
    int read(byte[] buf, double filePos) throws IOException;
    int write(byte[] buf, int length, double filePos) throws IOException;
  }

  // web does not provide sync access to host fs ;/
  default boolean hasSyncAccess() { return true; }

  void syncAccess(
      Consumer<SyncAccess> consumer,
      Consumer<String> onError,
      boolean write // todo: add truncate or append as an option ?
  );

  void getSize(DoubleConsumer result, Consumer<String> onError);

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
      double filePosition, byte[] data,
      Runnable onComplete, Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }

  default void remove(Runnable onComplete, Consumer<String> onError) {
    onError.accept("not implemented");
  }

  default void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
    readAsBytes(consumer, onError, 0, -1);
  }

  int _1gb = 1 << 30;

  static int limit1gb(double fileSize) {
    return fileSize < _1gb ? (int) fileSize : _1gb;
  }

  // read all the file (length<0) or length or tail
  static int limitTail(double fileSize, double begin, int length) {
    return length < 0 ? limit1gb(fileSize) :
        Math.min(length, limit1gb(fileSize - begin));
  }

  void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError,
                   double begin, int length);

  static void readTextFile(
      FileHandle fileHandle,
      BiConsumer<String, String> peer,
      Consumer<String> onError
  ) {
    fileHandle.readAsBytes(bytes -> {
      boolean gbk = FileEncoding.needGbk(bytes);
      if (gbk) {
        String text = TextDecoder.decodeGbk(bytes);
        peer.accept(text, FileEncoding.gbk);
      } else {
        String text = TextDecoder.decodeUtf8(bytes);
        peer.accept(text, FileEncoding.utf8);
      }
    }, onError);
  }

  class Stats {
    public boolean isDirectory, isFile, isSymbolicLink;
    public double size;

    public Stats(
        boolean isDirectory, boolean isFile,
        boolean isSymbolicLink, double size
    ) {
      this.isDirectory = isDirectory;
      this.isFile = isFile;
      this.isSymbolicLink = isSymbolicLink;
      this.size = size;
    }
  }

  // this method also works when path points to a directory
  default void stat(BiConsumer<Stats, String> cb) {
    getSize(size -> cb.accept(
            new Stats(false, true, false, size), null),
        error -> cb.accept(null, error));
  }

  static int hiGb(double addr) {
    return (int) (addr / _1gb);
  }

  static int loGb(double addr) {
    return (int) (addr % _1gb);
  }

  static double int2Address(int loGb, int hiGb) {
    return ((double) _1gb) * hiGb + loGb;
  }

  String eof = "eof";

  static boolean eof(String e) {
    return eof.equals(e);
  }
}
