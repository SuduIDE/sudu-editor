package org.sudu.experiments;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

// JvmFileHandle reads synchronously on worker threads
// use SyncAccess for many small reads
public class JvmFileHandle extends JvmFsHandle implements FileHandle {

  static final Function<byte[], byte[]> identity = b -> b;
  static final Set<OpenOption> none = Collections.emptySet();
  static final FileAttribute[] noAttributes = new FileAttribute[0];

  public JvmFileHandle(Path path, Path root, Executor bgWorker, Executor edt) {
    super(path, root, bgWorker, edt);
  }

  protected JvmFileHandle ctor(Executor edt) {
    return new JvmFileHandle(path, root, bgWorkerHi, edt);
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
      print(e);
      return 0;
    }
  }

  @Override
  public void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError, int begin, int length) {
    read(consumer, onError, identity, begin, length);
  }

  <T> void read(
      Consumer<T> consumer,
      Consumer<String> onError,
      Function<byte[], T> transform,
      int begin, int length
  ) {
    if (isOnWorker()) {
      read0(consumer, onError, transform, begin, length);
    } else {
      bgWorkerHi.execute(
          () -> read0(consumer, onError, transform, begin, length));
    }
  }

  <T> void read0(
      Consumer<T> consumer,
      Consumer<String> onError,
      Function<byte[], T> transform,
      int position, int length
  ) {
    try (SeekableByteChannel ch = openByteChannel()) {
      if (position != 0)
        ch.position(position);
      int avl = intSize(ch.size() - position);
      int readL = length < 0 ? avl : Math.min(length, avl);
      T apply = avl <= 0
          ? transform.apply(new byte[0])
          : readChannel(transform, readL, ch);
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

  SeekableByteChannel openByteChannel() throws IOException {
    return Files.newByteChannel(path, none, noAttributes);
  }

  static <T> T readChannel(
      Function<byte[], T> transform, int length, SeekableByteChannel ch
  ) throws IOException {
    byte[] data = new byte[length];
    ch.read(ByteBuffer.wrap(data));
    return transform.apply(data);
  }

  private int intSize(long size) throws IOException {
    int iSize = (int) size;
    if (iSize != size)
      throw new IOException("file too large");
    return iSize;
  }

  @Override
  public void syncAccess(
      Consumer<FileHandle.SyncAccess> h,
      Consumer<String> onError
  ) {
    try {
      h.accept(new JvmSyncAccess(openByteChannel()));
    } catch (IOException e) {
      onError.accept(e.getMessage());
    }
  }

  @Override
  public void writeText(
      Object text, String encoding,
      Runnable onComplete, Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }

  @Override
  public void copyTo(
      FsItem to,
      Runnable onComplete, Consumer<String> onError
  ) {
    onError.accept("not implemented");
  }

  @Override
  public void remove(Runnable onComplete, Consumer<String> onError) {
    onError.accept("not implemented");
  }

  static void print(IOException e) {
    System.err.println(e.getMessage());
  }
}
