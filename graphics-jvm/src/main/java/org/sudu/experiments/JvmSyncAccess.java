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
  public boolean close() {
    try {
      ch.close();
      return true;
    } catch (IOException e) {
      JvmFileHandle.print(e);
      return false;
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
  public int read(byte[] buf, double filePos) throws IOException {
    ch.position((long) filePos);
    return ch.read(ByteBuffer.wrap(buf));
  }

  @Override
  public int write(byte[] buf, double filePos) throws IOException {
    ch.position((long) filePos);
    return ch.write(ByteBuffer.wrap(buf));
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
