package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.ArrayBuffer;

public class Fetch {
  @JSBody(params = { "r" }, script = "return fetch(r);")
  public static native Promise<Response> fetch(String r);

  public interface Response extends JSObject {
    Promise<ArrayBuffer> arrayBuffer();
    Promise<JSString> text();
    Promise<JSObject> json();
  }
}
