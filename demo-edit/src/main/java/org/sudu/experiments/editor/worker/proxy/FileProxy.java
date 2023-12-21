package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.Debug;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.editor.Languages;
import org.sudu.experiments.editor.worker.parser.LineParser;
import org.sudu.experiments.math.ArrayOp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

public class FileProxy {

  public static final int TEXT_FILE = 0;
  public static final int JAVA_FILE = 1;
  public static final int CPP_FILE = 2;
  public static final int JS_FILE = 3;
  public static final int ACTIVITY_FILE = 4;

  public static final JavaProxy javaProxy = new JavaProxy();
  public static final CppProxy cppProxy = new CppProxy();
  public static final JavaScriptProxy javascriptProxy = new JavaScriptProxy();
  public static final ActivityProxy activityProxy = new ActivityProxy();

  public static final String asyncParseFile = "asyncParseFile";

  public static void asyncParseFile(FileHandle file, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> {
          char[] source = prepareChars(bytes);
          String lang = Languages.languageFromFilename(file.getName());
          BaseProxy proxy = getBaseProxy(Languages.getType(lang));
          parseFileStructure(proxy, source, result);
        },
        Debug::consoleInfo
    );
  }

  public static final String asyncParseFullFile = "asyncFullParseFile";

  public static void asyncParseFullFile(FileHandle file, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> {
          char[] source = prepareChars(bytes);
          String lang = Languages.languageFromFilename(file.getName());
          BaseProxy proxy = getBaseProxy(Languages.getType(lang));
          parseFullFile(proxy, source, result);
        },
        Debug::consoleInfo
    );
  }

  public static final String asyncParseFirstLines = "asyncParseFirstLines";

  public static void asyncParseFirstLines(FileHandle file, int[] lines, Consumer<Object[]> result) {
    file.readAsBytes(
        bytes -> {
          char[] source = prepareChars(bytes);
          String lang = Languages.languageFromFilename(file.getName());
          BaseProxy proxy = getBaseProxy(Languages.getType(lang));
          parseFirstLines(proxy, source, lines, result);
        },
        Debug::consoleInfo
    );
  }

  public static final String asyncIterativeParsing = "asyncIterativeParsing";

  public static void asyncIterativeParsing(
      char[] chars, int[] type,
      int[] interval, int[] version,
      int[] graphInts, char[] graphChars,
      Consumer<Object[]> result
  ) {
    var proxy = getBaseProxy(type[0]);
    if (proxy == javaProxy || proxy == cppProxy) proxy.parseIntervalScope(chars, interval, version, graphInts, graphChars, result);
    else proxy.parseInterval(chars, interval, version, result);
  }

  public static void parseFirstLines(BaseProxy proxy, char[] source, int[] lines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    if (proxy == null) LineParser.parse(source, list);
    else proxy.parseFirstLines(source, lines, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static void parseFileStructure(BaseProxy proxy, char[] source, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    if (proxy == null) LineParser.parse(source, list);
    else if (proxy == javaProxy) javaProxy.parseStructure(source, list);
    else proxy.parseFullFile(source, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static void parseFullFile(BaseProxy proxy, char[] source, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    if (proxy == null) LineParser.parse(source, list);
    else if (proxy == javaProxy || proxy == cppProxy) proxy.parseFullFileScopes(source, list);
    else proxy.parseFullFile(source, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static boolean isJavaExtension(String ext) {
    return ext.equals(".java");
  }

  private static BaseProxy getBaseProxy(int type) {
    return switch (type) {
      case JAVA_FILE -> javaProxy;
      case CPP_FILE -> cppProxy;
      case JS_FILE -> javascriptProxy;
      case ACTIVITY_FILE -> activityProxy;
      default -> null;
    };
  }

  private static char[] prepareChars(byte[] bytes) {
    String src = new String(bytes, StandardCharsets.UTF_8);
    src = src.replace("\r", "");
    return src.toCharArray();
  }
}
