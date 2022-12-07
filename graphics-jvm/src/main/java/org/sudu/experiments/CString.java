package org.sudu.experiments;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CString {
  public static char[] toChar16CString(String x) {
    char[] charArray = x.toCharArray();
    return Arrays.copyOf(charArray, charArray.length + 1);
  }

  public static byte[] toUtf8CString(String x) {
    byte[] bytes = x.getBytes(StandardCharsets.UTF_8);
    return Arrays.copyOf(bytes, bytes.length + 1);
  }

  public static char[] emptyCString() {
    return new char[1];
  }

  public static byte[] toAsciiCString(String x) {
    byte[] bytes = new byte[x.length() + 1];
    for (int i = 0, n = x.length(); i < n; i++) {
      bytes[i] = (byte) x.charAt(i);
    }
    return bytes;
  }

  public static native int strlen(long address);
  public static native int strlenChar16t(long address);

  public static native void setByteArrayRegion(byte[] dst, int start, int len, long src);
  public static native void setCharArrayRegion(char[] dst, int start, int len, long src);

  public static native void getByteArrayRegion(byte[] dst, int start, int len, long receiver);
  public static native void getCharArrayRegion(char[] dst, int start, int len, long receiver);

  public static native long operatorNew(long size);
  public static native void operatorDelete(long ptr);

  public static String fromNativeString(long address) {
    int length = strlen(address);
    byte[] data = new byte[length];
    setByteArrayRegion(data, 0, length, address);
    return new String(data, StandardCharsets.UTF_8);
  }

  public static String fromNativeStringNullable(long address) {
    return address != 0 ? fromNativeString(address) : null;
  }

  public static String fromNativeString16(long address) {
    int length = strlenChar16t(address);
    char[] data = new char[length];
    setCharArrayRegion(data, 0, length, address);
    return new String(data);
  }

  static native void getSetPrimitiveArrayCriticalTest(int[] array, int value);
  static native void setIntArrayRegionTest(int[] array, int value);
}
