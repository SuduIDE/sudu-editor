package org.sudu.experiments;

import org.sudu.experiments.diff.tests.CollectorFolderDiffTest;
import org.sudu.experiments.editor.worker.FsWorkerJobs;
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
import java.util.function.IntConsumer;

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
      JsFileInput pathFrom, JsFileInput pathTo,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  );

  void testNodeFsCopyFile(
      JSString src, JSString dest,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  );

  void testNodeFsCopyDirectory(
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

  void testCopyFileToFolder(
      JsFileInput from, JsFolderInput toDir, JsFileInput toFile,
      JsFunctions.Runnable onComplete, JsFunctions.Consumer<JSString> onError);

  void testFileAppend(
      JsFileInput file, JSString str1, JSString str2,
      JsFunctions.Runnable onComplete, JsFunctions.Consumer<JSString> onError);

  void testMkDir(
      JsFolderInput dir, JSString name,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  );

  void testRemoveDir(
      JsFolderInput dir,
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
    var fh = JsFileInput.fileHandle(path, false);
    if (fh == null) {
      onError.f(JsHelper.concat("bad path:", path));
      JsHelper.consoleError2("bad path:", path);
    } else {
      FsWorkerJobs.fileWriteText(pool, fh,
          TextEncoder.toCharArray(content),
          encoding.stringValue(),
          onComplete::f,
          wrap(onError)
      );
    }
    System.out.println("GbkEncoding.charToGbk[0x3000] = " +
        Integer.toHexString(GbkEncoding.Table.charToGbk[0x3000]));
  }

  @Override
  public void testFileReadWrite(
      JsFileInput pathFrom, JsFileInput pathTo,
      JsFunctions.Runnable onComplete, JsFunctions.Consumer<JSString> onErrorJ
  ) {
    var fhFrom = JsFileInput.fileHandle(pathFrom, true);
    var fhTo = JsFileInput.fileHandle(pathTo, false);
    if (fhFrom == null || fhTo == null) {
      onErrorJ.f(JsHelper.concat("bad path:", pathFrom));
      return;
    }
    Consumer<String> onError = e -> {
      JsHelper.consoleError("testFileReadWrite error:" + e);
      onErrorJ.f(JSString.valueOf(e));
    };

    FsWorkerJobs.readTextFile(pool, fhFrom, (t, en) -> {
      JsHelper.consoleError("testFileReadWrite: readTextFile ok "
          + fhFrom + ", l = " + t.length + ", enc = " + en);
      FsWorkerJobs.fileWriteText(pool,
          fhTo, t, en,
          onComplete::f, onError);
    }, onError);
  }

  @Override
  public void testNodeFsCopyFile(
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
  public void testNodeFsCopyDirectory(
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
      FsWorkerJobs.removeFile(pool, fh, onComplete::f, e -> {
        JsHelper.consoleError("fire remove error: " + e);
        onComplete.f();
      });
    }
  }

  @Override
  public void testCopyFileToFolder(
      JsFileInput from, JsFolderInput toDir, JsFileInput toFile,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onErrorJs
  ) {
    var fhFrom = JsFileInput.fileHandle(from, true);
    var fhToDir = JsFolderInput.directoryHandle(toDir);
    var fhToFile = JsFileInput.fileHandle(toFile, false);
    if (fhFrom == null) {
      onErrorJs.f(JSString.valueOf("bad input file"));
      return;
    }
//    boolean canCopyTo = fhFrom.canCopyTo(fhToDir);
//    System.out.println("testCopyFileToFolder: fhFrom.canCopyTo(fhTo) = " + canCopyTo);

    int[] box = new int[1];
    Consumer<String> onError = e -> {
      String m = "FileHandle.copyTo(" + fhFrom + ") error: " + e;
      JSString msg = JSString.valueOf(m);
      JsHelper.consoleError("  testCopyFileToFolder.error:", msg);
      if (--box[0] <= 0)
        onErrorJs.f(msg);

    };
    Runnable onCompleteJ = () -> {
      if (--box[0] <= 0)
        onComplete.f();
    };

    if (false) {
      box[0] = 2;
      fhFrom.copyTo(fhToDir, onCompleteJ, onError);
      fhFrom.copyTo(fhToFile, onCompleteJ, onError);
    } else {
      box[0] = 2;

      IntConsumer onCopy = size -> {
        System.out.println(
            "testCopyFileToFolder: " + size + " bytes copied, from " + fhFrom);
        onCompleteJ.run();
      };

      System.out.println(
          "testCopyFileToFolder: call " + FsWorkerJobs.asyncCopyFile + "("
              + fhFrom + "," + fhToFile + ")");

      FsWorkerJobs.copyFile(
          pool, fhFrom, fhToFile,
          onCopy, onError);

      System.out.println(
          "testCopyFileToFolder: call " + FsWorkerJobs.asyncCopyFile + "("
              + fhFrom + "," + fhToDir + ")");

      FsWorkerJobs.copyFile(
          pool, fhFrom, fhToDir,
          onCopy, onError);
    }
  }

  public void testFileAppend(
      JsFileInput file, JSString str1, JSString str2,
      JsFunctions.Runnable onComplete, JsFunctions.Consumer<JSString> onError
  ) {
    var hFile = JsFileInput.fileHandle(file, false);
    Consumer<String> eh = wrap(onError);
    if (hFile == null) {
      eh.accept("bad input file");
      return;
    }
    var b1 = TextEncoder.toUtf8(str1);
    var b2 = TextEncoder.toUtf8(str2);

    Runnable verify = () -> hFile.readAsBytes(actual -> {
      var expected = Arrays.copyOf(b1, b1.length + b2.length);
      System.arraycopy(b2, 0, expected, b1.length, b2.length);
      if (Arrays.equals(actual, expected)) {
        JsHelper.consoleInfo("testFileAppend: verify complete");
        onComplete.f();
      } else {
        eh.accept("result is different from expected");
      }
    }, eh);
    hFile.writeText(
        b1, null,
        () -> hFile.writeAppend(b1.length, b2, verify, eh), eh
    );
  }

  static Consumer<String> wrap(JsFunctions.Consumer<JSString> onError) {
    return error -> onError.f(JSString.valueOf(error));
  }

  @Override
  public void testMkDir(
      JsFolderInput dir, JSString name,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  ) {
    DirectoryHandle dh = JsFolderInput.directoryHandle(dir);

    FsWorkerJobs.mkDir(
        pool, dh, name.stringValue(),
        r -> {
          System.out.println("DiffTestApi.testMkDir ok, r = " + r);
          onComplete.f();
        },
        wrap(onError));
  }

  @Override
  public void testRemoveDir(
      JsFolderInput dir,
      JsFunctions.Runnable onComplete,
      JsFunctions.Consumer<JSString> onError
  ) {
    DirectoryHandle dh = JsFolderInput.directoryHandle(dir);

    FsWorkerJobs.removeDir(pool, dh,
        () -> {
          System.out.println("DiffTestApi.testRemoveDir ok");
          onComplete.f();
        },
        wrap(onError));
  }

  static String shortText(String s) {
    String sh = s.length() > 80 ? s.substring(0, 80) : s;
    return sh.replaceAll("\r", "").replaceAll("\n", " ");
  }
}
