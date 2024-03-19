package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.Window;
import org.sudu.experiments.editor.CtrlO;
import org.sudu.experiments.editor.WindowScene;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.UiFont;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.sudu.experiments.editor.worker.EditorWorker.array;
import static org.sudu.experiments.editor.worker.EditorWorker.string;
import static org.sudu.experiments.editor.worker.EditorWorker.file;

@SuppressWarnings({"PrimitiveArrayArgumentToVarargsMethod"})
public class WorkerTest extends WindowScene {
  public WorkerTest(SceneApi api) {
    super(api);

    api.input.onContextMenu.add(this::onContextMenu);

    sendPrimitiveTasks(api.window);

    api.input.onKeyPress.add(new CtrlO(api, this::openFile));
  }

  private void sendPrimitiveTasks(Window window) {
    window.sendToWorker(this::stringResult,
        TestJobs.withString, "hello string");
    window.sendToWorker(this::charsResult,
        TestJobs.withChars, new char[]{ 1,2,3,4,5 });
    window.sendToWorker(this::bytesResult,
        TestJobs.withBytes, new byte[]{ 1,2,3,4,5 });
    window.sendToWorker(this::integersResult,
        TestJobs.withInts, new int[]{ 1,2,3,4,5 });
  }

  boolean onContextMenu(MouseEvent event) {
    windowManager.showPopup(
        Themes.darculaColorScheme(),
        new UiFont("Consolas", 25),
        event.position, menu());
    return true;
  }

  private Supplier<ToolbarItem[]> menu() {
    return ArrayOp.supplier(
        new ToolbarItem(() -> callFibonacci(37, 3), "fibonacci(37) x3"),
        new ToolbarItem(() -> callFibonacci(40, 3), "fibonacci(40) x3"),
        new ToolbarItem(() -> callFibonacci(42, 3), "fibonacci(42) x3"),
        new ToolbarItem(() -> callFibonacci(45, 3), "fibonacci(45) x3"),
        new ToolbarItem(() -> callFibonacci(45, 5), "fibonacci(45) x5")
    );
  }

  private void callFibonacci(int n, int times) {
    for (int i = 0; i < times; i++) {
      api.window.sendToWorker(r -> fibResult(r, n),
          TestJobs.fibonacci, new int[]{ n });
    }
  }

  private void fibResult(Object[] result, int n) {
    int[] intResult = array(result, 0).ints();
    System.out.println("fib(" + n + ") result = " + intResult[0]);
    System.out.println("fib(" + n + ") time = " + intResult[1]);
  }

  private void openFile(FileHandle fileHandle) {
    api.window.sendToWorker(
        WorkerTest::fileResult, TestJobs.asyncWithFile, fileHandle);
  }

  void stringResult(Object[] args) {
    System.out.println("WorkerTest: \n  got " + args[0]);
    System.out.println("  methodWithStringResult = " + string(args, 1));
  }

  void charsResult(Object[] args) {
    System.out.println("charsResult: \n  got " + args[0]);
    char[] chars = array(args, 1).chars();
    System.out.println("  methodWithCharsResult: " + args[1] +
        ", chars = " + Arrays.toString(chars));
  }

  void bytesResult(Object[] args) {
    System.out.println("bytesResult: \n  got " + args[0]);
    byte[] bytes = array(args, 1).bytes();
    System.out.println("  methodWithBytesResult: " + args[1] +
        ", bytes = " + Arrays.toString(bytes));
  }

  void integersResult(Object[] args) {
    System.out.println("integersResult: \n  got " + args[0]);
    int[] ints = array(args, 1).ints();
    System.out.println("  " + args[1] +
        ", integers = " + Arrays.toString(ints)
    );
  }

  public static void fileResult(Object[] args) {
    Object name = args[0];
    FileHandle file = file(args, 1);
    byte[] content = array(args, 2).bytes();

    printFileResult(name, file, content);
  }

  public static void printFileResult(Object name, FileHandle file, byte[] content) {
    System.out.println("fileResult" +
        ": \"" + name + "\", file " + file);

    System.out.println("  content: " + content.length
        + " bytes, hash = " + Arrays.hashCode(content));
  }
}
