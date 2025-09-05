package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;

import java.io.IOException;
import java.util.function.Consumer;

class CopyFile {
  // D2D.ipch README.md zeroFile 3mbFile error
  static final boolean debug = false;

  final Consumer<Object[]> r;
  final FileHandle src, dst;
  FileHandle.SyncAccess srcAccess, dstAccess;
  final Consumer<String> onError = this::onError;

  // write queue
  byte[] nextData;
  double nextPos;

  boolean writing, complete;
  int depth = 0;

  CopyFile(
      Consumer<Object[]> r,
      FileHandle src, FileHandle dst
  ) {
    this.r = r;
    this.src = src;
    this.dst = dst;
    if (src.hasSyncAccess() && dst.hasSyncAccess()) {
      syncCopy();
    } else {
      read(0);
    }
  }

  private void syncCopy() {
    src.syncAccess(this::setSrcAccess, onError, false);
    dst.syncAccess(this::setDstAccess, onError, true);
  }

  private void setDstAccess(FileHandle.SyncAccess da) {
    dstAccess = da;
    if (srcAccess != null) copyFileSync();
  }

  private void setSrcAccess(FileHandle.SyncAccess sa) {
    srcAccess = sa;
    if (dstAccess != null) copyFileSync();
  }

  private void copyFileSync() {
    double size = srcAccess.getSize(), pos = 0;
    int limited = size < 1024 * 1024 ? (int) size : 1024 * 1024;
    nextData = new byte[limited];
    for (; pos < size; ) {
      try {
        double read = srcAccess.read(nextData, pos);
        if (read > 0) {
          double write = dstAccess.write(nextData, pos);
          if (write != read) {
            close();
            onError("file '" + dst.getFullPath() +
                "': write failed to write " + ((int)write) + " bytes");
            break;
          } else  {
            pos += read;
          }
        } else {
          close();
          onError("file '" + src.getFullPath() +
              "': read failed at position" + pos);
          break;
        }
      } catch (IOException e) {
        close();
        onError(e.getMessage());
        break;
      }
    }
    if (pos == size)
      postComplete(size);
  }

  private void close() {
    srcAccess.close();
    dstAccess.close();
    // todo: do we need to delete the dst file ?
  }

  void onError(String error) {
    System.out.println("CopyFile.onError: " + error);
    FsWorkerJobs.postCopyError(error, r);
  }

  void postComplete(double bytesWritten) {
    if (debug) System.out.println(
        " -> postComplete: bytesWritten = " + bytesWritten);
    FsWorkerJobs.postCopyOk(bytesWritten, r);
  }

  String hdr() {
    return "CopyFile(" + src.getName() + "->" + dst.getName() + ").";
  }

  void read(double pos) {
    if (debug && depth % 100 == 0)
      System.out.println(hdr() + "Read at " + pos + ", depth = " + depth);
    depth++;
    src.readAsBytes(data -> onRead(data, pos), onError, pos, FsWorkerJobs.blockSize);
    depth--;
  }

  void onRead(byte[] data, double pos) {
    if (debug && depth % 100 == 0)
      System.out.println(
          hdr() + "onRead: data.l = " + data.length + ", pos = " + pos + ", d = " + depth);
    if (writing) {
      if (nextData != null) {
        onError("internal error 1");
      } else {
        if (debug && depth % 100 == 0)
          System.out.println("  in writing, data -> nextData");
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

  void onWriteComplete(double bytesWritten) {
    writing = false;
    if (debug && depth % 100 == 0) {
      var lStr = nextData != null
          ? Integer.toString(nextData.length) : "null";
      System.out.println(
          hdr() + "onWriteComplete: nextData.l = " + lStr + ", depth = " + depth);
    }
    if (nextData != null) {
      var data = nextData;
      var pos = nextPos;
      nextData = null;
      nextPos = 0;
      writeAndFetch(data, pos);
    } else {
      if (complete) {
        postComplete(bytesWritten);
      } else {
        if (debug && depth % 100 == 0) System.out.println(
            "  waiting read to complete, depth = " + depth);
      }
    }
  }

  void writeAndFetch(byte[] data, double pos) {
    if (debug && depth % 100 == 0)
      System.out.println(
          hdr() + "writeAndFetch: data.l = " + data.length
              + ", pos = " + pos + ", depth = " + depth);

    if (pos > 0 && data.length == 0) {
      postComplete(pos);
      return;
    }

    writing = true;
    double bytesWritten = pos + data.length;
    Runnable onComplete = () -> onWriteComplete(bytesWritten);
    if (data.length < FsWorkerJobs.blockSize) {
      complete = true;
      if (debug) System.out.println(
          "  read completed, total bytes =" + bytesWritten);
    }

    if (pos == 0)
      dst.writeText(data, null, onComplete, onError);
    else
      dst.writeAppend(pos, data, onComplete, onError);

    if (data.length == FsWorkerJobs.blockSize)
      read(pos + FsWorkerJobs.blockSize);
  }
}
