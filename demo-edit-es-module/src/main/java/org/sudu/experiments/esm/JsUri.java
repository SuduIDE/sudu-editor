package org.sudu.experiments.esm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class JsUri implements JSObject {

  @JSProperty abstract String getScheme();
  @JSProperty abstract String getAuthority();
  @JSProperty abstract String getPath();

  @JSBody(params = {"scheme", "authority", "path"}, script = """
    return {scheme: scheme, authority: authority, path: path};
  """)
  private static native JsUri create(String scheme, String authority, String path);
}
