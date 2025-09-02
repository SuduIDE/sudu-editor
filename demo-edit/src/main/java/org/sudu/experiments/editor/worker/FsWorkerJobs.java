package org.sudu.experiments.editor.worker;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.TextDecoder;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public interface FsWorkerJobs {
  String asyncCopyFile = "asyncCopyFile";
  int blockSize = 1024 * 1024;

  static void copyFile(
      WorkerJobExecutor workers, FileHandle src, FsItem dst,
      DoubleConsumer onComplete, Consumer<String> onError
  ) {
    Consumer<Object[]> onCopy = r -> {
      if (r[0] instanceof String message) {
        onError.accept(message);
      } else {
        int[] data = ArgsCast.intArray(r, 0);
        onComplete.accept(FileHandle.int2Address(data[0], data[1]));
      }
    };
    workers.sendToWorker(onCopy,
        FsWorkerJobs.asyncCopyFile, src, dst);
  }

  static void postCopyOk(double bytesWritten, Consumer<Object[]> r) {
    r.accept(new Object[]{new int[]{
        FileHandle.loGb(bytesWritten), FileHandle.hiGb(bytesWritten)
    }});
  }

  static void postCopyError(String error, Consumer<Object[]> r) {
    r.accept(new Object[]{ error });
  }

  static void asyncCopyFile(Object[] args, Consumer<Object[]> r) {
    FileHandle src = (FileHandle) args[0];
    FileHandle dest = null;
    if (args[1] instanceof FileHandle dstFile) {
      dest = dstFile;
    } else if (args[1] instanceof DirectoryHandle dstDir) {
      dest = dstDir.createFileHandle(src.getName());
    }
    if (dest != null) {
      new CopyFile(r, src, dest);
    } else {
      postCopyError("asyncCopyFile bad args", r);
    }
  }

  String asyncFileWriteText = "asyncFileWriteText";

  static void fileWriteText(
      WorkerJobExecutor workers,
      FileHandle file, char[] content, String encoding,
      Runnable onComplete, Consumer<String> onError
  ) {
    workers.sendToWorker(true, on(onComplete, onError),
        asyncFileWriteText, file, content, encoding);
  }

  // args: FileHandle file, char[] content, String encoding
  // returns [ String error ] or [ null ]
  static void asyncFileWriteText(Object[] args, Consumer<Object[]> r) {
    FileHandle file = ArgsCast.file(args, 0);
    char[] content = ArgsCast.array(args, 1).chars();
    String encoding = ArgsCast.string(args, 2);
    file.writeText(content, encoding,
        () -> r.accept(new Object[0]),
        error -> r.accept(new Object[]{error}));
  }

  String asyncReadTextFile = "asyncReadTextFile";

  static void readTextFile(
      WorkerJobExecutor workers, FileHandle file,
      BiConsumer<char[], String> onComplete, Consumer<String> onError
  ) {
    workers.sendToWorker(true, r -> {
      if (r.length == 2) {
        onComplete.accept(
            ArgsCast.charArray(r, 0),
            ArgsCast.string(r, 1));
      } else {
        onError.accept(ArgsCast.string(r, 0));
      }
    }, asyncReadTextFile, file);
  }

  static void asyncReadTextFile(Object[] args, Consumer<Object[]> r) {
    FileHandle file = ArgsCast.file(args, 0);

    file.readAsBytes(bytes -> {
      boolean gbk = FileEncoding.needGbk(bytes);
      char[] text = gbk
          ? TextDecoder.gbkToChar(bytes)
          : TextDecoder.utf8ToChar(bytes);
      String encoding = gbk ? FileEncoding.gbk : FileEncoding.utf8;
      r.accept(new Object[]{text, encoding});
    }, error -> r.accept(new Object[]{error}));
  }

  String asyncRemoveFile = "asyncRemoveFile";

  static void removeFile(
      WorkerJobExecutor workers, FileHandle file,
      Runnable onComplete, Consumer<String> onError
  ) {
    workers.sendToWorker(on(onComplete, onError),
        FsWorkerJobs.asyncRemoveFile, file);
  }

  // args: FileHandle file
  // returns [ String error ] or [ null ]
  static void asyncRemoveFile(Object[] args, Consumer<Object[]> r) {
    FileHandle file = ArgsCast.file(args, 0);
    file.remove(() -> r.accept(new Object[0]),
        error -> r.accept(new Object[]{error}));
  }

  String asyncRemoveDir = "asyncRemoveDir";

  static void removeDir(
      WorkerJobExecutor workers, DirectoryHandle dir,
      Runnable onComplete, Consumer<String> onError
  ) {
    workers.sendToWorker(on(onComplete, onError),
        FsWorkerJobs.asyncRemoveDir, dir);
  }

  // args: FileHandle file
  // returns [ String error ] or [ null ]
  static void asyncRemoveDir(Object[] args, Consumer<Object[]> r) {
    DirectoryHandle dir = ArgsCast.dir(args, 0);
    dir.remove(() -> r.accept(new Object[0]),
        error -> r.accept(new Object[]{error}));
  }

  static Consumer<Object[]> on(
      Runnable onComplete, Consumer<String> onError
  ) {
    return r -> {
      if (r.length == 0) {
        onComplete.run();
      } else {
        onError.accept(ArgsCast.string(r,0));
      }
    };
  }

  String asyncMkDir = "asyncMkDir";

  static void mkDir(
      WorkerJobExecutor workers,
      DirectoryHandle dir, String name,
      Consumer<DirectoryHandle> onResult,
      Consumer<String> onError
  ) {
    workers.sendToWorker(
        r -> {
          if (r[0] instanceof DirectoryHandle directoryHandle) {
            onResult.accept(directoryHandle);
          } else {
            onError.accept(ArgsCast.string(r, 0));
          }
        }, asyncMkDir,
        dir, name
    );
  }

  static void asyncMkDir(Object[] args, Consumer<Object[]> r) {
    DirectoryHandle dir = ArgsCast.dir(args, 0);
    String name = ArgsCast.string(args, 1);
    dir.createDirectory(name,
        newDir -> r.accept(new Object[]{newDir}),
        error -> r.accept(new Object[]{error}));
  }

  String asyncStats = "asyncStats";

  static void asyncStats(
      WorkerJobExecutor workers, FileHandle fileOrDir,
      Consumer<FileHandle.Stats> onComplete, Consumer<String> onError
  ) {
    workers.sendToWorker(
        r -> {
          if (r[0] instanceof String error) {
            onError.accept(error);
          } else {
            onComplete.accept(unpackStats(
                ArgsCast.array(r, 0).numbers()));
          }
        }, asyncStats, fileOrDir);
  }

  static void asyncStats(Object[] args, Consumer<Object[]> r) {
    FileHandle file = ArgsCast.file(args, 0);
    file.stat((stats, error) -> {
      if (error != null) r.accept(new Object[]{error});
      else r.accept(new Object[]{packStats(stats)});
    });
  }

  static double[] packStats(FileHandle.Stats stats) {
    int flags = (stats.isDirectory ? 1 : 0) |
        (stats.isFile ? 2 : 0) | (stats.isSymbolicLink ? 4 : 0);
    return new double[]{flags, stats.size};
  }

  static FileHandle.Stats unpackStats(double[] packed) {
    int flags = (int) packed[0];
    return new FileHandle.Stats(
        (flags & 1) != 0, (flags & 2) != 0,
        (flags & 4) != 0, packed[1]);
  }
}
