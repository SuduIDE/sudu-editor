package org.sudu.experiments.js.node;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

// export type SshCredentials = { host: string, port: string } & (
//  { username: string, password: string } |
//  { username: string, privateKey: string });

public abstract class JsSshCredentials implements JSObject {
  @JSProperty
  public native JSString getHost();

  @JSProperty
  public native JSString getPort();

  @JSProperty
  public native JSString getUsername();

  @JSProperty
  public native JSString getPassword();

  @JSProperty
  public native JSString getPrivateKey();

  @JSBody(
      params = {"host", "port", "username", "password"},
      script = "return {host:host, port:port, username:username, password:password};")
  public static native JsSshCredentials createWithPassword(
      JSString host, JSString port, JSString username, JSString password
  );

  @JSBody(
      params = {"host", "port", "username", "privateKey"},
      script = "return {host:host, port:port, username:username, privateKey:privateKey};")
  public static native JsSshCredentials createWithPrivateKey(
      JSString host, JSString port, JSString username, JSString privateKey
  );
}
