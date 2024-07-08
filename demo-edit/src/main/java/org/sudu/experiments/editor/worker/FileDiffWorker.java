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
      case TestJobs.asyncWithFile -> TestJobs.asyncWithFile(ArgsCast.file(a, 0), r);
      case TestJobs.asyncWithDir -> TestJobs.asyncWithDir(ArgsCast.dir(a, 0), r);
      case DiffUtils.CMP_FILES -> DiffUtils.compareFiles(ArgsCast.file(a, 0), ArgsCast.file(a, 1), r);
      case DiffUtils.CMP_FOLDERS -> DiffUtils.compareFolders(ArgsCast.dir(a, 0), ArgsCast.dir(a, 1), r);
      case DiffUtils.READ_FOLDER -> DiffUtils.readFolder(ArgsCast.dir(a, 0), ArgsCast.array(a, 1).ints(), r);
      default -> System.out.println("asyncMethod = " + method);
    }
  }
}
