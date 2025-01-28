package org.sudu.experiments;

import org.sudu.experiments.diff.tests.CollectorFolderDiffTest;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.editor.worker.TestWalker;
import org.sudu.experiments.editor.worker.ThreadId;
import org.sudu.experiments.encoding.GbkEncoding;
import org.sudu.experiments.js.*;
import org.sudu.experiments.js.node.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSString;

import java.util.Arrays;
import java.util.function.Consumer;

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

  void testFileReadWrite(
      JSString pathFrom,
      JSString pathToS,
      JSString pathToJ,
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
  void testNodeBuffer(JsFunctions.Runnable onComplete);

  void testSshDir(JSObject sshPath, JsFunctions.Runnable onComplete);
  void testSshFile(JSObject sshPath, JsFunctions.Runnable onComplete);
  void testSshDirAsync(JSObject sshPath, JsFunctions.Runnable onComplete);
  void testSshFileAsync(JSObject sshPath, JsFunctions.Runnable onComplete);
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
    System.out.println("GbkEncoding.charToGbk[0x3000] = " +
        Integer.toHexString(GbkEncoding.charToGbk[0x3000]));
  }

  @Override
  public void testFileReadWrite(
      JSString pathFrom, JSString pathToS, JSString pathToJ,
      JsFunctions.Runnable onComplete, JsFunctions.Consumer<JSString> onError
  ) {
    var fhFrom = new NodeFileHandle(pathFrom);
    var fhToS = new NodeFileHandle(pathToS);
    var fhToJ = new NodeFileHandle(pathToJ);
    Consumer<String> error = e -> onError.f(JSString.valueOf(e));
    int[] box = new int[1];
    Runnable onCompleteJ = () -> {
      if (++box[0] == 2) onComplete.f();
    };
    FileHandle.readTextFile(
        fhFrom, (text, encoding) -> {
          fhToS.writeText(text, encoding, onCompleteJ, error);
          JSString jsString = JSString.valueOf(text);
          var jsAsObj = JsHelper.directJsToJava(jsString);
          fhToJ.writeText(jsAsObj, encoding, onCompleteJ, error);
        }, error);
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
    System.out.println("DiffTestApi.testAllGbk");
    GbkEncodingTestHelper.dump();
    GbkEncodingTestHelper.testGlyph((byte) 0xA1, (byte) 0xA1);
  }

  @Override
  public void testNodeBuffer(JsFunctions.Runnable onComplete) {
    byte[] data0 = new byte[1000];
    JsBuffer jsBuffer = JsBuffer.from(data0);
    byte[] data1 = JsMemoryAccess.toJavaArray(jsBuffer);
    data1[0] = 77;
    data1[999] = 55;
    data1[10] = 100;
    data1[11] = (byte) 200;
    JsHelper.consoleInfo("data1[0] = " + data1[0]);
    JsHelper.consoleInfo("data1[999] = " + data1[999]);
    pool.sendToWorker(
        r -> {
          byte[] bytes = array(r, 1).bytes();
          JsHelper.consoleInfo("bytes[0] = " + bytes[0]);
          JsHelper.consoleInfo("bytes[999] = " + bytes[999]);
          JsHelper.consoleInfo("bytes[10] = " + bytes[10]);
          JsHelper.consoleInfo("bytes[11] = " + bytes[11]);
          JsHelper.consoleInfo2("data1.ArrayBuffer",
              JsMemoryAccess.bufferView(data1).getBuffer());
          onComplete.f();
        }, TestJobs.withBytes, data1
    );
  }

  @Override
  public void testSshDir(JSObject sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsFileInputSsh.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshDir " + ThreadId.id);
    System.out.println("JsFileInputSsh.isInstance(sshPath) = "
        + instance);
    JSString path = JsFileInputSsh.getPath(sshPath);
    JaSshCredentials ssh = JsFileInputSsh.getSsh(sshPath);
    JsHelper.consoleInfo2("path", path);
    JsHelper.consoleInfo2("ssh", ssh);
    if (instance && path != null) {
      var dir = new SshDirectoryHandle(path, ssh);
      dir.read(new TestWalker(dir,
          r -> {
            JsHelper.consoleInfo("testWalkerHandler:" + Arrays.toString(r));
            onComplete.f();
          }));
    }
  }

  @Override
  public void testSshFile(JSObject sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsFileInputSsh.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshFile " + ThreadId.id);
    System.out.println("JsFileInputSsh.isInstance(sshPath) = "
        + instance);
    JSString path = JsFileInputSsh.getPath(sshPath);
    JaSshCredentials ssh = JsFileInputSsh.getSsh(sshPath);
    JsHelper.consoleInfo2("path", path);
    JsHelper.consoleInfo2("ssh", ssh);
    if (instance && path != null) {
      var file = new SshFileHandle(path, ssh);
      file.getSize(s -> {
        JsHelper.consoleInfo("file.getSize: " + s);
        onComplete.f();
      });
    } else {
      onComplete.f();
    }
  }

  @Override
  public void testSshDirAsync(JSObject sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsFileInputSsh.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshDirAsync: " + ThreadId.id);
    if (instance) {
      JSString path = JsFileInputSsh.getPath(sshPath);
      JaSshCredentials ssh = JsFileInputSsh.getSsh(sshPath);
      var dir = new SshDirectoryHandle(path, ssh);
      pool.sendToWorker(objects -> {
        JsHelper.consoleInfo("DiffTestApi.testSshDirAsync complete, " +
            "response l =  " + objects.length);
        onComplete.f();
      }, TestJobs.asyncWithDir, dir);
    }
  }

  @Override
  public void testSshFileAsync(JSObject sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsFileInputSsh.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshFileAsync " + ThreadId.id);
    if (instance) {
      JSString path = JsFileInputSsh.getPath(sshPath);
      JaSshCredentials ssh = JsFileInputSsh.getSsh(sshPath);
      var file = new SshDirectoryHandle(path, ssh);
      pool.sendToWorker(objects -> {
        JsHelper.consoleInfo("DiffTestApi.testSshFileAsync complete, " +
            "response l =  " + objects.length);
        onComplete.f();
      }, TestJobs.asyncWithFile, file);
    }
  }
}
