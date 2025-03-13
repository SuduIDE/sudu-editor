package org.sudu.experiments.js;

import org.teavm.jso.typedarrays.*;

@SuppressWarnings("ClassCanBeRecord")
public class JsArrayView implements org.sudu.experiments.worker.ArrayView {
  public final ArrayBuffer ab;

  public JsArrayView(ArrayBuffer ab) { this.ab = ab; }

  @Override
  public byte[] bytes() {
    return JsMemoryAccess.toJavaArray(Int8Array.create(ab));
  }

  @Override
  public char[] chars() {
    return JsMemoryAccess.toJavaArray(Uint16Array.create(ab));
  }

  @Override
  public int[] ints() {
    return JsMemoryAccess.toJavaArray(Int32Array.create(ab));
  }

  @Override
  public double[] numbers() {
    return JsMemoryAccess.toJavaArray(Float64Array.create(ab));
  }

  @Override
  public String toString() {
    return "JsArrayView{ buffer.byteLength = " + ab.getByteLength() + " }";
  }
}
