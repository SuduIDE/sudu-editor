package org.sudu.experiments;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.concurrent.Executor;

class JvmDirectoryHandle extends JvmFsHandle implements DirectoryHandle {

  JvmDirectoryHandle(String path, Path root, Executor bgWorker, Executor edt) {
    super(path, root, bgWorker, edt);
  }

  JvmDirectoryHandle(Path path, Path root, Executor bgWorker, Executor edt) {
    super(path, root, bgWorker, edt);
  }

  // Returns a handle to same file, but with specified event thread
  // If edt argument is null then the background worker's bus is used
  public JvmDirectoryHandle withEdt(Executor edt) {
    return this.edt == edt ? this :
        new JvmDirectoryHandle(path, root, bgWorker,
            edt != null ? edt : bgWorker);
  }

  @Override
  public void read(Reader reader) {
    var visitor = new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        if (exc != null)
          System.err.println(msg(dir, exc, "folder visiting error: "));
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(msg(file, exc, "file visit error: "));
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (attrs.isDirectory()) {
          var dh = new JvmDirectoryHandle(file, root, bgWorker, edt);
          sendBack(dh, reader, edt);
        }
        if (attrs.isRegularFile()) {
          var fh = new JvmFileHandle(file, root, bgWorker, edt);
          sendBack(fh, reader, edt);
        }
        return FileVisitResult.CONTINUE;
      }
    };

    bgWorker.execute(() -> {
      try {
        Files.walkFileTree(
            path, EnumSet.noneOf(FileVisitOption.class), 1, visitor);
        sendComplete(reader, edt);
      } catch (IOException e) {
        System.err.println("Files.walkFileTree error: " + e.getMessage());
      }
    });
  }

  static String msg(Path dir, IOException exc, String title) {
    return title + exc.getMessage() + ", path = " + dir;
  }

  static void sendComplete(Reader reader, Executor edt) {
    edt.execute(reader::onComplete);
  }

  static void sendBack(JvmFileHandle fh, Reader reader, Executor edt) {
    edt.execute(() -> reader.onFile(fh));
  }

  static void sendBack(JvmDirectoryHandle dh, Reader reader, Executor edt) {
    edt.execute(() -> reader.onDirectory(dh));
  }

  @Override
  public String toString() {
    return FsItem.fullPath(getPath(), getName());
  }

}
