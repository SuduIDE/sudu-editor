package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;

public abstract class JsPosition implements JSObject {
  @JSProperty
  public abstract int getColumn();
  @JSProperty
  public abstract int getLineNumber();

  @JSBody(params = {"column", "lineNumber"}, script =
      "return {column: column, lineNumber: lineNumber};"
  )
  public static native JsPosition create(int column, int lineNumber);

  public static boolean isInstance(JSObject obj) {
    return JSObjects.hasProperty(obj, "column")
        && JSObjects.hasProperty(obj, "lineNumber");
  }
}
