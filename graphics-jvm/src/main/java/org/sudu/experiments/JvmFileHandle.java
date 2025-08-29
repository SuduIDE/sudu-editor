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
import java.util.function.DoubleConsumer;

// JvmFileHandle reads synchronously on worker threads
// use SyncAccess for many small reads
public class JvmFileHandle extends JvmFsHandle implements FileHandle {

  static final Set<OpenOption> none = Collections.emptySet();
  static final FileAttribute[] noAttributes = new FileAttribute[0];

  public JvmFileHandle(Path path, Path root, Executor bgWorker, Executor edt) {
    super(path, root, bgWorker, edt);
  }

  protected JvmFileHandle ctor(Executor edt) {
    return new JvmFileHandle(path, root, bgWorkerHi, edt);
  }

  @Override
  public void getSize(DoubleConsumer result, Consumer<String> onError) {
    try {
      long size = Files.size(path);
      result.accept(size);
    } catch (IOException e) {
      onError.accept(e.getMessage());
    }
  }

  @Override
  public void readAsBytes(
      Consumer<byte[]> consumer, Consumer<String> onError,
      double begin, int length
  ) {
    read(consumer, onError, (long) begin, length);
  }

  void read(
      Consumer<byte[]> consumer,
      Consumer<String> onError,
      long begin, int length
  ) {
    if (isOnWorker()) {
      read0(consumer, onError, begin, length);
    } else {
      bgWorkerHi.execute(
          () -> read0(consumer, onError, begin, length));
    }
  }

  void read0(
      Consumer<byte[]> consumer,
      Consumer<String> onError,
      long position, int length
  ) {
    try (SeekableByteChannel ch = openByteChannel()) {
      if (position != 0)
        ch.position(position);
      int avl = intSize(ch.size() - position);
      int readL = length < 0 ? avl : Math.min(length, avl);
      var data = avl <= 0
          ? new byte[0]
          : readChannel(readL, ch);
      if (isOnWorker()) {
        consumer.accept(data);
      } else {
        edt.execute(() -> consumer.accept(data));
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

  static byte[] readChannel(int length, SeekableByteChannel ch
  ) throws IOException {
    byte[] data = new byte[length];
    ch.read(ByteBuffer.wrap(data));
    return data;
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

  static void print(IOException e) {
    System.err.println(e.getMessage());
  }
}
