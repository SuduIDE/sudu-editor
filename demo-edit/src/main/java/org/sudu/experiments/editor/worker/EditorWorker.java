package org.sudu.experiments.editor.worker;

import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.parser.LangBuildFlags;
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
      case TextProxy.PARSE_FULL_FILE -> textProxy.parseFullFile(a, result);
      case ScopeProxy.RESOLVE_ALL -> ScopeProxy.resolveAll(a, result);
      case DiffUtils.FIND_DIFFS -> DiffUtils.findDiffs(
          ArgsCast.array(a, 0).chars(), ArgsCast.array(a, 1).ints(),
          ArgsCast.array(a, 2).chars(), ArgsCast.array(a, 3).ints(),
          ArgsCast.array(a, 4).ints(), ArgsCast.array(a, 5).ints(),
          ArgsCast.array(a, 6).ints(), result);
      default -> syncLangMethod(method, a, result);
    }
  }

  /**
   * Language parse jobs guarded by {@link LangBuildFlags} so TeaVM can drop
   * disabled lexer/parser graphs from worker.js.
   */
  static void syncLangMethod(String method, Object[] a, ArrayList<Object> result) {
    if (LangBuildFlags.JAVA) {
      JavaProxy java = (JavaProxy) javaProxy;
      if (JavaProxy.PARSE_FULL_FILE.equals(method)) {
        java.parseFullFile(a, result);
        return;
      }
      if (JavaProxy.PARSE_FULL_FILE_SCOPES.equals(method)) {
        java.parseFullFileScopes(a, result);
        return;
      }
      if (JavaProxy.PARSE_VIEWPORT.equals(method)) {
        java.parseViewport(a, result);
        return;
      }
      if (JavaProxy.PARSE_STRUCTURE.equals(method)) {
        java.parseStructure(a, result);
        return;
      }
    }
    if (LangBuildFlags.CPP) {
      if (CppProxy.PARSE_FULL_FILE.equals(method)) {
        cppProxy.parseFullFile(a, result);
        return;
      }
      if (CppProxy.PARSE_FULL_FILE_SCOPES.equals(method)) {
        cppProxy.parseFullFileScopes(a, result);
        return;
      }
    }
    if (LangBuildFlags.JS && JavaScriptProxy.PARSE_FULL_FILE.equals(method)) {
      javascriptProxy.parseFullFile(a, result);
      return;
    }
    if (LangBuildFlags.TS && TypeScriptProxy.PARSE_FULL_FILE.equals(method)) {
      typescriptProxy.parseFullFile(a, result);
      return;
    }
    if (LangBuildFlags.ACTIVITY && ActivityProxy.PARSE_FULL_FILE.equals(method)) {
      activityProxy.parseFullFile(a, result);
      return;
    }
    if (LangBuildFlags.HTML && HtmlProxy.PARSE_FULL_FILE.equals(method)) {
      htmlProxy.parseFullFile(a, result);
      return;
    }
    if (LangBuildFlags.JSON && JsonProxy.PARSE_FULL_FILE.equals(method)) {
      jsonProxy.parseFullFile(a, result);
      return;
    }
    if (LangBuildFlags.PYTHON && PythonProxy.PARSE_FULL_FILE.equals(method)) {
      pythonProxy.parseFullFile(a, result);
      return;
    }
    if (LangBuildFlags.ASCEND_C && AscendCProxy.PARSE_FULL_FILE.equals(method)) {
      ascendCProxy.parseFullFile(a, result);
      return;
    }
    System.err.println("Unknown or compiled-out language method: " + method);
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
