package org.sudu.experiments;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.EnumSet;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import static java.nio.file.StandardOpenOption.*;

// JvmFileHandle reads synchronously on worker threads
// use SyncAccess for many small reads
public class JvmFileHandle extends JvmFsHandle implements FileHandle {

  static final EnumSet<StandardOpenOption> read = EnumSet.of(READ);
  static final EnumSet<StandardOpenOption> createWriteTruc =
      EnumSet.of(CREATE, WRITE, TRUNCATE_EXISTING);
  static final EnumSet<StandardOpenOption> append =
      EnumSet.of(WRITE, APPEND);
  static final FileAttribute[] att0 = new FileAttribute[0];

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
    try (SeekableByteChannel ch = Files.newByteChannel(path, read, att0)) {
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
      Consumer<SyncAccess> h,
      Consumer<String> onError,
      boolean write
  ) {
    try {
      var channel = Files.newByteChannel(path,
          write ? createWriteTruc : read, att0);
      h.accept(new JvmSyncAccess(channel));
    } catch (IOException e) {
      onError.accept(e.getMessage());
    }
  }

  static void print(IOException e) {
    System.err.println(e.getMessage());
  }
}
