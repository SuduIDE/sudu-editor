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

  public String getSchemeOrNull() {
    JSString scheme = getScheme();
    return JsHelper.getStringOrNull(scheme);
  }

  public String getAuthorityOrNull() {
    JSString authority = getAuthority();
    return JsHelper.getStringOrNull(authority);
  }

  public String getPathOrNull() {
    JSString path = getPath();
    return JsHelper.getStringOrNull(path);
  }

  @JSBody(params = {"scheme", "authority", "path"}, script = """
    return {scheme: scheme, authority: authority, path: path};
  """)
  private static native JsUri create(String scheme, String authority, String path);

  public static JsUri fromJava(Uri uri) {
    if (uri == null) return null;
    return create(uri.scheme, uri.authority, uri.path);
  }

  public Uri toJava() {
    if (!jsIf(this)) return null;
    return new Uri(
        getSchemeOrNull(),
        getAuthorityOrNull(),
        getPathOrNull()
    );
  }

  public String stringValue() {
    return Objects.toString(getSchemeOrNull(), "") +
        Objects.toString(getAuthorityOrNull(), "") +
        Objects.toString(getPathOrNull(), "");
  }
}
