package org.sudu.experiments.js;

import org.sudu.experiments.worker.WorkerExecutor;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

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
    JsArray<JSObject> message = JsArray.create(args.length + 2);
    message.set(0, JSNumber.valueOf(id));
    message.set(1, JSString.valueOf(method));
    transferAndSend(worker, args, message, 2);
  }

  static void execute(WorkerExecutor executor, JsArrayReader<JSObject> array) {
    if (array.getLength() >= 2) {
      JSObject taskId = array.get(0);
      String method = array.get(1).<JSString>cast().stringValue();
      executor.execute(method, toJava(array, 2),
          results -> sendResults(WorkerContext.self(), results, taskId));
    } else throw new IllegalArgumentException();
  }

  static void dispatchResult(Function<Integer, Consumer<Object[]>> handlers, JsArrayReader<JSObject> array) {
    if (array.getLength() >= 1) {
      int taskId = array.get(0).<JSNumber>cast().intValue();
      Consumer<Object[]> handler = handlers.apply(taskId);
      handler.accept(toJava(array, 1));
    } else throw new IllegalArgumentException();
  }

  private static void sendResults(WorkerContext context, Object[] result, JSObject taskId) {
    JsArray<JSObject> message = JsArray.create(result.length + 1);
    message.set(0, taskId);
    transferAndSend(context, result, message, 1);
  }

  private static void transferAndSend(
      WorkerContext context, Object[] args,
      JsArray<JSObject> message, int start
  ) {
    JsArray<JSObject> transfer = JsArray.create();
    for (Object arg : args) {
      int end = bridgeToJs(arg, message, start);
      JSObject value = message.get(start);
      start = end;
      if (isArrayBuffer(value)) transfer.push(value);
    }
    context.postMessage(message, transfer);
  }

  static Object[] toJava(JsArrayReader<JSObject> array, int shift) {
    int arrayLength = array.getLength();
    Object[] args = new Object[arrayLength - shift];
    int ptr = 0;
    for (int i = shift; i < arrayLength;) {
      i = bridgeToJava(array, i, args, ptr++);
    }
    return ptr == args.length ? args : Arrays.copyOf(args, ptr);
  }

  // JS <-> Java bridges:
  //   JSString <-> String
  //   char[], byte[], int[] <-> ArrayBuffer <-> ArrayView
  static int bridgeToJava(
      JsArrayReader<JSObject> array,
      int arrayIndex, Object[] r, int idx
  ) {
    JSObject jsObject = array.get(arrayIndex++);
    if (JSString.isInstance(jsObject)) {
      r[idx] = jsObject.<JSString>cast().stringValue();
    } else if (isArrayBuffer(jsObject)) {
      r[idx] = new JsArrayView(jsObject.cast());
    } else if (isFile(jsObject)) {
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

  static int bridgeToJs(Object javaObject, JsArray<JSObject> message, int idx) {
    if (javaObject instanceof String javaString) {
      message.set(idx++, JSString.valueOf(javaString));
    } else if (javaObject instanceof byte[] byteArray) {
      message.set(idx++, JsMemoryAccess.bufferView(byteArray).getBuffer());
    } else if (javaObject instanceof char[] charArray) {
      message.set(idx++, JsMemoryAccess.bufferView(charArray).getBuffer());
    } else if (javaObject instanceof int[] intArray) {
      message.set(idx++, JsMemoryAccess.bufferView(intArray).getBuffer());
    } else if (javaObject instanceof JsFileHandle jsFile) {
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

  @JSBody(params = "data", script = "return data instanceof Array;")
  static native boolean isArray(JSObject data);

  @JSBody(params = "data", script = "return data instanceof ArrayBuffer;")
  static native boolean isArrayBuffer(JSObject data);

  @JSBody(params = "data", script = "return data instanceof File;")
  static native boolean isFile(JSObject data);

  @JSBody(params = "data", script = "return data instanceof FileSystemFileHandle;")
  static native boolean isFileSystemFileHandle(JSObject data);

  @JSBody(params = "data", script = "return data instanceof FileSystemDirectoryHandle;")
  static native boolean isFileSystemDirectoryHandle(JSObject data);
}
