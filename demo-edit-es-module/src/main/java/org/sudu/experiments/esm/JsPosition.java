package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

abstract class JsPosition implements JSObject {
  @JSProperty abstract int getColumn();
  @JSProperty abstract int getLineNumber();

  @JSBody(params = {"column", "lineNumber"}, script =
      "return {column: column, lineNumber: lineNumber};"
  )
  public static native JsPosition create(int column, int lineNumber);
}
