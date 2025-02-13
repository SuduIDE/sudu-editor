package org.sudu.experiments;

import org.sudu.experiments.diff.tests.CollectorFolderDiffTest;
import org.sudu.experiments.editor.worker.TestJobs;
import org.sudu.experiments.editor.worker.TestWalker;
import org.sudu.experiments.editor.worker.ThreadId;
import org.sudu.experiments.encoding.FileEncoding;
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
      JsFolderInput path1, JsFolderInput path2,
      boolean content, JsFunctions.Runnable onComplete);

  void testFileWrite(
      JsFileInput path, JSString content, JSString encoding,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  );

  void testFileReadWrite(
      JsFileInput pathFrom,
      JsFileInput pathToS,
      JsFileInput pathToJ,
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

  void testSshDir(JsSshInput sshPath, JsFunctions.Runnable onComplete);
  void testSshFile(JsSshInput sshPath, JsFunctions.Runnable onComplete);
  void testSshDirAsync(JsSshInput sshPath, JsFunctions.Runnable onComplete);
  void testSshFileAsync(JsSshInput sshPath, JsFunctions.Runnable onComplete);

  void testDeleteFile(JsFileInput path, JsFunctions.Runnable onComplete);
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
      JsFolderInput path1, JsFolderInput path2,
      boolean content,
      JsFunctions.Runnable onComplete
  ) {
    DirectoryHandle dir1 = JsFolderInput.directoryHandle(path1);
    DirectoryHandle dir2 = JsFolderInput.directoryHandle(path2);
    if (dir1 == null || dir2 == null) {
      if (dir1 == null) JsHelper.consoleError2("bad path1:", path1);
      if (dir2 == null) JsHelper.consoleError2("bad path2:", path2);
      onComplete.f();
      return;
    }

    JsHelper.consoleInfo("testDiff: ");
    JsHelper.consoleInfo("  left = " + dir1);
    JsHelper.consoleInfo("  right = " + dir2);
    JsHelper.consoleInfo("  content = ", JSBoolean.valueOf(content));
    JsTime jsTime = new JsTime();
    new CollectorFolderDiffTest(
        dir1, dir2, content, pool,
        jsTime, JsFunctions.toJava(onComplete)
    ).scan();
  }

  @Override
  public void testFileWrite(
      JsFileInput path, JSString content, JSString encoding,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  ) {
    var fh = JsFileInput.fileHandle(path, true);
    if (fh == null)
      JsHelper.consoleError2("bad path:", path);
    else fh.writeText(
        JsHelper.directJsToJava(content),
        encoding.stringValue(),
        onComplete::f,
        e -> onError.f(JSString.valueOf(e))
    );
    System.out.println("GbkEncoding.charToGbk[0x3000] = " +
        Integer.toHexString(GbkEncoding.Table.charToGbk[0x3000]));
  }

  @Override
  public void testFileReadWrite(
      JsFileInput pathFrom, JsFileInput pathToS, JsFileInput pathToJ,
      JsFunctions.Runnable onComplete, JsFunctions.Consumer<JSString> onError
  ) {
    var fhFrom = JsFileInput.fileHandle(pathFrom, true);
    var fhToS = JsFileInput.fileHandle(pathToS, false);
    var fhToJ = JsFileInput.fileHandle(pathToJ, false);
    int[] box = new int[] {1};
    Consumer<String> error = e -> {
      JsHelper.consoleError("testFileReadWrite error:" + e);
      if (--box[0] <= 0)
        onError.f(JSString.valueOf(e));
      else
        JsHelper.consoleError("testFileReadWrite error:" + e);
    };
    Runnable onCompleteJ = () -> {
      if (--box[0] <= 0)
        onComplete.f();
    };
    FileHandle.readTextFile(
        fhFrom, (text, encoding) -> {
          box[0] = 2;
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
    byte[] data1 = jsBuffer.asArray();
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
          if (bytes[0] != 55 || bytes[999] != 77)
            throw new RuntimeException("testNodeBuffer failed");
          JsHelper.consoleInfo2("data1.ArrayBuffer",
              JsMemoryAccess.bufferView(data1).getBuffer());
          onComplete.f();
        }, TestJobs.withBytes, data1
    );
  }

  @Override
  public void testSshDir(JsSshInput sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsSshInput.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshDir " + ThreadId.id);
    System.out.println("JsFileInputSsh.isInstance(sshPath) = "
        + instance);
    JSString path = sshPath.getPath();
    JsSshCredentials ssh = sshPath.getSsh();
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
  public void testSshFile(JsSshInput sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsSshInput.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshFile " + ThreadId.id);
    System.out.println("JsFileInputSsh.isInstance(sshPath) = "
        + instance);
    JSString path = sshPath.getPath();
    JsSshCredentials ssh = sshPath.getSsh();
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
  public void testSshDirAsync(JsSshInput sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsSshInput.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshDirAsync: " + ThreadId.id);
    if (instance) {
      JSString path = sshPath.getPath();
      JsSshCredentials ssh = sshPath.getSsh();
      var dir = new SshDirectoryHandle(path, ssh);
      pool.sendToWorker(objects -> {
        JsHelper.consoleInfo("DiffTestApi.testSshDirAsync complete, " +
            "response l =  " + objects.length);
        onComplete.f();
      }, TestJobs.asyncWithDir, dir);
    }
  }

  @Override
  public void testSshFileAsync(JsSshInput sshPath, JsFunctions.Runnable onComplete) {
    boolean instance = JsSshInput.isInstance(sshPath);
    System.out.println("DiffTestApi.testSshFileAsync " + ThreadId.id);
    if (instance) {
      JSString path = sshPath.getPath();
      JsSshCredentials ssh = sshPath.getSsh();
      var file = new SshFileHandle(path, ssh);
      pool.sendToWorker(objects -> {
        JsHelper.consoleInfo("DiffTestApi.testSshFileAsync complete, " +
            "response l =  " + objects.length);
        JsHelper.consoleInfo(" [0] = " + objects[0]);
        JsHelper.consoleInfo(" [1] = " + objects[1]);
        byte[] bytes = array(objects, 2).bytes();
        JsHelper.consoleInfo(" bytes.length = " + bytes.length);
        JsHelper.consoleInfo(" text: " + shortText(FileEncoding.decodeText(bytes)));
        onComplete.f();
      }, TestJobs.asyncWithFile, file);
    }
  }

  @Override
  public void testDeleteFile(JsFileInput path, JsFunctions.Runnable onComplete) {
    var fh = JsFileInput.fileHandle(path, true);
    if (fh == null) {
      JsHelper.consoleError2("bad path:", path);
    } else {
      fh.remove(onComplete::f, e -> {
        JsHelper.consoleError("fire remove error: " + e);
        onComplete.f();
      });
    }
  }

  static String shortText(String s) {
    String sh = s.length() > 80 ? s.substring(0, 80) : s;
    return sh.replaceAll("\r", "").replaceAll("\n", " ");
  }
}
