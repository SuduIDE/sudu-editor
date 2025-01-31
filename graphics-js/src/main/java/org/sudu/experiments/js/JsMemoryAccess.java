package org.sudu.experiments.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSByRef;
import org.teavm.jso.typedarrays.*;

public class JsMemoryAccess {
  public final ArrayBuffer buffer;
  public final Int8Array i8;
  public final Uint16Array u16;
  public final Int32Array i32;
  public final Float32Array f32;
  public final Float64Array f64;

  public JsMemoryAccess(ArrayBuffer memory) {
    buffer = memory;
    i8 = Int8Array.create(memory);
    u16 = Uint16Array.create(memory);
    i32 = Int32Array.create(memory);
    f32 = Float32Array.create(memory);
    f64 = Float64Array.create(memory);
  }

  static byte[] toByteArray(ArrayBuffer jsArrayBuffer) {
    return toJavaArray(Int8Array.create(jsArrayBuffer));
  }

  public int byteLength() {
    return buffer.getByteLength();
  }

  public byte readByte(int address) {
    return i8.get(address);
  }

  // todo: refactor Uint16Array to return char directly
  public char readChar(int address) {
    return (char) u16.get(address >>> 1);
  }

  public int readInt(int address) {
    return i32.get(address >>> 2);
  }

  public float readFloat(int address) {
    return f32.get(address >>> 2);
  }

  public double readDouble(int address) {
    return f64.get(address >>> 3);
  }

  public byte[] byteArray(int address, int length) {
    return toJavaArray(Int8Array.create(buffer, address, length));
  }

  public char[] charArray(int address, int length) {
    return toJavaArray(Uint16Array.create(buffer, address, length));
  }

  public int[] intArray(int address, int length) {
    return toJavaArray(Int32Array.create(buffer, address, length));
  }

  public float[] floatArray(int address, int length) {
    return toJavaArray(Float32Array.create(buffer, address, length));
  }

  public double[] doubleArray(int address, int length) {
    return toJavaArray(Float64Array.create(buffer, address, length));
  }

  public int byteStrLen(int address) {
    int p = address;
    while (i8.get(p) != 0) p++;
    return p - address;
  }

  public int char16StrLen(int address) {
    int p0 = address >>> 1, p = p0;
    while (u16.get(p) != 0) p++;
    return p - p0;
  }

  public String readByteString(int address) {
    return new String(byteArray(address, byteStrLen(address)));
  }

  public String readChar16String(int address) {
    return new String(charArray(address, char16StrLen(address)));
  }

  @JSBody(params = "data", script = "return data;")
  @JSByRef
  public static native byte[] toJavaArray(Int8Array data);

  @JSBody(params = "data", script = "return data;")
  @JSByRef
  public static native byte[] toJavaArray(Uint8Array data);

  @JSByRef
  @JSBody(params = {"array"}, script = "return array;")
  public static native char[] toJavaArray(Uint16Array array);

  @JSByRef
  @JSBody(params = {"array"}, script = "return array;")
  public static native int[] toJavaArray(Int32Array array);

  @JSByRef
  @JSBody(params = {"array"}, script = "return array;")
  public static native float[] toJavaArray(Float32Array array);

  @JSByRef
  @JSBody(params = {"array"}, script = "return array;")
  public static native double[] toJavaArray(Float64Array array);

  @JSBody(params = {"data"}, script = "return data;")
  public static native Int8Array bufferView(@JSByRef byte[] data);

  @JSBody(params = {"data"}, script = "return data;")
  public static native Uint16Array bufferView(@JSByRef char[] data);

  @JSBody(params = {"data"}, script = "return data;")
  public static native Int32Array bufferView(@JSByRef int[] data);

  @JSBody(params = {"data"}, script = "return data;")
  public static native Float32Array bufferView(@JSByRef float[] data);

  public static Uint8Array uInt8View(byte[] data) {
    Int8Array v = bufferView(data);
    return Uint8Array.create(v.getBuffer(), v.getByteOffset(), v.getByteLength());
  }

  public static byte[] toByteArray(Uint8Array ua) {
    return toJavaArray(Int8Array.create(
        ua.getBuffer(), ua.getByteOffset(), ua.getByteLength()));
  }
}
