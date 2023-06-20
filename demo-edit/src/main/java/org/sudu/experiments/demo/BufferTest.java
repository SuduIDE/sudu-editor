package org.sudu.experiments.demo;

import java.nio.*;

public class BufferTest {
  public static void test() {
    ByteBuffer bb = ByteBuffer.allocate(0);
    bb.put(new byte[0]);
    bb.get(new byte[0]);

    CharBuffer cb = CharBuffer.allocate(0);
    cb.put("");
    cb.get(new char[0]);

    ShortBuffer sb = ShortBuffer.allocate(0);
    sb.put(new short[0]);
    sb.get(new short[0]);

    IntBuffer ib = IntBuffer.allocate(0);
    ib.put(new int[0]);
    ib.get(new int[0]);

    FloatBuffer fb = FloatBuffer.allocate(0);
    fb.put(new float[0]);
    fb.get(new float[0]);

    DoubleBuffer db = DoubleBuffer.allocate(0);
    db.put(new double[0]);
    db.get(new double[0]);

    LongBuffer lb = LongBuffer.allocate(0);
    lb.put(new long[0]);
    lb.get(new long[0]);
  }
}
