package org.sudu.experiments.js;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;

import java.nio.CharBuffer;

public class JsTests {

  // this code crashes TeaVM, but works fine on java
  public static void charBufferTest() {
    CharBuffer cb = CharBuffer.allocate(0);
    cb.put("");
  }

  public static void testArray() {
    JSString string1 = JSString.valueOf("string1");
    JSString string2 = JSString.valueOf("string2");
    JSArray<JSObject> array = JsHelper.toJsArray(string1, string2);
    JsHelper.consoleInfo("JsHelper.isJsArray(array) = " + JsHelper.isJsArray(array));
    JsHelper.consoleInfo("JsHelper.isJsArray(string1) = " + JsHelper.isJsArray(string1));
    JsHelper.consoleInfo("JSString.isInstance(string1) = " + JSString.isInstance(string1));
  }

  public static void testPromise() {
    Promise<JSObject> promise1 = Promise.create(
        (postResult, postError) -> postResult.f(
            JSString.valueOf("promiseResult")));

    Promise<JSObject> promise2 = Promise.resolve(JSString.valueOf("promiseResult"));
    Promise<JSObject> promise3 = Promise.reject("rejected");

    JsHelper.consoleInfo("Promise.isThenable(promise1) = " + Promise.isThenable(promise1));
    JsHelper.consoleInfo("Promise.isThenable(promise2) = " + Promise.isThenable(promise2));
    JsHelper.consoleInfo("Promise.isThenable(promise3) = " + Promise.isThenable(promise3));
  }
}
