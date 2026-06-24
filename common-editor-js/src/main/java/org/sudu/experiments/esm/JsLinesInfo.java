package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.JSObject;

public abstract class JsLinesInfo implements JSObject {

  @JSProperty
  public abstract int getLinesAdded();

  @JSProperty
  public abstract int getLinesRemoved();

  @JSProperty
  public abstract int getLinesModified();

  @JSBody(params = {"linesAdded", "linesRemoved", "linesModified"},
      script = "return {linesAdded: linesAdded, linesRemoved: linesRemoved, linesModified: linesModified};"
  )
  public static native JsLinesInfo create(int linesAdded, int linesRemoved, int linesModified);

}
