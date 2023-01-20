package org.sudu.experiments.demo.worker;

import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;

import java.util.Arrays;
import java.util.List;

import static org.sudu.experiments.demo.worker.EditorWorker.array;
import static org.sudu.experiments.demo.worker.EditorWorker.string;

@SuppressWarnings({"PrimitiveArrayArgumentToVarargsMethod"})
public class WorkerTest extends Scene0 {

  public WorkerTest(SceneApi api) {
    super(api);

    api.window.sendToWorker(this::stringResult,
        TestJobs.withString, "hello string");
    api.window.sendToWorker(this::charsResult,
        TestJobs.withChars, new char[]{ 1,2,3,4,5 });
    api.window.sendToWorker(this::bytesResult,
        TestJobs.withBytes, new byte[]{ 1,2,3,4,5 });
    api.window.sendToWorker(this::intsResult,
        TestJobs.withInts, new int[]{ 1,2,3,4,5 });
  }

  void stringResult(Object[] args) {
    System.out.println("WorkerTest: \n  got " + args[0]);
    System.out.println("  methodWithStringResult = " + string(args, 1));
  }

  void charsResult(Object[] args) {
    System.out.println("WorkerTest: \n  got " + args[0]);
    char[] chars = array(args, 1).chars();
    System.out.println("  methodWithCharsResult: " + args[1] +
        ", chars = " + Arrays.toString(chars));
  }

  void bytesResult(Object[] args) {
    System.out.println("WorkerTest: \n  got " + args[0]);
    byte[] bytes = array(args, 1).bytes();
    System.out.println("  methodWithBytesResult: " + args[1] +
        ", bytes = " + Arrays.toString(bytes));
  }

  void intsResult(Object[] args) {
    System.out.println("WorkerTest: \n  got " + args[0]);
    int[] ints = array(args, 1).ints();
    System.out.println("methodWithIntsResult: " + args[1] +
        ", ints = " + Arrays.toString(ints)
    );
  }

  interface TestJobs {
    String withString = "withString";

    static void withString(String string, List<Object> result) {
      result.add("string job");
      result.add("job " + string + " complete");
    }

    String withChars = "withChars";
    static void withChars(char[] array, List<Object> result) {
      array[0] ^= array[array.length-1];
      array[array.length-1] ^= array[0];
      array[0] ^= array[array.length-1];
      result.add("char[] array");
      result.add(array);
    }

    String withInts = "withInts";

    static void withInts(int[] array, List<Object> result) {
      array[0] ^= array[array.length-1];
      array[array.length-1] ^= array[0];
      array[0] ^= array[array.length-1];
      result.add("int[] array");
      result.add(array);
    }

    String withBytes = "withBytes";

    static void withBytes(byte[] array, List<Object> result) {
      array[0] ^= array[array.length-1];
      array[array.length-1] ^= array[0];
      array[0] ^= array[array.length-1];
      result.add("byte[] array");
      result.add(array);
    }
  }
}
