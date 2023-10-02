package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.Scene0;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.editor.CtrlO;
import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.sudu.experiments.editor.worker.EditorWorker.array;
import static org.sudu.experiments.editor.worker.EditorWorker.string;

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

    api.input.onKeyPress.add(new CtrlO(api, this::openDirectory, this::openFile));
  }

  private void openFile(FileHandle fileHandle) {
    api.window.sendToWorker(this::bytesResult, TestJobs.asyncWithFile, fileHandle);
  }

  private void openDirectory(FileHandle fileHandle) {
    System.err.println("todo: add directory worker test " + fileHandle);
    throw new UnsupportedOperationException();
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

    String asyncWithFile = "asyncWithFile";

    static void asyncWithFile(FileHandle file, Consumer<Object[]> result) {
      ArrayList<Object> list = new ArrayList<>();
      list.add("file " + file.getName());
      file.readAsBytes(
          bytes -> sendFileResult(result, list, bytes),
          error -> sendFileResult(result, list, utf(error)));
    }

    static byte[] utf(String error) {
      return error.getBytes(StandardCharsets.UTF_8);
    }

    static void sendFileResult(Consumer<Object[]> result, ArrayList<Object> list, byte[] bytes) {
      list.add(bytes);
      ArrayOp.sendArrayList(list, result);
    }
  }
}
