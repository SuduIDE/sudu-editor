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

  public JvmFileHandle(String path, Path root, Executor bgWorker, Executor edt) {
    super(path, root, bgWorker, edt);
  }

  public JvmFileHandle(Path path, Path root, Executor bgWorker, Executor edt) {
    super(path, root, bgWorker, edt);
  }

  protected JvmFileHandle ctor(Executor edt) {
    return new JvmFileHandle(path, root, bgWorker, edt);
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

  <T> void read(
      Consumer<T> consumer,
      Consumer<String> onError,
      Function<byte[], T> transform
  ) {
    if (isOnWorker()) {
      read0(consumer, onError, transform);
    } else {
      bgWorker.execute(() -> read0(consumer, onError, transform));
    }
  }

  <T> void read0(
      Consumer<T> consumer,
      Consumer<String> onError,
      Function<byte[], T> transform
  ) {
    try {
      byte[] allBytes = Files.readAllBytes(path);
      T apply = transform.apply(allBytes);
      if (isOnWorker()) {
        consumer.accept(apply);
      } else {
        edt.execute(() -> consumer.accept(apply));
      }
    } catch (IOException e) {
      String message = e.getMessage();
      if (isOnWorker()) {
        onError.accept(message);
      } else {
        edt.execute(() -> onError.accept(message));
      }
    }
  }
}
