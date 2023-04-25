package org.sudu.experiments.demo.worker;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.demo.EditorComponent;
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
        bytes -> parseBytes(file.getExtension(), bytes, result),
        error -> parseBytes(file.getExtension(), error.getBytes(StandardCharsets.UTF_8), result)
    );
  }

  public static final String asyncParseFullFile = "asyncFullParseFile";

  public static void asyncParseFullFile(FileHandle file, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> parseFullBytes(file.getExtension(), bytes, result),
        Debug::consoleInfo
    );
  }

  public static final String asyncParseFirstLines = "asyncParseFirstLines";

  public static void asyncParseFirstLines(FileHandle file, int[] lines, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> parseFirstLinesBytes(file.getExtension(), bytes, lines, result),
        Debug::consoleInfo
    );
  }

  private static void parseBytes(String res, byte[] bytes, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> parseJavaBytes(bytes, result);
      default -> parseBytes(bytes, result);
    }
  }

  private static void parseFullBytes(String res, byte[] bytes, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> parseFullJavaBytes(bytes, result);
      default -> parseBytes(bytes, result);
    }
  }

  private static void parseFirstLinesBytes(String res, byte[] bytes, int[] lines, Consumer<Object[]> result) {
    switch (res) {
      case ".java" -> JavaLexerFirstLines.parseFirstLines(bytes, lines, result);
      default -> LineParser.parseFirstLines(bytes, lines, result);
    }
  }

  private static void parseBytes(byte[] bytes, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    LineParser.parseBytes(bytes, list);
    list.add(new int[]{TEXT_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseJavaBytes(byte[] bytes, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaStructureParser.parseBytes(bytes, list);
    list.add(new int[]{JAVA_FILE});
    ArrayOp.sendArrayList(list, result);
  }

  private static void parseFullJavaBytes(byte[] bytes, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaParser.parseBytes(bytes, list);
    list.add(new int[]{JAVA_FILE});
    ArrayOp.sendArrayList(list, result);
  }
}