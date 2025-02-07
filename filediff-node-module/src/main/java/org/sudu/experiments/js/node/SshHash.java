package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsHelper;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class SshHash {
  final JSString host, port, username;
  final JSString password, privateKey;
  final int hash;

  public SshHash(JsSshCredentials c) {
    this(c.getHost(), c.getPort(), c.getUsername(),
        c.getPassword(), c.getPrivateKey());
  }
  public SshHash(
      JSString host, JSString port, JSString username,
      JSString password, JSString privateKey
  ) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    this.privateKey = privateKey;
    hash = computeHash();
  }

  public JsSshCredentials jsSshCredentials() {
    return password != null ?
        JsSshCredentials.createWithPassword(
            host, port, username, password) :
        JsSshCredentials.createWithPrivateKey(
            host, port, username, privateKey);
  }

  static int hashUpdate(int h, JSString s) {
    int sh = s == null || JSObjects.isUndefined(s) ?
        0 : s.stringValue().hashCode();
    return h * 31 + sh;
  }

  int computeHash() {
    int result = 1;
    result = hashUpdate(result, host);
    result = hashUpdate(result, port);
    result = hashUpdate(result, username);
    result = hashUpdate(result, password);
    result = hashUpdate(result, privateKey);
    return result;
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SshHash key))
      return false;

    return JsHelper.strictEquals(host, key.host) &&
        JsHelper.strictEquals(port, key.port) &&
        JsHelper.strictEquals(username, key.username) &&
        JsHelper.strictEquals(password, key.password) &&
        JsHelper.strictEquals(privateKey, key.privateKey);
  }
}
