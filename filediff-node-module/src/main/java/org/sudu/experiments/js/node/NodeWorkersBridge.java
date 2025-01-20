package org.sudu.experiments.js.node;

import org.sudu.experiments.JsFileInputSsh;
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
      message.set(idx++, sshDir.credentials.getHost());
      message.set(idx++, sshDir.credentials.getPort());
      message.set(idx++, sshDir.credentials.getUsername());
      message.set(idx++, sshDir.credentials.getPassword());
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
          JSString host = array.get(arrayIndex++).cast();
          JSString port = array.get(arrayIndex++).cast();
          JSString user = array.get(arrayIndex++).cast();
          JSString password = array.get(arrayIndex++).cast();
          var ssh = JsFileInputSsh.Helper.create(host, port, user, password);
          r[idx] = new SshDirectoryHandle(jsPath, ssh);
        }
      }
    }
    return arrayIndex;
  }

  @JSBody(params = "data", script = "return typeof data === 'number';")
  static native boolean isNumber(JSObject data);
}
