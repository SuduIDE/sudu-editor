package org.sudu.experiments.js;

import org.sudu.experiments.worker.WorkerExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

import java.util.Map;
import java.util.function.Consumer;

public class WorkerProtocol {
  static JSString started() { return JSString.valueOf("started"); }

  static boolean isStarted(JSObject obj) {
    return JsHelper.strictEquals(obj, started());
  }

  public static JSString ping() { return JSString.valueOf("ping"); }

  public static boolean isPing(JSObject obj) {
    return JsHelper.strictEquals(obj, ping());
  }

  public static void sendPingToWorker(WorkerContext worker) {
    worker.postMessage(ping());
  }

  public static void sendToWorker(WorkerContext worker, int id, String method, Object[] args) {
    JSArray<JSObject> message = JSArray.create(args.length + 2);
    message.set(0, JSNumber.valueOf(id));
    message.set(1, JSString.valueOf(method));
    transferAndSend(worker, args, message, 2);
  }

  static void execute(WorkerExecutor executor, JSArrayReader<?> array) {
    if (array.getLength() >= 2) {
      JSObject taskId = array.get(0);
      String method = array.get(1).<JSString>cast().stringValue();
      executor.execute(method, toJava(array, 2),
          results -> sendResults(WorkerContext.self(), results, taskId));
    } else throw new IllegalArgumentException();
  }

  static void dispatchResult(Map<Integer, Consumer<Object[]>> handlers, JSArrayReader<JSObject> array) {
    if (array.getLength() >= 1) {
      int taskId = array.get(0).<JSNumber>cast().intValue();
      Consumer<Object[]> handler = handlers.remove(taskId);
      handler.accept(toJava(array, 1));
    } else throw new IllegalArgumentException();
  }

  private static void sendResults(WorkerContext context, Object[] result, JSObject taskId) {
    JSArray<JSObject> message = JSArray.create(result.length + 1);
    message.set(0, taskId);
    transferAndSend(context, result, message, 1);
  }

  private static void transferAndSend(WorkerContext context, Object[] args, JSArray<JSObject> message, int start) {
    JSArray<JSObject> transfer = JSArray.create();
    for (int i = 0; i < args.length; i++) {
      JSObject value = bridgeToJs(args[i]);
      message.set(i + start, value);
      if (isArrayBuffer(value)) transfer.push(value);
    }
    context.postMessage(message, transfer);
  }

  static Object[] toJava(JSArrayReader<?> array, int shift) {
    Object[] args = new Object[array.getLength() - shift];
    for (int i = 0; i < args.length; i++) {
      args[i] = bridgeToJava(array.get(i + shift));
    }
    return args;
  }

  // JS <-> Java bridges:
  //   JSString <-> String
  //   char[], byte[], int[] <-> ArrayBuffer <-> ArrayView
  static Object bridgeToJava(JSObject jsObject) {
    if (JSString.isInstance(jsObject)) {
      return jsObject.<JSString>cast().stringValue();
    }
    if (isArrayBuffer(jsObject)) {
      return new JsArrayView(jsObject.cast());
    }
    if (isFile(jsObject)) {
      return new JsFileHandle(null, jsObject.cast());
    }
    if (isFileSystemFileHandle(jsObject)) {
      return new JsFileHandle(jsObject.cast(), null);
    }
    return null;
  }

  static JSObject bridgeToJs(Object javaObject) {
    if (javaObject instanceof String javaString) {
      return JSString.valueOf(javaString);
    }
    if (javaObject instanceof byte[] byteArray) {
      return JsMemoryAccess.bufferView(byteArray).getBuffer();
    }
    if (javaObject instanceof char[] charArray) {
      return JsMemoryAccess.bufferView(charArray).getBuffer();
    }
    if (javaObject instanceof int[] intArray) {
      return JsMemoryAccess.bufferView(intArray).getBuffer();
    }
    if (javaObject instanceof JsFileHandle jsFileHandle) {
      return jsFileHandle.fileHandle != null
          ? jsFileHandle.fileHandle : jsFileHandle.jsFile;
    }
    throw new IllegalArgumentException();
  }

  @JSBody(params = "data", script = "return data instanceof Array;")
  static native boolean isArray(JSObject data);

  @JSBody(params = "data", script = "return data instanceof ArrayBuffer;")
  static native boolean isArrayBuffer(JSObject data);

  @JSBody(params = "data", script = "return data instanceof File;")
  static native boolean isFile(JSObject data);

  @JSBody(params = "data", script = "return data instanceof FileSystemFileHandle;")
  static native boolean isFileSystemFileHandle(JSObject data);
}
