package org.sudu.experiments;

import org.sudu.experiments.diff.tests.CollectorFolderDiffTest;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.encoding.GbkEncoding;
import org.sudu.experiments.encoding.TextDecoder;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.Fs;
import org.sudu.experiments.js.node.NodeDirectoryHandle;
import org.sudu.experiments.js.node.NodeFileHandle;
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
      JSString path, JSString content, JSString encoding,
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

  void testGbkEncoder();
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
      JSString path, JSString content, JSString encoding,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  ) {
    var fh = new NodeFileHandle(path);
    fh.writeText(
        JsHelper.directJsToJava(content),
        encoding.stringValue(),
        onComplete::f,
        e -> onError.f(JSString.valueOf(e))
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

  @Override
  public void testGbkEncoder() {
    testAllGbk();
    testGlyph((byte) 0xA1, (byte) 0xA1);
  }

  static void testAllGbk() {
    System.out.println("DiffTestApi.testAllGbk");
    GbkEncoding.dump();
  }

  static void testGlyph(byte ... glyph) {
    System.out.println("testGlyph: " + Integer.toHexString(glyph[0] & 0xFF) +
        " " + Integer.toHexString(glyph[1] & 0xFF) );
    String string = TextDecoder.decodeGbk(glyph);
    System.out.println("  string.charAt(0) = " + Integer.toHexString(string.charAt(0)));
    byte[] bA1A1en = GbkEncoding.encode(string);
    System.out.println("  Encoded: " + Integer.toHexString(bA1A1en[0] & 0xFF) +
        " " + Integer.toHexString(bA1A1en[1] & 0xFF) );
  }
}
