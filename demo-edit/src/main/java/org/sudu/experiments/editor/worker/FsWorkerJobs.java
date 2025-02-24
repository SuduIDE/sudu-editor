package org.sudu.experiments.editor.worker;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.FsItem;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface FsWorkerJobs {
  String copyFile = "asyncCopyFile";
  int blockSize = 1024 * 1024;

  static void copyFile(
      Object[] args,
      Consumer<Object[]> r
  ) {
    FileHandle src = (FileHandle) args[0];
    FsItem dst = (FsItem) args[1];
    if (dst instanceof FileHandle dstFile) {
      new CopyFile(r, src, dstFile);
    } else if (dst instanceof DirectoryHandle dstDir) {
      dstDir.createFile(src.getName(),
          newFile -> new CopyFile(r, src, newFile),
          e -> postError(e, r)
      );
    } else {
      postError("IllegalAccessException", r);
    }
  }

  static void postCopyOk(int bytesWritten, Consumer<Object[]> r) {
    r.accept(new Object[] { new int[] {1, bytesWritten} });
  }

  static void postError(String error, Consumer<Object[]> r) {
    r.accept(new Object[] { new int[] {0}, error});
  }

  static void onReply(
      Object[] packet,
      IntConsumer onComplete,
      Consumer<String> onError
  ) {
    int[] data = ArgsCast.array(packet, 0).ints();
    if (packet.length == 1 && data.length == 2 && data[0] == 1) {
      onComplete.accept(data[1]);
    } else {
      boolean errorMsg = packet.length == 2 && data[0] == 0;
      var message = errorMsg ? ArgsCast.string(packet, 1) : null;
      onError.accept(message);
    }
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
}
