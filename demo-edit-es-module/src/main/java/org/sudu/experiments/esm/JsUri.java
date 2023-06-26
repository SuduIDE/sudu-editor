package org.sudu.experiments.esm;

import org.sudu.experiments.demo.Uri;
import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

import java.util.Objects;

import static org.sudu.experiments.js.JsHelper.jsIf;

public abstract class JsUri implements JSObject {

  @JSProperty abstract JSString getScheme();
  @JSProperty abstract JSString getAuthority();
  @JSProperty abstract JSString getPath();
  @JSProperty abstract JSObject getJavaPeer();

  public String getSchemeOrNull() {
    return JsHelper.toString(getScheme(), null);
  }

  public String getAuthorityOrNull() {
    return JsHelper.toString(getAuthority(), null);
  }

  public String getPathOrNull() {
    return JsHelper.toString(getPath(), null);
  }

  @JSBody(
      params = {"scheme", "authority", "path", "javaPeer"},
      script = "return {scheme: scheme, authority: authority, path: path, javaPeer:javaPeer};")
  private static native JsUri create(
      String scheme,
      String authority,
      String path,
      JSObject javaPeer);

  public static JsUri fromJava(Uri uri) {
    if (uri == null) return null;
    if (uri.nativeObject != null) return (JsUri) uri.nativeObject;
    return create(uri.scheme, uri.authority, uri.path, (JSObject) uri);
  }

  public Uri toJava() {
    if (!jsIf(this)) return null;
    Object javaPeer = getJavaPeer();
    if (javaPeer instanceof Uri uri) return uri;
    return new Uri(
        getSchemeOrNull(),
        getAuthorityOrNull(),
        getPathOrNull(),
        this);
  }
}
