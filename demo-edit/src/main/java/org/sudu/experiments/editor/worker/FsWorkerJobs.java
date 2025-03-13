package org.sudu.experiments.editor.worker;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;
import org.sudu.experiments.encoding.FileEncoding;
import org.sudu.experiments.encoding.TextDecoder;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface FsWorkerJobs {
  String asyncCopyFile = "asyncCopyFile";
  int blockSize = 1024 * 1024;

  static void copyFile(
      WorkerJobExecutor workers, FileHandle src, FsItem dst,
      IntConsumer onComplete, Consumer<String> onError
  ) {
    workers.sendToWorker(onCopy(onComplete, onError),
        FsWorkerJobs.asyncCopyFile, src, dst);
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
      postError("asyncCopyFile bad args", r);
    }
  }

  static void postCopyOk(int bytesWritten, Consumer<Object[]> r) {
    r.accept(new Object[] { new int[] {1, bytesWritten} });
  }

  static void postError(String error, Consumer<Object[]> r) {
    r.accept(new Object[] { new int[] {0}, error});
  }

  static Consumer<Object[]> onCopy(
      IntConsumer onComplete, Consumer<String> onError
  ) {
    return r -> {
      int[] data = ArgsCast.array(r, 0).ints();
      if (r.length == 1 && data.length == 2 && data[0] == 1) {
        onComplete.accept(data[1]);
      } else {
        boolean errorMsg = r.length == 2 && data[0] == 0;
        var message = errorMsg ? ArgsCast.string(r, 1) : null;
        onError.accept(message);
      }
    };
  }

  class CopyFile {
    static final boolean debug = false;

    final Consumer<Object[]> r;
    final FileHandle src, dst;
    final Consumer<String> onError = this::onError;

    // write queue
    byte[] nextData;
    int    nextPos;

    boolean writing, complete;

    CopyFile(
        Consumer<Object[]> r,
        FileHandle src, FileHandle dst
    ) {
      this.r = r;
      this.src = src;
      this.dst = dst;
      read(0);
    }

    void onError(String error) {
      postError(error, r);
    }

    void postComplete(int bytesWritten) {
      if (debug) System.out.println(
          " -> postComplete: bytesWritten = " + bytesWritten);
      postCopyOk(bytesWritten, r);
    }

    String hdr() {
      return "CopyFile(" + src.getName() + "->" + dst.getName() + ").";
    }

    void read(int pos) {
      if (debug) System.out.println(hdr() + "Read at " + pos);
      src.readAsBytes(data -> onRead(data, pos), onError, pos, blockSize);
    }

    void onRead(byte[] data, int pos) {
      if (debug) System.out.println(
          hdr() + "onRead: data.l = " + data.length + ", pos = " + pos);
      if (writing) {
        if (nextData != null) {
          onError("internal error 1");
        } else {
          if (debug) System.out.println(
              "  in writing, data -> nextData");
          nextData = data;
          nextPos = pos;
        }
      } else {
        if (nextData != null) {
          onError("internal error 2");
        } else {
          writeAndFetch(data, pos);
        }
      }
    }

    void onWriteComplete(int bytesWritten) {
      writing = false;
      if (debug) {
        var lStr = nextData != null
            ? Integer.toString(nextData.length) : "null";
        System.out.println(
            hdr() + "onWriteComplete: nextData.l = " + lStr);
      }
      if (nextData != null) {
        var data = nextData;
        var pos = nextPos;
        nextData = null; nextPos = 0;
        writeAndFetch(data, pos);
      } else {
        if (complete) {
          postComplete(bytesWritten);
        } else {
          if (debug) System.out.println(
              "  waiting read to complete");
        }
      }
    }

    void writeAndFetch(byte[] data, int pos) {
      if (debug) System.out.println(
          hdr() + "writeAndFetch: data.l = " + data.length
              + ", pos = " + pos);

      if (pos > 0 && data.length == 0) {
        postComplete(pos);
        return;
      }

      writing = true;
      int bytesWritten = pos + data.length;
      Runnable onComplete = () -> onWriteComplete(bytesWritten);
      if (data.length < blockSize) {
        complete = true;
        if (debug) System.out.println(
            "  read completed, total bytes =" + bytesWritten);
      }

      if (pos == 0)
        dst.writeText(data, null, onComplete, onError);
      else
        dst.writeAppend(pos, data, onComplete, onError);

      if (data.length == blockSize)
        read(pos + blockSize);
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
