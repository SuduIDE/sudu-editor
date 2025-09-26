package org.sudu.experiments.editor.worker;

import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FileDiffWorker {

  public static void execute(String method, Object[] a, Consumer<Object[]> onResult) {
    if (method.startsWith("async")) {
      asyncMethod(method, a, onResult);
    } else {
      ArrayList<Object> list = new ArrayList<>();
      syncMethod(method, a, list);
      ArrayOp.sendArrayList(list, onResult);
    }
  }

  static void syncMethod(String method, Object[] a, ArrayList<Object> result) {
    switch (method) {
      case "foo" -> result.add("foo");
      case TestJobs.withString -> TestJobs.withString(ArgsCast.string(a, 0), result);
      case TestJobs.withChars -> TestJobs.withChars(ArgsCast.array(a, 0).chars(), result);
      case TestJobs.withBytes -> TestJobs.withBytes(ArgsCast.array(a, 0).bytes(), result);
      case TestJobs.withInts -> TestJobs.withInts(ArgsCast.array(a, 0).ints(), result);
      case TestJobs.fibonacci -> TestJobs.fibonacci(ArgsCast.array(a, 0).ints(), result);

      default -> System.out.println("syncMethod = " + method);
    }
  }

  static void asyncMethod(String method, Object[] a, Consumer<Object[]> r) {
    switch (method) {
      case TestJobs.asyncWithFile -> TestJobs.asyncWithFile(
          ArgsCast.file(a, 0), r);
      case TestJobs.asyncWithDir -> TestJobs.asyncWithDir(
          ArgsCast.dir(a, 0), r);
      case FileCompare.asyncCompareFiles -> FileCompare.asyncCompareFiles(a,r);
      case DiffUtils.CMP_FOLDERS -> DiffUtils.compareFolders(
          ArgsCast.dir(a, 0), ArgsCast.dir(a, 1), r);
      case DiffUtils.READ_FOLDER -> DiffUtils.readFolder(
          ArgsCast.dir(a, 0), ArgsCast.array(a, 1).ints(), r);
      case DiffUtils.REREAD_FOLDER -> DiffUtils.rereadFolder(
          ArgsCast.dir(a, 0),
          ArgsCast.array(a, 1).ints(),
          ArgsCast.array(a, 2).chars(), r
      );
      case DiffUtils.asyncListDirectory -> DiffUtils.listDirectory(
          ArgsCast.dir(a, 0), r);

      case FsWorkerJobs.asyncCopyFile -> FsWorkerJobs.asyncCopyFile(a, r);
      case FsWorkerJobs.asyncFileWriteText -> FsWorkerJobs.asyncFileWriteText(a, r);
      case FsWorkerJobs.asyncReadTextFile -> FsWorkerJobs.asyncReadTextFile(a, r);
      case FsWorkerJobs.asyncDetectCodePage -> FsWorkerJobs.asyncDetectCodePage(a, r);
      case FsWorkerJobs.asyncReadBinFile -> FsWorkerJobs.asyncReadBinFile(a, r);
      case FsWorkerJobs.asyncRemoveFile -> FsWorkerJobs.asyncRemoveFile(a, r);
      case FsWorkerJobs.asyncRemoveDir -> FsWorkerJobs.asyncRemoveDir(a, r);
      case FsWorkerJobs.asyncMkDir -> FsWorkerJobs.asyncMkDir(a, r);
      case FsWorkerJobs.asyncStats -> FsWorkerJobs.asyncStats(a, r);
      case SizeScanner.asyncSizeScanner -> SizeScanner.asyncSizeScanner(a, r);

      default -> System.out.println("asyncMethod = " + method);
    }
  }
}
