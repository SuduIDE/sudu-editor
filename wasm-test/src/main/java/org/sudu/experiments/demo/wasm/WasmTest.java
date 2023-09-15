package org.sudu.experiments.demo.wasm;

import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.JsMemoryAccess;
import org.sudu.experiments.js.Promise;
import org.sudu.experiments.js.WebAssembly;
import org.sudu.experiments.math.Numbers;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;

import java.util.Arrays;

public class WasmTest {
  public static final String module = "test.wasm";
  public static final String DIV = "panelDiv";

  public interface Exports extends WebAssembly.MemoryContainer {
    int callToCpp1();
    float callToCpp2();
    int getC8String();
    int getC16String();
    int getCDoubleArray8();
    int getCFloatArray8();
    int getCIntArray8();
  }

  public interface Imports {
    @JSFunctor interface F1 extends JSObject { int f(int a, int b); }
    @JSFunctor interface F2 extends JSObject { float f(float a, float b); }
  }

  @JSBody(
      params = {"f1", "f2"},
      script = "return { env: { jsFunction1: f1, jsFunction2: f2 }};"
  )
  public static native JSObject wasmImportObject(Imports.F1 f1, Imports.F2 f2);

  static Promise<WebAssembly.ModuleWithInstance<Exports>> instantiate(ArrayBuffer b) {
    return WebAssembly.instantiate(b, wasmImportObject(
        WasmTest::jsToWasm1, WasmTest::jsToWasm2));
  }

  static void onLoad(WebAssembly.ModuleWithInstance<Exports> exportsModuleWithInstance) {
    Exports exports = exportsModuleWithInstance.instance().exports();
    JsMemoryAccess access = exports.memory().memoryAccess();

    addPreText("wasm memory size = " + access.byteLength());
    addPreText("callToCpp1 = " +  exports.callToCpp1());
    addPreText("callToCpp2 = " + exports.callToCpp2());

    addPreText("getC8String = " + access.readByteString(exports.getC8String()));
    addPreText("getC16String = " + access.readChar16String(exports.getC16String()));

    int[] intArray = access.intArray(exports.getCIntArray8(), 8);
    addPreText("int array: " + Arrays.toString(intArray));

    float[] floatArray = access.floatArray(exports.getCFloatArray8(), 8);
    addPreText("float array: " + Arrays.toString(floatArray));

    double[] doubleArray = access.doubleArray(exports.getCDoubleArray8(), 8);
    addPreText("double array: " + Arrays.toString(doubleArray));

    addPreText("c8String memory test " +
        testAsciiString(exports.getC8String(), "this is a C/C++ string", access));

    addPreText("c16String memory test " +
        testC16String(exports.getC16String(), "this is a C/C++ char16_t string 新年快乐", access));

    addPreText("int memory read test " +
        intArrayTest(intArray, access, exports.getCIntArray8(),
            11, 22, 33, 44, 55, 66, 77, 88));

    addPreText("float memory read test " +
        floatArrayTest(floatArray, access, exports.getCFloatArray8(),
            100, 111, 222, 333, 444, 555, 666, 777, 888));

    addPreText("double memory read test " +
        doubleArrayTest(doubleArray, access, exports.getCDoubleArray8(),
            1000, 1111, 2222, 3333, 4444, 5555, 6666, 7777, 8888));

  }

  static String floatArrayTest(float[] a, JsMemoryAccess access, int address, int m, int ... expected) {
    if (a.length != expected.length) return "fail: length differ";
    for (int i = 0, n = a.length; i < n; i++) {
      if (Numbers.iRnd(a[i] * m) != expected[i] ||
          access.readFloat(address + i * 4) != a[i]) {
        return "fail: not equal to expected";
      }
    }
    return "ok";
  }

  static String doubleArrayTest(double[] a, JsMemoryAccess access, int address, int m, int ... expected) {
    if (a.length != expected.length) return "fail: length differ";
    for (int i = 0, n = a.length; i < n; i++) {
      if (Numbers.iRnd(a[i] * m) != expected[i] ||
          access.readDouble(address + i * 8) != a[i]) {
        return "fail: not equal to expected";
      }
    }
    return "ok";
  }

  static String intArrayTest(int[] a, JsMemoryAccess access, int address, int ... expected) {
    if (a.length != expected.length) return "fail: length differ";
    for (int i = 0, n = a.length; i < n; i++) {
      if (a[i] != expected[i] || access.readInt(address + i * 4) != a[i]) {
        return "fail: not equal to expected";
      }
    }
    return "ok";
  }

  static String testAsciiString(int c8String, String expected, JsMemoryAccess access) {
    String byteString = access.readByteString(c8String);
    if (!expected.equals(byteString)) return "fail: not expected";
    for (int i = 0; i < byteString.length(); i++) {
      if (byteString.charAt(i) != access.readByte(c8String + i)) {
        return "fail";
      }
    }
    return  "ok";
  }

  private static String testC16String(int c16String, String expected, JsMemoryAccess access) {
    String wCharString = access.readChar16String(c16String);
    if (!expected.equals(wCharString)) return "fail: not expected";
    for (int i = 0; i < wCharString.length(); i++) {
      if (wCharString.charAt(i) != access.readChar(c16String + i * 2)) {
        return "fail";
      }
    }
    return "ok";
  }

  static int jsToWasm1(int a, int b) {
    addPreText("jsToWasm1: a = " + a + ", b = " + b);
    return a + b;
  }

  static float jsToWasm2(float a, float b) {
    addPreText("jsToWasm2: a = " + a + ", b = " + b);
    return a + b;
  }

  static void addPreText(String s) {
    JsHelper.addPreText(DIV, s);
  }
}
