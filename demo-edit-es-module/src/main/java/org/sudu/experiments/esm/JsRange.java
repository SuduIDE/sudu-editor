package org.sudu.experiments.esm;

import org.sudu.experiments.demo.Range;
import org.sudu.experiments.demo.Selection;
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
  static native JsRange create(int endColumn, int endLineNumber, int startColumn, int startLineNumber);

  public static JsRange fromJava(Selection s) {
    return JsRange.create(
        s.endPos.charInd + 1, s.endPos.line + 1,
        s.startPos.charInd + 1, s.startPos.line + 1);
  }

  public static JsRange fromJava(Range r) {
    return JsRange.create(
        r.endColumn + 1, r.endLineNumber + 1,
        r.startColumn + 1, r.startLineNumber + 1);
  }

  public void toJava(Range range) {
    range.startColumn = getStartColumn() - 1;
    range.startLineNumber = getStartLineNumber() - 1;
    range.endColumn = getEndColumn() - 1;
    range.endLineNumber = getEndLineNumber() - 1;
  }
}
