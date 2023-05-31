package org.sudu.experiments.demo.worker;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

public class FileParser {

  public static final int TEXT_FILE = 0;
  public static final int JAVA_FILE = 1;

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

  private static void parseChars(String res, char[] chars, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> parseJavaChars(chars, result);
      default -> parseChars(chars, result);
    }
  }

  private static void parseFullChars(String res, char[] chars, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> parseFullJavaChars(chars, result);
      default -> parseChars(chars, result);
    }
  }

  private static void parseFirstLinesChars(String res, char[] chars, int[] lines, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> JavaLexerFirstLines.parseFirstLines(chars, lines, result);
      default -> LineParser.parseFirstLines(chars, lines, result);
    }
  }

  private static void parseChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    LineParser.parseChars(chars, list);
    list.add(new int[]{TEXT_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseJavaChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaStructureParser.parseChars(chars, list);
    list.add(new int[]{JAVA_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFullJavaChars(char[] chars, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaParser.parseChars(chars, list);
    list.add(new int[]{JAVA_FILE});
    ArrayOp.sendArrayList(list, result);
  }
}
