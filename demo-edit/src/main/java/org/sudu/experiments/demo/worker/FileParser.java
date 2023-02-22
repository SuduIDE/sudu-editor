package org.sudu.experiments.demo.worker;

import org.sudu.experiments.FileHandle;
import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

public class FileParser {

  public static final String asyncParseFile = "asyncParseFile";

  public static void asyncParseFile(FileHandle file, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> parseBytes(bytes, result),
        error -> parseBytes(error.getBytes(StandardCharsets.UTF_8), result)
    );
  }

  private static void parseBytes(byte[] bytes, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    JavaParser.parseBytes(bytes, list);
    ArrayOp.sendArrayList(list, result);
  }
}