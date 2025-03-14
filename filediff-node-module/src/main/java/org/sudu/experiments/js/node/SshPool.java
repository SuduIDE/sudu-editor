package org.sudu.experiments.js.node;

import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSError;
import org.teavm.jso.core.JSObjects;

import java.util.HashMap;
import java.util.Map;

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

  Map<SshHash, Value> map = new HashMap<>();

  static void sftp(
      SshHash creds,
      JsFunctions.Consumer<JsSftpClient> callback,
      JsFunctions.Consumer<JSError> error
  ) {
    connect(creds).then(
        r -> callback.f(r.getSftp()), error
    );
  }

  @SuppressWarnings("Convert2MethodRef")
  static Promise<Record> connect(SshHash key) {
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
            client.onError(postError);
            client.connect(key.jsSshCredentials());
          }
      );
      map.put(key, value = new Value(p));
    }
    return value.v;
  }

  static void terminate() {
    for (var e: map.entrySet()) {
      e.getValue().v.then(r -> {
        JsHelper.consoleInfo2("terminate a ssh", e.getKey().host);
        r.getSsh().end();
      }, jsError -> {});
    }
    map.clear();
  }
}
