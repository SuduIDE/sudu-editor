package org.sudu.experiments;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

public class JvmFileHandle extends JvmFsHandle implements FileHandle {

  static final Function<byte[], String> asUtf8String = b -> new String(b, StandardCharsets.UTF_8);
  static final Function<byte[], byte[]> identity = b -> b;

  public JvmFileHandle(String path, String[] rPath, Executor bgWorker, Executor edt) {
    super(path, rPath, bgWorker, edt);
  }

  public JvmFileHandle(Path path, String[] rPath, Executor bgWorker, Executor edt) {
    super(path, rPath, bgWorker, edt);
  }

  // Returns a handle to same file, but with specified event thread
  // If edt argument is null then the background worker's bus is used
  public JvmFileHandle withEdt(Executor edt) {
    return this.edt == edt ? this :
        new JvmFileHandle(path, rPath, bgWorker, edt != null ? edt : bgWorker);
  }

  @Override
  public void getSize(IntConsumer result) {
    result.accept(getFileSize());
  }

  private int getFileSize() {
    try {
      long size = Files.size(path);
      if (size > (int) size) throw new IOException("size > (int)size");
      return (int) size;
    } catch (IOException e) {
      System.err.println(e.getMessage());
      return 0;
    }
  }

  @Override
  public void readAsText(Consumer<String> consumer, Consumer<String> onError) {
    read(consumer, onError, asUtf8String);
  }

  @Override
  public void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
    read(consumer, onError, identity);
  }

  <T> void read(Consumer<T> consumer, Consumer<String> onError, Function<byte[], T> transform) {
    bgWorker.execute(() -> {
      try {
        byte[] allBytes = Files.readAllBytes(path);
        T apply = transform.apply(allBytes);
        edt.execute(() -> consumer.accept(apply));
      } catch (IOException e) {
        onError.accept(e.getMessage());
      }
    });
  }

  @Override
  public String toString() {
    return FsItem.toString(rPath, getName(), getFileSize());
  }
}
