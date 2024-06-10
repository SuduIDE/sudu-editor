package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

public class WebWorkersBridge implements WorkerProtocol.PlatformBridge {

  @Override
  public int toJs(Object javaObject, JsArray<JSObject> message, int idx) {
    if (javaObject instanceof JsFileHandle jsFile) {
      if (jsFile.fileHandle != null) {
        message.set(idx++, jsFile.fileHandle);
        message.set(idx++, JsDirectoryHandle.pathToJSString(jsFile.path));
      } else {
        message.set(idx++, jsFile.jsFile);
      }
    } else if (javaObject instanceof JsDirectoryHandle jsDir) {
      message.set(idx++, jsDir.fsDirectory);
      message.set(idx++, JsDirectoryHandle.pathToJSString(jsDir.path));
    } else throw new IllegalArgumentException(
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
    if (isFile(jsObject)) {
      r[idx] = JsFileHandle.fromWebkitRelativeFile(jsObject.cast());
    } else if (isFileSystemFileHandle(jsObject)) {
      String[] path = JsDirectoryHandle.toPath(array.get(arrayIndex++).cast());
      r[idx] = new JsFileHandle(jsObject.cast(), path);
    } else if (isFileSystemDirectoryHandle(jsObject)) {
      JSString jsPath = array.get(arrayIndex++).cast();
      r[idx] = new JsDirectoryHandle(jsObject.cast(), jsPath);
    }
    return arrayIndex;
  }

  @JSBody(params = "data", script = "return data instanceof File;")
  static native boolean isFile(JSObject data);

  @JSBody(params = "data", script = "return data instanceof FileSystemFileHandle;")
  static native boolean isFileSystemFileHandle(JSObject data);

  @JSBody(params = "data", script = "return data instanceof FileSystemDirectoryHandle;")
  static native boolean isFileSystemDirectoryHandle(JSObject data);

}
