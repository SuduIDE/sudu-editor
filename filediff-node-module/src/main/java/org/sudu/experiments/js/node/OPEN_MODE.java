package org.sudu.experiments.js.node;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

//  READ: 0x00000001,
//  WRITE: 0x00000002,
//  APPEND: 0x00000004,
//  CREAT: 0x00000008,
//  TRUNC: 0x00000010,
//  EXCL: 0x00000020
public class OPEN_MODE implements JSObject {
  @JSBody(script = "return OPEN_MODE;")
  public static native OPEN_MODE OPEN_MODE();

  @JSProperty("READ")
  public native int READ();
  @JSProperty("WRITE")
  public native int WRITE();
  @JSProperty("APPEND")
  public native int APPEND();
  @JSProperty("CREAT")
  public native int CREAT();
}
