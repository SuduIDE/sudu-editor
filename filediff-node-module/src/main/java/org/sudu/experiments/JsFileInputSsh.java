package org.sudu.experiments;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

// export type SshCredentials = { host: string, port: string } & (
//  { username: string, password: string } |
//  { privateKey: string });
// export type SshInput = { path: string, ssh: SSHCredentials }

public interface JsFileInputSsh extends JSObject {

  class Helper {
    @JSBody(
        params = {"host", "port", "user", "password"},
        script = "return {host:host, port:port, username:user, password:password};")
    public static native JaSshCredentials create(
        JSString host, JSString port, JSString user, JSString password
    );
  }

  static boolean isInstance(JSObject input) {
    return JSObjects.hasProperty(input, "ssh")
        && JSObjects.hasProperty(input, "path");
  }

  static JSString getPath(JSObject input) {
    return input.<JsFileInputSsh>cast().getPath();
  }

  static JaSshCredentials getSsh(JSObject input) {
    return input.<JsFileInputSsh>cast().getSsh();
  }

  @JSProperty
  JSString getPath();
  @JSProperty
  JaSshCredentials getSsh();
}
