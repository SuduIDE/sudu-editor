package org.sudu.experiments;

import org.sudu.experiments.diff.tests.CollectorFolderDiffTest;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.js.JsFunctions;
import org.sudu.experiments.js.JsHelper;
import org.sudu.experiments.js.Promise;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

import static org.sudu.experiments.editor.worker.ArgsCast.array;

interface JsDiffTestApi extends JSObject {
  Promise<JSString> testFib(int n);

  void testFS(JSString path, JsFunctions.Runnable onComplete);

  void testDiff(
      JSString path1, JSString path2,
      boolean content, JsFunctions.Runnable onComplete);

  void testFileWrite(
      JSString path, JSString content,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  );

  void testFileCopy(
      JSString src, JSString dest,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  );

  void testDirCopy(
      JSString src, JSString dest,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  );
}

public class DiffTestApi implements JsDiffTestApi {

  private final NodeWorkersPool pool;

  public DiffTestApi(NodeWorkersPool pool) {
    this.pool = pool;
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
  public void testFS(JSString path, JsFunctions.Runnable onComplete) {
    FsTest.fsTest(path, onComplete);
  }

  @Override
  public void testDiff(
      JSString path1, JSString path2,
      boolean content,
      JsFunctions.Runnable onComplete
  ) {
    if (DiffEngine.notDir(path1) || DiffEngine.notDir(path2)) {
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
        jsTime, JsFunctions.toJava(onComplete)
    ).scan();
  }

  @Override
  public void testFileWrite(
      JSString path, JSString content,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  ) {
    Fs.fs().writeFile(
        path, content, JSString.valueOf("utf-8"),
        error -> {
          if (error == null) {
            onComplete.f();
          } else {
            onError.f(JsHelper.jsToString(error));
          }
        }
    );
  }

  @Override
  public void testFileCopy(
      JSString src, JSString dest,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  ) {
    Fs.fs().copyFile(
        src, dest, 0,
        error -> {
          if (error == null) {
            onComplete.f();
          } else {
            onError.f(JsHelper.jsToString(error));
          }
        }
    );
  }

  @Override
  public void testDirCopy(
      JSString src, JSString dest,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  ) {
    Fs.fs().cp(
        src, dest, Fs.cpOptions(true, true),
        error -> {
          if (error == null) {
            onComplete.f();
          } else {
            onError.f(JsHelper.jsToString(error));
          }
        }
    );
  }
}
