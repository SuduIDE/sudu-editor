package org.sudu.experiments.editor.worker;

import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.proxy.*;
import org.sudu.experiments.math.ArrayOp;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.sudu.experiments.editor.worker.proxy.FileProxy.*;

public class EditorWorker {
  public static void execute(String method, Object[] a, Consumer<Object[]> onResult) {
//    Debug.consoleInfo("EditorWorker: method = " + method);

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
      case TestJobs.withString -> TestJobs.withString(ArgsCast.string(a, 0), result);
      case TestJobs.withChars -> TestJobs.withChars(ArgsCast.array(a, 0).chars(), result);
      case TestJobs.withBytes -> TestJobs.withBytes(ArgsCast.array(a, 0).bytes(), result);
      case TestJobs.withInts -> TestJobs.withInts(ArgsCast.array(a, 0).ints(), result);
      case TestJobs.fibonacci -> TestJobs.fibonacci(ArgsCast.array(a, 0).ints(), result);
      case ActivityProxy.PARSE_FULL_FILE -> activityProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case JavaProxy.PARSE_FULL_FILE -> javaProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case JavaProxy.PARSE_FULL_FILE_SCOPES -> javaProxy.parseFullFileScopes(ArgsCast.array(a, 0).chars(), result);
      case JavaProxy.PARSE_VIEWPORT -> javaProxy.parseViewport(ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(), ArgsCast.array(a, 2).ints(), result);
      case JavaProxy.PARSE_STRUCTURE -> javaProxy.parseStructure(ArgsCast.array(a, 0).chars(), result);
      case CppProxy.PARSE_FULL_FILE -> cppProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case CppProxy.PARSE_FULL_FILE_SCOPES -> cppProxy.parseFullFileScopes(ArgsCast.array(a, 0).chars(), result);
      case JavaScriptProxy.PARSE_FULL_FILE -> javascriptProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case TypeScriptProxy.PARSE_FULL_FILE -> typescriptProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case HtmlProxy.PARSE_FULL_FILE -> htmlProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case JsonProxy.PARSE_FULL_FILE -> jsonProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case TextProxy.PARSE_FULL_FILE -> textProxy.parseFullFile(ArgsCast.array(a, 0).chars(), result);
      case ScopeProxy.RESOLVE_ALL -> ScopeProxy.resolveAll(ArgsCast.array(a, 0).ints(), ArgsCast.array(a, 1).chars(), ArgsCast.array(a, 2).ints(), result);
      case DiffUtils.FIND_DIFFS -> DiffUtils.findDiffs(
          ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(),
          ArgsCast.array(a, 2).chars(), ArgsCast.array(a, 3).ints(),
          ArgsCast.array(a, 4).ints(), result);
    }
  }

  static void asyncMethod(String method, Object[] a, Consumer<Object[]> r) {
    switch (method) {
      case TestJobs.asyncWithFile -> TestJobs.asyncWithFile(ArgsCast.file(a, 0), r);
      case TestJobs.asyncWithDir -> TestJobs.asyncWithDir(ArgsCast.dir(a, 0), r);
      case FileProxy.asyncParseFile -> FileProxy.asyncParseFile(ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(), r);
      case FileProxy.asyncParseFullFile -> FileProxy.asyncParseFullFile(ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(), r);
      case FileProxy.asyncParseFirstLines -> FileProxy.asyncParseFirstLines(ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(), r);
      case FileProxy.asyncIterativeParsing -> FileProxy.asyncIterativeParsing(
          ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(),
          ArgsCast.array(a, 2).ints(),
          ArgsCast.array(a,3).ints(), ArgsCast.array(a, 4).ints(),
          ArgsCast.array(a, 5).chars(), r
      );
      case DiffUtils.CMP_FOLDERS -> DiffUtils.compareFolders(ArgsCast.dir(a, 0), ArgsCast.dir(a, 1), r);
      case DiffUtils.READ_FOLDER -> DiffUtils.readFolder(ArgsCast.dir(a, 0), ArgsCast.array(a, 1).ints(), r);
      case FileCompare.asyncCompareFiles -> FileCompare.asyncCompareFiles(a, r);
      case SizeScanner.asyncSizeScanner -> SizeScanner.asyncSizeScanner(a, r);
    }
  }

  public static int numDemoThreads() {
    return TestJobs.numDemoThreads;
  }
}
