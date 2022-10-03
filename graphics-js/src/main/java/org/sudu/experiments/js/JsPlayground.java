package org.sudu.experiments.js;

import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint8Array;

import java.util.Arrays;

public class JsPlayground {
  public static void jsoTagTest() {
    {
      ArrayBufferView byteArray = JsMemoryAccess.bufferView(new byte[]{1, 2, 3, 4, 5});
      System.out.println("byteArray tag = " + JsHelper.toStringTag(byteArray));

      JsHelper.consoleInfo("byteArray(", JsHelper.jsToStringTag(byteArray), "): ", byteArray);
    }

    {
      ArrayBufferView charArray = JsMemoryAccess.bufferView("String".toCharArray());
      System.out.println("charArray tag = " + JsHelper.toStringTag(charArray));

      JsHelper.consoleInfo("charArray(", JsHelper.jsToStringTag(charArray), "): ", charArray);
    }
  }

  public static void jsTestViewOfJavaArray() {
    byte[] jArrayCopyRef = new byte[10];
    byte[] jArrayViewRef = new byte[10];
    Int8Array arrayCopy = Int8Array.create(JsMemoryAccess.bufferView(jArrayCopyRef));
    Int8Array liveView = JsMemoryAccess.bufferView(jArrayViewRef);
    fill(arrayCopy);
    fill(liveView);

    System.out.println("jArray on copy = " + Arrays.toString(jArrayCopyRef));
    System.out.println("jArray on view1 = " + Arrays.toString(jArrayViewRef));

    Uint8Array liveViewUint = JsMemoryAccess.uInt8View(jArrayViewRef);

    for (int i = 0; i < 10; i++) liveViewUint.set(i, (short) (10 - i));

    System.out.println("jArray on view2 = " + Arrays.toString(jArrayViewRef));
  }

  private static void fill(Int8Array uArray) {
    for (int i = 0; i < 10; i++) {
      uArray.set(i, (byte) (i + 1));
    }
  }
}
