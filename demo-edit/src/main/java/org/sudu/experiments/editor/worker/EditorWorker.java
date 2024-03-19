package org.sudu.experiments.editor.worker;

import org.sudu.experiments.DirectoryHandle;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.parser.LineParser;
import org.sudu.experiments.editor.worker.proxy.*;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.worker.ArrayView;

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
      case TestJobs.withString -> TestJobs.withString(string(a, 0), result);
      case TestJobs.withChars -> TestJobs.withChars(array(a, 0).chars(), result);
      case TestJobs.withBytes -> TestJobs.withBytes(array(a, 0).bytes(), result);
      case TestJobs.withInts -> TestJobs.withInts(array(a, 0).ints(), result);
      case TestJobs.fibonacci -> TestJobs.fibonacci(array(a, 0).ints(), result);
      case ActivityProxy.PARSE_FULL_FILE -> activityProxy.parseFullFile(array(a, 0).chars(), result);
      case JavaProxy.PARSE_FULL_FILE -> javaProxy.parseFullFile(array(a, 0).chars(), result);
      case JavaProxy.PARSE_FULL_FILE_SCOPES -> javaProxy.parseFullFileScopes(array(a, 0).chars(), result);
      case JavaProxy.PARSE_VIEWPORT -> javaProxy.parseViewport(array(a, 0).chars(), array(a, 1).ints(), array(a, 2).ints(), result);
      case JavaProxy.PARSE_STRUCTURE -> javaProxy.parseStructure(array(a, 0).chars(), result);
      case CppProxy.PARSE_FULL_FILE -> cppProxy.parseFullFile(array(a, 0).chars(), result);
      case CppProxy.PARSE_FULL_FILE_SCOPES -> cppProxy.parseFullFileScopes(array(a, 0).chars(), result);
      case JavaScriptProxy.PARSE_FULL_FILE -> javascriptProxy.parseFullFile(array(a, 0).chars(), result);
      case HtmlProxy.PARSE_FULL_FILE -> htmlProxy.parseFullFile(array(a, 0).chars(), result);
      case LineParser.PARSE -> LineParser.parse(array(a, 0).chars(), result);
      case ScopeProxy.RESOLVE_ALL -> ScopeProxy.resolveAll(array(a, 0).ints(), array(a, 1).chars(), array(a, 2).ints(), result);
      case DiffUtils.FIND_DIFFS -> DiffUtils.findDiffs(
          array(a, 0).chars(), array(a, 1).ints(),
          array(a, 2).chars(), array(a, 3).ints(), result);
    }
  }

  static void asyncMethod(String method, Object[] a, Consumer<Object[]> r) {
    switch (method) {
      case TestJobs.asyncWithFile -> TestJobs.asyncWithFile(file(a, 0), r);
      case TestJobs.asyncWithDir -> TestJobs.asyncWithDir(dir(a, 0), r);
      case FileProxy.asyncParseFile -> FileProxy.asyncParseFile(array(a, 0).chars(), array(a, 1).ints(), r);
      case FileProxy.asyncParseFullFile -> FileProxy.asyncParseFullFile(array(a, 0).chars(), array(a, 1).ints(), r);
      case FileProxy.asyncParseFirstLines -> FileProxy.asyncParseFirstLines(array(a, 0).chars(), array(a, 1).ints(), r);
      case FileProxy.asyncIterativeParsing -> FileProxy.asyncIterativeParsing(
          array(a, 0).chars(), array(a, 1).ints(),
          array(a, 2).ints(),
          array(a,3).ints(), array(a, 4).ints(),
          array(a, 5).chars(), r
      );
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

  public static int numDemoThreads() {
    return TestJobs.numDemoThreads;
  }
}
