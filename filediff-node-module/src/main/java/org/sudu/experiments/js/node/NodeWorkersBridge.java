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
          arrayIndex += 4;
        }
        case 3 -> {
          JSString jsPath = array.get(arrayIndex++).cast();
          JsSftpClient.Attrs attrs = array.get(arrayIndex++).cast();
          var ssh = getSsh(array, arrayIndex);
          r[idx] = new SshFileHandle(jsPath, ssh, attrs);
          arrayIndex += 4;
        }
      }
    }
    return arrayIndex;
  }

  static JaSshCredentials getSsh(JsArrayReader<JSObject> array, int arrayIndex) {
    JSString host = array.get(arrayIndex).cast();
    JSString port = array.get(arrayIndex + 1).cast();
    JSString username = array.get(arrayIndex + 2).cast();
    JSString password = array.get(arrayIndex + 3).cast();
    return JsFileInputSsh.Helper.create(host, port, username, password);
  }

  static int putSsh(JsArray<JSObject> message, int idx, JaSshCredentials sshDir) {
    message.set(idx++, sshDir.getHost());
    message.set(idx++, sshDir.getPort());
    message.set(idx++, sshDir.getUsername());
    message.set(idx++, sshDir.getPassword());
    return idx;
  }

  @JSBody(params = "data", script = "return typeof data === 'number';")
  static native boolean isNumber(JSObject data);
}
