package org.sudu.experiments.demo.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.demo.worker.WorkerTest.TestJobs;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.worker.ArrayView;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EditorWorker {
  public static void execute(String method, Object[] a, Consumer<Object[]> onResult) {
    System.out.println("EditorWorker: method = " + method);

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
      case LineParser.PARSE_LINES -> LineParser.parseBytes(array(a, 0).bytes(), result);
      case KeywordParser.PARSE_KEYWORDS -> KeywordParser.parseBytes(array(a, 0).bytes(), result);
      case JavaParser.PARSE_BYTES_JAVA -> JavaParser.parseBytes(array(a, 0).bytes(), result);
      case JavaParser.PARSE_BYTES_JAVA_VIEWPORT -> JavaParser.parseViewport(array(a, 0).bytes(), array(a, 1).ints(), array(a, 2).ints(), result);
      case JavaStructureParser.PARSE_STRUCTURE_JAVA -> JavaStructureParser.parseBytes(array(a, 0).bytes(), result);
    }
  }

  static void asyncMethod(String method, Object[] a, Consumer<Object[]> r) {
    switch (method) {
      case TestJobs.asyncWithFile -> TestJobs.asyncWithFile(file(a, 0), r);
      case FileParser.asyncParseFile -> FileParser.asyncParseFile(file(a, 0), r);
      case FileParser.asyncParseFullFile -> FileParser.asyncParseFullFile(file(a, 0), r);
      case LineParser.PARSE_FIRST_LINES -> LineParser.parseFirstLines(file(a, 0), array(a, 1).ints(), r);
      case JavaLexerFirstLines.LEXER_FIRST_LINES -> JavaLexerFirstLines.parseFirstLines(file(a, 0), array(a, 1).ints(), r);
    }
  }

  static ArrayView array(Object[] args, int index) {
    return (ArrayView) args[index];
  }

  static String string(Object[] args, int index) {
    return (String) args[index];
  }

  static FileHandle file(Object[] args, int index) {
    return (FileHandle) args[index];
  }
}
