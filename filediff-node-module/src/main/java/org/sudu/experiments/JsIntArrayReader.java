package org.sudu.experiments;

import org.sudu.experiments.arrays.IntsReader;
import org.teavm.jso.typedarrays.Int32Array;

public class JsIntArrayReader implements IntsReader {

  private final Int32Array source;
  private int pointer;

  public JsIntArrayReader(Int32Array source) {
    this.source = source;
    pointer = 0;
  }

  public int next() {
    return source.get(pointer++);
  }
}
