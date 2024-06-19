package org.sudu.experiments;

import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.tests.FolderDiffTest;
import org.sudu.experiments.diff.tests.FolderScanTest;
import org.sudu.experiments.diff.update.UpdateDto;
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

import static org.sudu.experiments.editor.worker.EditorWorker.array;

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
  public void startFolderDiff(JSString leftPath, JSString rightPath, Channel channel) {
    JsHelper.consoleInfo("Starting folder diff ");

    if (DiffEngine.notDir(leftPath))
      throw new IllegalArgumentException(leftPath.stringValue() + " should be directory");
    if (DiffEngine.notDir(rightPath))
      throw new IllegalArgumentException(rightPath.stringValue() + " should be directory");

    DirectoryHandle leftHandle = new NodeDirectoryHandle(leftPath);
    DirectoryHandle rightHandle = new NodeDirectoryHandle(rightPath);

    FolderDiffModel leftModelRoot = new FolderDiffModel(null);
    FolderDiffModel rightModelRoot = new FolderDiffModel(null);

    channel.setOnMessage(
        jsResult -> {
          JsHelper.consoleInfo("Got update batch from channel");
          var ints = JsMemoryAccess.toJavaArray((Int32Array) jsResult.get(0).cast());
          Object[] result = new Object[jsResult.getLength()];
          result[0] = ints;
          for (int i = 1; i < result.length; i++) {
            result[i] = new NodeDirectoryHandle(jsResult.get(i).cast());
          }
          var updateDto = UpdateDto.fromInts(ints, result);
          leftModelRoot.update(updateDto.leftRoot);
          rightModelRoot.update(updateDto.rightRoot);

          if (leftModelRoot.compared && rightModelRoot.compared) {
            JsHelper.consoleInfo("Roots compared");
            if (leftModelRoot.diffType == DiffTypes.EDITED) JsHelper.consoleInfo("EDITED");
          }
        }
    );

    DiffModelChannelUpdater updater = new DiffModelChannelUpdater(
        leftModelRoot, rightModelRoot,
        leftHandle, rightHandle,
        pool, channel
    );
    updater.beginCompare();
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
  public void testFS2(JSString path1, JSString path2, JsFunctions.Runnable onComplete) {
    if (notDir(path1) || notDir(path2)) {
      onComplete.f();
      return;
    }

    JsHelper.consoleInfo("testFS2 path1 = ", path1);
    JsHelper.consoleInfo("testFS2 path2 = ", path2);

    NodeDirectoryHandle dir1 = new NodeDirectoryHandle(path1);
    NodeDirectoryHandle dir2 = new NodeDirectoryHandle(path2);

    new FolderScanTest(
        dir1, dir2, pool, onComplete::f
    ).scan();
  }

  @Override
  public void testDiff(
      JSString path1, JSString path2, boolean content,
      JsFunctions.Runnable onComplete
  ) {
    if (notDir(path1) || notDir(path2)) {
      onComplete.f();
      return;
    }
    NodeDirectoryHandle dir1 = new NodeDirectoryHandle(path1);
    NodeDirectoryHandle dir2 = new NodeDirectoryHandle(path2);

    JsHelper.consoleInfo("testDiff:", path1);
    JsHelper.consoleInfo("  path1 = ", path1);
    JsHelper.consoleInfo("  path2 = ", path2);
    JsHelper.consoleInfo("  content = ", JSBoolean.valueOf(content));
    JsTime jsTime = new JsTime();
    new FolderDiffTest(
        dir1, dir2, content, pool,
        jsTime, onComplete::f
    ).scan();
  }

  static boolean notDir(JSString path) {
    if (!Fs.isDirectory(path)) {
      JsHelper.consoleError("path is not a directory: ", path);
      return true;
    }
    return false;
  }
}
