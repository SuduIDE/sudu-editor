package org.sudu.experiments;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileFactory {

  static final Function<byte[], String> asUtf8String = b -> new String(b, StandardCharsets.UTF_8);
  static final Function<byte[], byte[]> identity = b -> b;

  public static FileHandle fromPath(String path, String[] rPath, Executor bgWorker, Executor edt) {
    return fromPath(Path.of(path), rPath, bgWorker, edt);
  }

  public static FileHandle fromPath(Path path, String[] rPath, Executor bgWorker, Executor edt) {
    return new FileHandle() {

      @Override
      public int getSize() {
        try {
          long size = Files.size(path);
          if (size > (int)size) throw new IOException("size > (int)size");
          return (int)size;
        } catch (IOException e) {
          return 0;
        }
      }

      @Override
      public String getName() {
        return path.getFileName().toString();
      }

      @Override
      public String[] getPath() {
        return rPath;
      }

      @Override
      public void readAsText(Consumer<String> consumer, Consumer<String> onError) {
        read(consumer, onError, asUtf8String);
      }

      @Override
      public void readAsBytes(Consumer<byte[]> consumer, Consumer<String> onError) {
        read(consumer, onError, identity);
      }

      <T>void read(Consumer<T> consumer, Consumer<String> onError, Function<byte[], T> transform) {
        bgWorker.execute(() -> {
          try {
            byte[] allBytes = Files.readAllBytes(path);
            edt.execute(() -> consumer.accept(transform.apply(allBytes)));
          } catch (IOException e) {
            onError.accept(e.getMessage());
          }
        });
      }

      @Override
      public String toString() {
        return FileHandle.toString(rPath, getName(), getSize());
      }
    };
  }
}
