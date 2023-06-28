package org.sudu.experiments.esm;

import org.sudu.experiments.parser.common.Pos;
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

  public static JsPosition fromJava(Pos pos) {
    return JsPosition.create(pos.pos + 1, pos.line + 1);
  }

  public static JsPosition fromJava(int line, int column) {
    return JsPosition.create(column + 1, line + 1);
  }

  public static boolean isInstance(JSObject obj) {
    return JSObjects.hasProperty(obj, "column")
        && JSObjects.hasProperty(obj, "lineNumber");
  }
}
