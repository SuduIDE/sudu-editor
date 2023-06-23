package org.sudu.experiments.esm;

import org.sudu.experiments.demo.Range;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsRange implements JSObject {
  @JSProperty public abstract int getEndColumn();
  @JSProperty public abstract int getEndLineNumber();
  @JSProperty public abstract int getStartColumn();
  @JSProperty public abstract int getStartLineNumber();

  @JSBody(params = {"endColumn", "endLineNumber", "startColumn", "startLineNumber"}, script =
      "return { endColumn: endColumn, endLineNumber: endLineNumber, " +
          "startColumn: startColumn, startLineNumber: startLineNumber}"
  )
  public static native JsRange create(int endColumn, int endLineNumber, int startColumn, int startLineNumber);

  public void toJava(Range range) {
    range.startColumn = getStartColumn();
    range.startLineNumber = getStartLineNumber();
    range.endColumn = getEndColumn();
    range.endLineNumber = getEndLineNumber();
  }
}
