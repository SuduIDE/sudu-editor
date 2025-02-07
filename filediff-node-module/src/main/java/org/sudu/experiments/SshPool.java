package org.sudu.experiments;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.sudu.experiments.js.node.JsSshCredentials;
import org.sudu.experiments.js.node.JsSftpClient;
import org.sudu.experiments.js.node.JsSshClient;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.HashMap;
import java.util.Map;

class Key {
  final JsSshCredentials creds;
  int hash = 0;

  Key(JsSshCredentials creds) {
    this.creds = creds;
  }

  static int hashUpdate(int h, JSString s) {
    int sh = s == null || JSObjects.isUndefined(s) ?
        0 : s.stringValue().hashCode();
    return h * 31 + sh;
  }

  static int hash(JsSshCredentials creds) {
    int result = 1;
    result = hashUpdate(result, creds.getHost());
    result = hashUpdate(result, creds.getPort());
    result = hashUpdate(result, creds.getUsername());
    result = hashUpdate(result, creds.getPassword());
    result = hashUpdate(result, creds.getPrivateKey());
    return result;
  }

  @Override
  public int hashCode() {
    return hash != 0 ? hash : (hash = hash(creds));
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Key key))
      return false;

    JsSshCredentials c2 = key.creds;
    return JsHelper.strictEquals(creds.getHost(), c2.getHost()) &&
        JsHelper.strictEquals(creds.getPort(), c2.getPort()) &&
        JsHelper.strictEquals(creds.getUsername(), c2.getUsername()) &&
        JsHelper.strictEquals(creds.getPassword(), c2.getPassword()) &&
        JsHelper.strictEquals(creds.getPrivateKey(), c2.getPrivateKey());
  }
}

class Value {
  public Promise<SshPool.Record> v;

  public Value(Promise<SshPool.Record> v) {
    this.v = v;
  }
}

class Native {
  @JSBody(script = "return newSshClient();")
  static native JsSshClient newSshClient();

  @JSBody(
      params = {"ssh", "sftp"},
      script = "return {ssh:ssh, sftp:sftp};"
  )
  static native SshPool.Record createRecord(
      JsSshClient ssh, JsSftpClient sftp);
}

public interface SshPool {
  interface Record extends JSObject {
    @JSProperty
    JsSshClient getSsh();
    @JSProperty
    JsSftpClient getSftp();
  }

  Map<Key, Value> map = new HashMap<>();

  static void sftp(
      JsSshCredentials creds,
      JsFunctions.Consumer<JsSftpClient> callback,
      JsFunctions.Consumer<JSError> error
  ) {
    connect(creds).then(
        r -> callback.f(r.getSftp()), error
    );
  }

  static Promise<Record> connect(JsSshCredentials creds) {
    var key = new Key(creds);
    Value value = map.get(key);
    if (value == null) {
      Promise<Record> p = Promise.create(
          (postResult, postError) -> {
            JsSshClient client = Native.newSshClient();
            client.onReady(() -> client.sftp((JSError e, JsSftpClient sftp) -> {
              if (e == null || JSObjects.isUndefined(e)) {
                postResult.f(Native.createRecord(client, sftp));
              } else {
                postError.f(e);
              }
            }));
            client.onError(error -> {
              JsHelper.consoleInfo2("sshClient.onError", error);
              JsHelper.consoleInfo("sshClient.onError forwarding the error...");
              postError.f(error);
            });
            client.connect(creds);
          }
      );
      map.put(key, value = new Value(p));
    }
    return value.v;
  }

  static void terminate() {
    for (var e: map.entrySet()) {
      e.getValue().v.then(r -> {
        JsHelper.consoleInfo2("terminate a ssh", e.getKey().creds.getHost());
        r.getSsh().end();
      }, jsError -> {});
    }
    map.clear();
  }
}
