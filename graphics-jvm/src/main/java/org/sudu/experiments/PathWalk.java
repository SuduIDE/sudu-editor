package org.sudu.experiments;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class PathWalk {

  static void walkFileTree(String path, Consumer<FileHandle> onResult, Executor bgWorker, Executor edt) {
    bgWorker.execute(() -> worker(path, onResult, bgWorker, edt));
  }

  private static void worker(String path, Consumer<FileHandle> onResult, Executor bgWorker, Executor edt) {
    Path start = Paths.get(path);
    ArrayList<FileHandle> files = new ArrayList<>();

    try {
      Files.walkFileTree(start, new SimpleFileVisitor<>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
          FileHandle fh = JvmFileHandle.fromPath(file, toRPath(file, start), bgWorker, edt);
          files.add(fh);
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      System.err.println("Files.walkFileTree error: " + e.getMessage());
    }
    sendResult(onResult, edt, files.toArray(new FileHandle[0]));
  }

  private static String[] toRPath(Path file, Path start) {
    Path relativize = start.relativize(file);
    int nameCount = relativize.getNameCount();
    String[] rPath = new String[nameCount];
    rPath[0] = start.getFileName().toString();
    for (int i = 1; i < rPath.length; i++) rPath[i] = relativize.getName(i - 1).toString();
    return rPath;
  }

  private static void sendResult(Consumer<FileHandle> onResult, Executor edt, FileHandle[] array) {
    edt.execute(() -> {
      for (FileHandle file : array) {
        onResult.accept(file);
      }
    });
  }
}
