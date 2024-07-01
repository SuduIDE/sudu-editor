package org.sudu.experiments;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

class JvmSyncAccess implements FileHandle.SyncAccess {
  final SeekableByteChannel ch;

  JvmSyncAccess(SeekableByteChannel ch) {
    this.ch = ch;
  }

  @Override
  public void close() {
    try {
      ch.close();
    } catch (IOException e) {
      JvmFileHandle.print(e);
    }
  }

  @Override
  public double getSize() {
    try {
      return ch.size();
    } catch (IOException e) {
      JvmFileHandle.print(e);
    }
    return 0;
  }

  @Override
  public double read(byte[] buf, double filePos) {
    try {
      ch.position((long) filePos);
      return ch.read(ByteBuffer.wrap(buf));
    } catch (IOException e) {
      JvmFileHandle.print(e);
    }
    return 0;
  }

  @Override
  public int hashCode() {
    return ch.hashCode();
  }

  @Override
  public String toString() {
    return ch.toString();
  }
}
