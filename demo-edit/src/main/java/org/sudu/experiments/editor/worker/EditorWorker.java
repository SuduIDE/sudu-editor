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
      case ActivityProxy.PARSE_FULL_FILE -> activityProxy.parseFullFile(a, result);
      case JavaProxy.PARSE_FULL_FILE -> javaProxy.parseFullFile(a, result);
      case JavaProxy.PARSE_FULL_FILE_SCOPES -> javaProxy.parseFullFileScopes(a, result);
      case JavaProxy.PARSE_VIEWPORT -> javaProxy.parseViewport(a, result);
      case JavaProxy.PARSE_STRUCTURE -> javaProxy.parseStructure(a, result);
      case CppProxy.PARSE_FULL_FILE -> cppProxy.parseFullFile(a, result);
      case CppProxy.PARSE_FULL_FILE_SCOPES -> cppProxy.parseFullFileScopes(a, result);
      case JavaScriptProxy.PARSE_FULL_FILE -> javascriptProxy.parseFullFile(a, result);
      case TypeScriptProxy.PARSE_FULL_FILE -> typescriptProxy.parseFullFile(a, result);
      case HtmlProxy.PARSE_FULL_FILE -> htmlProxy.parseFullFile(a, result);
      case JsonProxy.PARSE_FULL_FILE -> jsonProxy.parseFullFile(a, result);
      case TextProxy.PARSE_FULL_FILE -> textProxy.parseFullFile(a, result);
      case ScopeProxy.RESOLVE_ALL -> ScopeProxy.resolveAll(a, result);
      case DiffUtils.FIND_DIFFS -> DiffUtils.findDiffs(
          ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(),
          ArgsCast.array(a, 2).chars(), ArgsCast.array(a, 3).ints(),
          ArgsCast.array(a, 4).ints(), ArgsCast.array(a, 5).ints(),
          ArgsCast.array(a, 6).ints(), result);
    }
  }

  static void asyncMethod(String method, Object[] a, Consumer<Object[]> r) {
    switch (method) {
      case TestJobs.asyncWithFile -> TestJobs.asyncWithFile(ArgsCast.file(a, 0), r);
      case TestJobs.asyncWithDir -> TestJobs.asyncWithDir(ArgsCast.dir(a, 0), r);
      case FileProxy.asyncParseFile -> FileProxy.asyncParseFile(ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(), r);
      case FileProxy.asyncParseFullFile -> FileProxy.asyncParseFullFile(ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(), r);
      case FileProxy.asyncLexer -> FileProxy.asyncLexer(ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(), r);
      case FileProxy.asyncIterativeParsing -> FileProxy.asyncIterativeParsing(a, r);
      case DiffUtils.CMP_FOLDERS -> DiffUtils.compareFolders(ArgsCast.dir(a, 0), ArgsCast.dir(a, 1), r);
      case DiffUtils.READ_FOLDER -> DiffUtils.readFolder(ArgsCast.dir(a, 0), ArgsCast.array(a, 1).ints(), r);
      case FileCompare.asyncCompareFiles -> FileCompare.asyncCompareFiles(a, r);
      case FileCompare.asyncFindNextDiff -> FileCompare.asyncFindNextDiff(a, r);
      case SizeScanner.asyncSizeScanner -> SizeScanner.asyncSizeScanner(a, r);
      case FsWorkerJobs.asyncStats -> FsWorkerJobs.asyncStats(a, r);
      case FsWorkerJobs.asyncReadTextFile -> FsWorkerJobs.asyncReadTextFile(a, r);
      case FsWorkerJobs.asyncReadBinFile -> FsWorkerJobs.asyncReadBinFile(a, r);
      default -> System.err.println("Unknown method: " + method);
    }
  }

  public static int numDemoThreads() {
    return TestJobs.numDemoThreads;
  }
}
