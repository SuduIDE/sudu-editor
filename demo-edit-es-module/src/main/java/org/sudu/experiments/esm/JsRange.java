package org.sudu.experiments.esm;

import org.sudu.experiments.editor.Range;
import org.sudu.experiments.editor.Selection;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsRange implements JSObject {
  @JSProperty public abstract int getStartLineNumber();
  @JSProperty public abstract int getStartColumn();
  @JSProperty public abstract int getEndLineNumber();
  @JSProperty public abstract int getEndColumn();

  @JSBody(params = {"startLineNumber", "startColumn", "endLineNumber", "endColumn"}, script =
      "return { startLineNumber: startLineNumber, startColumn: startColumn, " +
          "endLineNumber: endLineNumber, endColumn: endColumn}"
  )
  static native JsRange create(int startLineNumber, int startColumn, int endLineNumber, int endColumn);

  public static JsRange fromJava(Selection s) {
    return JsRange.create(
        s.startPos.line + 1, s.startPos.charPos + 1,
        s.endPos.line + 1, s.endPos.charPos + 1);
  }

  public static JsRange fromJava(Range r) {
    return JsRange.create(
        r.startLineNumber + 1, r.startColumn + 1,
        r.endLineNumber + 1, r.endColumn + 1);
  }

  public void toJava(Range range) {
    range.startLineNumber = getStartLineNumber() - 1;
    range.startColumn = getStartColumn() - 1;
    range.endLineNumber = getEndLineNumber() - 1;
    range.endColumn = getEndColumn() - 1;
  }
}
