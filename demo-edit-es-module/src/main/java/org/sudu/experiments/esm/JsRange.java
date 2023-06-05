package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

abstract class JsRange implements JSObject {
  @JSProperty abstract int getEndColumn();
  @JSProperty abstract int getEndLineNumber();
  @JSProperty abstract int getStartColumn();
  @JSProperty abstract int getStartLineNumber();

  @JSBody(params = {"endColumn", "endLineNumber", "startColumn", "startLineNumber"}, script =
      "return { endColumn: endColumn, endLineNumber: endLineNumber, " +
          "startColumn: startColumn, startLineNumber: startLineNumber}"
  )
  public static native JsRange create(int endColumn, int endLineNumber, int startColumn, int startLineNumber);
}
