package org.sudu.experiments.js.node;

import org.teavm.interop.NoSideEffects;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

//  READ: 0x00000001,
//  WRITE: 0x00000002,
//  APPEND: 0x00000004,
//  CREAT: 0x00000008,
//  TRUNC: 0x00000010,
//  EXCL: 0x00000020
public abstract class OPEN_MODE implements JSObject {
  @NoSideEffects
  @JSBody(script = "return OPEN_MODE;")
  public static native OPEN_MODE OPEN_MODE();

  public static int read() {
    return OPEN_MODE().READ();
  }

  public static int write_create_trunc() {
    return OPEN_MODE().WRITE() | OPEN_MODE().CREAT() | OPEN_MODE().TRUNC();
  }

  public static int append() {
    return OPEN_MODE().WRITE() | OPEN_MODE().APPEND();
  }

  @JSProperty("READ")
  public native int READ();
  @JSProperty("WRITE")
  public native int WRITE();
  @JSProperty("APPEND")
  public native int APPEND();
  @JSProperty("CREAT")
  public native int CREAT();
  @JSProperty("TRUNC")
  public native int TRUNC();
}
