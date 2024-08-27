package org.sudu.experiments;

import org.sudu.experiments.diff.folder.RemoteFolderDiffModel;
import org.sudu.experiments.diff.tests.CollectorFolderDiffTest;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.update.DiffModelChannelUpdater;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Uint16Array;

import java.util.Arrays;

import static org.sudu.experiments.editor.worker.ArgsCast.array;

public class DiffEngine implements DiffEngineJs {
  final NodeWorkersPool pool;

  DiffEngine(JsArray<NodeWorker> worker) {
    pool = new NodeWorkersPool(worker);
  }

  @Override
  public void dispose() {
    pool.terminateAll();
  }

  @Override
  public Promise<JSString> testFib(int n) {
    return Promise.create((postResult, postError) -> {
      pool.sendToWorker(
          result -> {
            int[] intResult = array(result, 0).ints();
            postResult.f(JSString.valueOf(
                "r: " + intResult[0] + ", time: " + intResult[1]
            ));
          }, TestJobs.fibonacci, new int[]{n}
      );
    });
  }

  @Override
  public JsDisposable startFolderDiff(JSString leftPath, JSString rightPath, Channel channel) {
    JsHelper.consoleInfo("Starting folder diff ");
    boolean scanFileContent = true;

    if (notDir(leftPath))
      throw new IllegalArgumentException("Left path " + leftPath.stringValue() + " should be directory");
    if (notDir(rightPath))
      throw new IllegalArgumentException("Right path " + rightPath.stringValue() + " should be directory");

    JsHelper.consoleInfo("DiffEngine LeftPath: ", leftPath);
    JsHelper.consoleInfo("DiffEngine RightPath: ", rightPath);

    DirectoryHandle leftHandle = new NodeDirectoryHandle(leftPath);
    DirectoryHandle rightHandle = new NodeDirectoryHandle(rightPath);

    RemoteFolderDiffModel root = new RemoteFolderDiffModel(null, "");

    DiffModelChannelUpdater updater = new DiffModelChannelUpdater(
        root,
        leftHandle, rightHandle,
        scanFileContent,
        pool, channel
    );
    updater.beginCompare();

    return JsDisposable.of(updater);
  }

  /*
    NodeFileHandle fh = new NodeFileHandle(leftPath);
    NodeFileHandle fh = new NodeFileHandle(rightPath);

    JsHelper.consoleInfo("Starting folder diff ");
    JsHelper.consoleInfo("\t leftPath ", leftPath);
    JsHelper.consoleInfo("\t rightPath ", rightPath);
    channel.setOnMessage(
        m -> {
          JsHelper.consoleInfo("channel onMessage ", m);
          getArray(m);
        }
    );
    JsArray<JSObject> array = JsArray.create();

    {
      String string = "sss";
      int[] data = new int[10];
      char[] dataChar = string.toCharArray();


      0 array.push(JSNumber.valueOf(20));
      1 array.push(JSString.valueOf(string));
      2 array.push(JsMemoryAccess.bufferView(data));
      3 array.push(JsMemoryAccess.bufferView(dataChar));
   }

   channel.sendMessage(array);

   new DiffModelBuilder(null, pool, true);

   getArray(array);
   JS -> [Mp.alg1] -> Workers -> [WorkerJob] -> [Mp.alg1] -> JS
   */

  void getArray(JsArray<JSObject> array) {
    int i20 = array.get(0).<JSNumber>cast().intValue();
    String sss = array.get(1).<JSString>cast().stringValue();

    int[] intArray = JsMemoryAccess.toJavaArray(
        array.get(2).<Int32Array>cast());
    char[] chars = JsMemoryAccess.toJavaArray(
        array.get(3).<Uint16Array>cast());

    JsHelper.consoleInfo("i = " + i20);
    JsHelper.consoleInfo("sss = " + sss);
    JsHelper.consoleInfo("intArray = " + Arrays.toString(intArray));
    JsHelper.consoleInfo("charArray = " + Arrays.toString(chars));
  }

  @Override
  public void testFS(JSString path, JsFunctions.Runnable onComplete) {
    FsTest.fsTest(path, onComplete);
  }

  @Override
  public void testDiff(
      JSString path1, JSString path2,
      boolean content,
      JsFunctions.Runnable onComplete
  ) {
    if (notDir(path1) || notDir(path2)) {
      onComplete.f();
      return;
    }
    NodeDirectoryHandle dir1 = new NodeDirectoryHandle(path1);
    NodeDirectoryHandle dir2 = new NodeDirectoryHandle(path2);

    JsHelper.consoleInfo("testDiff: ");
    JsHelper.consoleInfo("  path1 = ", path1);
    JsHelper.consoleInfo("  path2 = ", path2);
    JsHelper.consoleInfo("  content = ", JSBoolean.valueOf(content));
    JsTime jsTime = new JsTime();
    new CollectorFolderDiffTest(
        dir1, dir2, content, pool,
        jsTime, JsFunctions.wrap(onComplete)
    ).scan();
  }

  static boolean notDir(JSString path) {
    if (!isDir(path)) {
      JsHelper.consoleError("path is not a directory ", path);
      return true;
    }
    return false;
  }

  public static boolean isDir(JSString path) {
    return Fs.isDirectory(path);
  }

}
