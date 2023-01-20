package org.sudu.experiments.worker;

import org.sudu.experiments.EventQueue;

import java.util.Objects;
import java.util.function.Consumer;

public class WorkerProxy {
  public static Runnable job(
      WorkerExecutor we, String method, Object[] args,
      Consumer<Object[]> handler, EventQueue edt
  ) {
    return () -> {
      Object[] cArgs = convertArgs(args);
      Object[] result = we.execute(method, cArgs);
      Object[] cResult = convertArgs(result);
      edt.execute(() -> handler.accept(cResult));
    };
  }

  static Object[] convertArgs(Object[] args) {
    Object[] newArgs = new Object[args.length];
    for (int i = 0; i < args.length; i++) {
      newArgs[i] = convertArg(args[i]);
    }
    return newArgs;
  }

  static Object convertArg(Object arg) {
    if (arg instanceof byte[] bytes) return new Array(bytes, null, null);
    if (arg instanceof char[] chars) return new Array(null, chars, null);
    if (arg instanceof int[] ints) return new Array(null, null, ints);
    return arg;
  }

  @SuppressWarnings("ClassCanBeRecord")
  static final class Array implements ArrayView {
    final byte[] bytes;
    final char[] chars;
    final int[] ints;

    Array(byte[] bytes, char[] chars, int[] ints) {
      this.bytes = bytes;
      this.chars = chars;
      this.ints = ints;
    }

    @Override
    public byte[] bytes() {
      return Objects.requireNonNull(bytes);
    }

    @Override
    public char[] chars() {
      return Objects.requireNonNull(chars);
    }

    @Override
    public int[] ints() {
      return Objects.requireNonNull(ints);
    }

    @Override
    public String toString() {
      return "Array view" +
          (bytes != null ? " bytes.length = ".concat(Integer.toString(bytes.length)) : "") +
          (chars != null ? " chars.length = ".concat(Integer.toString(chars.length)) : "") +
          (ints != null ? " ints.length = ".concat(Integer.toString(ints.length)) : "");
    }
  }
}
