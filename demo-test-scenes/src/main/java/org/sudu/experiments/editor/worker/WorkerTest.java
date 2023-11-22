package org.sudu.experiments.editor.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.Window;
import org.sudu.experiments.editor.CtrlO;
import org.sudu.experiments.editor.Scene1;
import org.sudu.experiments.editor.TestHelper;
import org.sudu.experiments.editor.ui.colors.Themes;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.ui.PopupMenu;
import org.sudu.experiments.ui.ToolbarItem;
import org.sudu.experiments.ui.UiFont;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.sudu.experiments.Const.emptyRunnable;
import static org.sudu.experiments.editor.worker.EditorWorker.array;
import static org.sudu.experiments.editor.worker.EditorWorker.string;

@SuppressWarnings({"PrimitiveArrayArgumentToVarargsMethod"})
public class WorkerTest extends Scene1 {

  private final PopupMenu popupMenu;

  public WorkerTest(SceneApi api) {
    super(api);

    popupMenu = new PopupMenu(uiContext);
    popupMenu.setTheme(Themes.darculaColorScheme(),
        new UiFont("Consolas", 25));
    api.input.onMouse.add(TestHelper.popupMouseListener(popupMenu));
    api.input.onContextMenu.add(this::onContextMenu);

    sendPrimitiveTasks(api.window);

    api.input.onKeyPress.add(new CtrlO(api, this::openDirectory, this::openFile));
  }

  private void sendPrimitiveTasks(Window window) {
    window.sendToWorker(this::stringResult,
        TestJobs.withString, "hello string");
    window.sendToWorker(this::charsResult,
        TestJobs.withChars, new char[]{ 1,2,3,4,5 });
    window.sendToWorker(this::bytesResult,
        TestJobs.withBytes, new byte[]{ 1,2,3,4,5 });
    window.sendToWorker(this::intsResult,
        TestJobs.withInts, new int[]{ 1,2,3,4,5 });
  }

  @Override
  public void dispose() {
    popupMenu.dispose();
  }

  @Override
  public void paint() {
    super.paint();
    popupMenu.paint();
  }

  boolean onContextMenu(MouseEvent event) {
    if (!popupMenu.isVisible()) {
      popupMenu.display(event.position, menu(), emptyRunnable);
    }
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
}