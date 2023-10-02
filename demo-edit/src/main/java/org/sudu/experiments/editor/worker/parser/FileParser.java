package org.sudu.experiments.editor.worker.parser;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

public class FileParser {

  public static final int TEXT_FILE = 0;
  public static final int JAVA_FILE = 1;
  public static final int CPP_FILE = 2;
  public static final int JS_FILE = 3;

  public static final String asyncParseFile = "asyncParseFile";

  public static void asyncParseFile(FileHandle file, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> {
          String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");
          parseChars(file.getExtension(), source.toCharArray(), result);
        },
        error -> parseChars(file.getExtension(), error.toCharArray(), result)
    );
  }

  public static final String asyncParseFullFile = "asyncFullParseFile";

  public static void asyncParseFullFile(FileHandle file, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> {
          String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");
          parseFullChars(file.getExtension(), source.toCharArray(), result);
        },
        Debug::consoleInfo
    );
  }

  public static final String asyncParseFirstLines = "asyncParseFirstLines";

  public static void asyncParseFirstLines(FileHandle file, int[] lines, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> {
          String source = new String(bytes, StandardCharsets.UTF_8).replace("\r", "");
          parseFirstLinesChars(file.getExtension(), source.toCharArray(), lines, result);
        },
        Debug::consoleInfo
    );
  }

  public static final String asyncIterativeParsing = "asyncIterativeParsing";

  public static void asyncIterativeParsing(char[] chars, int[] type, int[] interval, Consumer<Object[]> result) {
    switch (type[0]) {
      case JAVA_FILE -> JavaParser.parseInterval(chars,interval, result);
      case CPP_FILE -> CppParser.parseInterval(chars,interval, result);
      case JS_FILE -> JavaScriptParser.parseInterval(chars,interval, result);
    }
  }

  public static boolean isJavaExtension(String ext) {
    return ext.equals(".java");
  }

  public static boolean isCppExtension(String ext) {
    return ext.equals(".cpp") || ext.equals(".cc") || ext.equals(".h");
  }

  public static boolean isJsExtension(String ext) {
    return ext.equals(".js");
  }

  private static void parseChars(String res, char[] chars, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> parseJavaChars(chars, result);
      case ".cpp", ".cc", ".h" -> parseCppChars(chars, result);
      case ".js" -> parseJavaScriptChars(chars, result);
      default -> parseChars(chars, result);
    }
  }

  private static void parseFullChars(String res, char[] chars, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> parseFullJavaChars(chars, result);
      case ".cpp", ".cc", ".h" -> parseFullCppChars(chars, result);
      case ".js" -> parseFullJavaScriptChars(chars, result);
      default -> parseChars(chars, result);
    }
  }

  private static void parseFirstLinesChars(String res, char[] chars, int[] lines, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> JavaParser.parseFirstLines(chars, lines, result);
      case ".cpp", ".cc", ".h" -> CppParser.parseFirstLines(chars, lines, result);
      case ".js" -> JavaScriptParser.parseFirstLines(chars, lines, result);
      default -> LineParser.parseFirstLines(chars, lines, result);
    }
  }

  private static void parseChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    LineParser.parse(chars, list);
    list.add(new int[]{TEXT_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseJavaChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaStructureParser.parseChars(chars, list);
    list.add(new int[]{JAVA_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseCppChars(char[] chars, Consumer<Object[]> result) {
    //todo structure cpp parsing
    parseFullCppChars(chars, result);
  }

  private static void parseJavaScriptChars(char[] chars, Consumer<Object[]> result) {
    //todo structure js parsing
    parseFullJavaScriptChars(chars, result);
  }

  private static void parseFullJavaChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaParser.parse(chars, list);
    list.add(new int[]{JAVA_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFullCppChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    CppParser.parse(chars, list);
    list.add(new int[]{CPP_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFullJavaScriptChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaScriptParser.parse(chars, list);
    list.add(new int[]{JS_FILE});
    ArrayOp.sendArrayList(list, result);
  }
}
