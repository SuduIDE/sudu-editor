package org.sudu.experiments;

import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.js.JsMessagePort;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.worker.ArrayView;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FileDiffWorker {

  public static void main(String[] args) {
    JsMessagePort.workerMain(FileDiffWorker::execute);
  }

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
      default -> System.out.println("syncMethod = " + method);
    }
  }

  static void asyncMethod(String method, Object[] a, Consumer<Object[]> r) {
    switch (method) {
      case DiffUtils.CMP_FILES -> DiffUtils.compareFiles(file(a, 0), file(a, 1), r);
      case DiffUtils.CMP_FOLDERS -> DiffUtils.compareFolders(dir(a, 0), dir(a, 1), r);
      default -> System.out.println("asyncMethod = " + method);
    }
  }

  public static ArrayView array(Object[] args, int index) {
    return (ArrayView) args[index];
  }

  public static String string(Object[] args, int index) {
    return (String) args[index];
  }

  public static FileHandle file(Object[] args, int index) {
    return (FileHandle) args[index];
  }

  public static DirectoryHandle dir(Object[] args, int index) {
    return (DirectoryHandle) args[index];
  }

}
