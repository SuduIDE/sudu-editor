package org.sudu.experiments.js.node;

import org.sudu.experiments.js.*;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

public class NodeWorkersBridge implements WorkerProtocol.PlatformBridge {

  @Override
  public int toJs(Object javaObject, JsArray<JSObject> message, int idx) {
    if (javaObject instanceof NodeFileHandle nodeFile) {
      message.set(idx++, JSNumber.valueOf(0));
      message.set(idx++, nodeFile.jsPath());
    } else if (javaObject instanceof NodeDirectoryHandle nodeDir) {
      message.set(idx++, JSNumber.valueOf(1));
      message.set(idx++, nodeDir.jsPath());
    } else if (javaObject instanceof SshDirectoryHandle sshDir) {
      message.set(idx++, JSNumber.valueOf(2));
      message.set(idx++, sshDir.jsPath());
      idx = putSsh(message, idx, sshDir.credentials);
    } else if (javaObject instanceof SshFileHandle sshFile) {
      message.set(idx++, JSNumber.valueOf(3));
      message.set(idx++, sshFile.jsPath());
      message.set(idx++, sshFile.attrs);
      idx = putSsh(message, idx, sshFile.credentials);
    }

    else throw new IllegalArgumentException(
        "Illegal argument sent to worker " + javaObject.getClass().getName()
    );
    return idx;
  }

  @Override
  public int toJava(
      JSObject jsObject, JsArrayReader
      <JSObject> array, int arrayIndex,
      Object[] r, int idx
  ) {
    if (isNumber(jsObject)) {
      int value = jsObject.<JSNumber>cast().intValue();
      switch (value) {
        case 0 -> {
          JSString jsPath = array.get(arrayIndex++).cast();
          r[idx] = new NodeFileHandle(jsPath);
        }
        case 1 -> {
          JSString jsPath = array.get(arrayIndex++).cast();
          r[idx] = new NodeDirectoryHandle(jsPath);
        }
        case 2 -> {
          JSString jsPath = array.get(arrayIndex++).cast();
          var ssh = getSsh(array, arrayIndex);
          r[idx] = new SshDirectoryHandle(jsPath, ssh);
          arrayIndex += 5;
        }
        case 3 -> {
          JSString jsPath = array.get(arrayIndex++).cast();
          JsSftpClient.Attrs attrs = array.get(arrayIndex++).cast();
          var ssh = getSsh(array, arrayIndex);
          r[idx] = new SshFileHandle(jsPath, ssh, attrs);
          arrayIndex += 5;
        }
      }
    }
    return arrayIndex;
  }

  static SshHash getSsh(JsArrayReader<JSObject> array, int arrayIndex) {
    JSString host = array.get(arrayIndex).cast();
    JSString port = array.get(arrayIndex + 1).cast();
    JSString username = array.get(arrayIndex + 2).cast();
    JSString password = array.get(arrayIndex + 3).cast();
    JSString privateKey = array.get(arrayIndex + 4).cast();
    return new SshHash(host, port, username, password, privateKey);
  }

  static int putSsh(JsArray<JSObject> message, int idx, SshHash sshDir) {
    message.set(idx++, sshDir.host);
    message.set(idx++, sshDir.port);
    message.set(idx++, sshDir.username);
    message.set(idx++, sshDir.password);
    message.set(idx++, sshDir.privateKey);
    return idx;
  }

  @JSBody(params = "data", script = "return typeof data === 'number';")
  static native boolean isNumber(JSObject data);
}
