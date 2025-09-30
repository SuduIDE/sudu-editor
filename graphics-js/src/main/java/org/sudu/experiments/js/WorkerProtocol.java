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

  public interface PlatformBridge {
    int toJs(Object javaObject, JsArray<JSObject> message, int idx);
    int toJava(
        JSObject jsObject,
        JsArrayReader<JSObject> array, int arrayIndex,
        Object[] r, int idx);
  }

  static PlatformBridge bridge;

  public static void setBridge(PlatformBridge bridge) {
    // during some errors (like stack limit)
    // TextDecoder.Singleton.decoderUTF16 is not initialized properly
    // we can pre-init it here
    TextDecoder.Singleton.class.getClass();
    WorkerProtocol.bridge = bridge;
  }

  public static JSString started() { return JSString.valueOf("started"); }

  public static boolean isStarted(JSObject obj) {
    return JsHelper.strictEquals(obj, started());
  }

  public static JSString ping() { return JSString.valueOf("ping"); }

  public static boolean isPing(JSObject obj) {
    return JsHelper.strictEquals(obj, ping());
  }

  public static void sendPingToWorker(JsMessagePort0 worker) {
    worker.postMessage(ping());
  }

  public static void sendToWorker(JsMessagePort0 worker, int id, String method, Object[] args) {
    JsArray<JSObject> message = JsArray.create(args.length + 2);
    message.set(0, JSNumber.valueOf(id));
    message.set(1, JSString.valueOf(method));
    transferAndSend(worker, args, message, 2);
  }

  static void execute(WorkerExecutor executor, JsArrayReader<JSObject> array, JsMessagePort0 port) {
    if (array.getLength() >= 2) {
      JSObject taskId = array.get(0);
      String method = array.get(1).<JSString>cast().stringValue();
      executor.execute(method, toJava(array, 2),
          results -> sendResults(port, results, taskId));
    } else throw new IllegalArgumentException();
  }

  static void dispatchResult(Function<Integer, Consumer<Object[]>> handlers, JsArrayReader<JSObject> array) {
    if (array.getLength() >= 1) {
      int taskId = array.get(0).<JSNumber>cast().intValue();
      Consumer<Object[]> handler = handlers.apply(taskId);
//  todo: this error happens when async job send the result more than once
//        we may need to somehow detect and report such issues
//      if (handler == null) throw new IllegalArgumentException();
      handler.accept(toJava(array, 1));
    } else throw new IllegalArgumentException();
  }

  private static void sendResults(JsMessagePort0 context, Object[] result, JSObject taskId) {
    JsArray<JSObject> message = JsArray.create(result.length + 1);
    message.set(0, taskId);
    transferAndSend(context, result, message, 1);
  }

  private static void transferAndSend(
      JsMessagePort0 context, Object[] args,
      JsArray<JSObject> message, int start
  ) {
    JsArray<JSObject> transfer = JsArray.create();
    for (Object arg : args) {
      int end = bridgeToJs(arg, message, start);
      JSObject value = message.get(start);
      start = end;
      if (isArrayBufferView(value)) transfer.push(value);
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
    if (jsObject == null) {
      r[idx] = null;
    } else if (JSString.isInstance(jsObject)) {
      r[idx] = jsObject.<JSString>cast().stringValue();
    } else if (isArrayBufferView(jsObject)) {
      r[idx] = new JsArrayView(jsObject.cast());
    } else {
      arrayIndex = bridge.toJava(jsObject, array, arrayIndex, r, idx);
    }
    return arrayIndex;
  }

  static int bridgeToJs(Object javaObject, JsArray<JSObject> message, int idx) {
    if (javaObject == null) {
      message.set(idx++, null);
    } else if (javaObject instanceof String javaString) {
//      message.set(idx++, JSString.valueOf(javaString));
      message.set(idx++, JsHelper.fastToJs(javaString));
    } else if (javaObject instanceof byte[] byteArray) {
      message.set(idx++, JsMemoryAccess.bufferView(byteArray).getBuffer());
    } else if (javaObject instanceof char[] charArray) {
      message.set(idx++, JsMemoryAccess.bufferView(charArray).getBuffer());
    } else if (javaObject instanceof int[] intArray) {
      message.set(idx++, JsMemoryAccess.bufferView(intArray).getBuffer());
    } else if (javaObject instanceof double[] numbers) {
      message.set(idx++, JsMemoryAccess.bufferView(numbers).getBuffer());
    } else {
      idx = bridge.toJs(javaObject, message, idx);
    }
    return idx;
  }

  @JSBody(params = "data", script = "return data instanceof Array;")
  static native boolean isArray(JSObject data);

  @JSBody(params = "data", script = "return data instanceof ArrayBuffer;")
  static native boolean isArrayBufferView(JSObject data);

  public static void onWorkerMessage(WorkerExecutor executor, JSObject message, JsMessagePort0 port) {
    if (isPing(message)) {
//      JsHelper.consoleInfo("Worker: hello");
      port.postMessage(ping());
    } else if (isArray(message)) {
      execute(executor, message.cast(), port);
    } else throw new IllegalArgumentException();
  }

  public static void onEdtMessage(Function<Integer, Consumer<Object[]>> handlers, JSObject message) {
    if (isPing(message)) {
//      JsHelper.consoleInfo("App: hello from worker");
    } else if (isArray(message)) {
//      JsHelper.consoleInfo("App: message from worker = ", message);
      dispatchResult(handlers, message.cast());
    } else throw new IllegalArgumentException();
  }
}
