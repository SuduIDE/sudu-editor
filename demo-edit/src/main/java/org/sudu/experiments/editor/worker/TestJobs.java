package org.sudu.experiments.editor.worker;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface TestJobs {
  int numDemoThreads = 6;

  String withString = "withString";

  static void withString(String string, List<Object> result) {
    result.add("string job");
    result.add("job " + string + " complete");
  }

  String withChars = "withChars";

  static void withChars(char[] array, List<Object> result) {
    array[0] ^= array[array.length - 1];
    array[array.length - 1] ^= array[0];
    array[0] ^= array[array.length - 1];
    result.add("char[] array");
    result.add(array);
  }

  String withInts = "withInts";

  static void withInts(int[] array, List<Object> result) {
    array[0] ^= array[array.length - 1];
    array[array.length - 1] ^= array[0];
    array[0] ^= array[array.length - 1];
    result.add("int[] array");
    result.add(array);
  }

  String withBytes = "withBytes";

  static void withBytes(byte[] array, List<Object> result) {
    array[0] ^= array[array.length - 1];
    array[array.length - 1] ^= array[0];
    array[0] ^= array[array.length - 1];
    result.add("byte[] array");
    result.add(array);
  }

  String fibonacci = "fibonacci";

  static void fibonacci(int[] array, List<Object> result) {
    double t0 = (double)System.nanoTime() / 1000_000.;
    int f = fibonacci(array[0]);
    double t1 = (double)System.nanoTime() / 1000_000.;
    int dt = (int) (t1 - t0 + .5);
    result.add(new int[]{f, dt});
  }

  static int fibonacci(int arg) {
    return arg <= 2 ? 1 : fibonacci(arg - 1) + fibonacci(arg - 2);
  }

  String asyncWithFile = "asyncWithFile";

  static void asyncWithFile(FileHandle file, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    list.add(file.toString());
    list.add(file);
    file.readAsBytes(
        bytes -> sendFileResult(result, list, bytes),
        error -> sendFileResult(result, list, utf(error)));
  }

  String asyncWithDir = "asyncWithDir";

  static void asyncWithDir(DirectoryHandle dir, Consumer<Object[]> result) {
    dir.read(new TestWalker(dir, result));
  }

  static byte[] utf(String error) {
    return error.getBytes(StandardCharsets.UTF_8);
  }

  static void sendFileResult(Consumer<Object[]> result, ArrayList<Object> list, byte[] bytes) {
    list.add(bytes);
    ArrayOp.sendArrayList(list, result);
  }
}
