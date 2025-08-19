package org.sudu.experiments.editor.worker.proxy;

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
  public static final int HTML_FILE = 5;
  public static final int TS_FILE = 6;
  public static final int JSON_FILE = 7;

  public static final JavaProxy javaProxy = new JavaProxy();
  public static final CppProxy cppProxy = new CppProxy();
  public static final JavaScriptProxy javascriptProxy = new JavaScriptProxy();
  public static final TypeScriptProxy typescriptProxy = new TypeScriptProxy();
  public static final ActivityProxy activityProxy = new ActivityProxy();
  public static final HtmlProxy htmlProxy = new HtmlProxy();
  public static final JsonProxy jsonProxy = new JsonProxy();
  public static final TextProxy textProxy = new TextProxy();

  public static final String asyncParseFile = "asyncParseFile";

  public static void asyncParseFile(char[] source, int[] lang, Consumer<Object[]> result) {
    if (lang.length == 0) throw new IllegalArgumentException("Lang type is empty");
    BaseProxy proxy = getBaseProxy(lang[0]);
    parseFileStructure(proxy, source, result);
  }

  public static final String asyncParseFullFile = "asyncFullParseFile";

  public static void asyncParseFullFile(char[] source, int[] lang, Consumer<Object[]> result) {
    if (lang.length == 0) throw new IllegalArgumentException("Lang type is empty");
    BaseProxy proxy = getBaseProxy(lang[0]);
    parseFullFile(proxy, source, result);
  }

  public static final String asyncLexer = "asyncLexer";

  public static void asyncLexer(char[] source, int[] langAndLines, Consumer<Object[]> result) {
    if (langAndLines.length < 2) throw new IllegalArgumentException("Lang type or number of lines is empty");
    int lang = langAndLines[0], lines = langAndLines[1];
    BaseProxy proxy = getBaseProxy(lang);
    parseFirstLines(proxy, source, lines, result);
  }

  public static final String asyncIterativeParsing = "asyncIterativeParsing";

  public static void asyncIterativeParsing(
      char[] chars, int[] type,
      int[] interval, int[] version,
      int[] graphInts, char[] graphChars,
      Consumer<Object[]> result
  ) {
    var proxy = getBaseProxy(type[0]);
    if (proxy == javaProxy || proxy == cppProxy)
      proxy.parseIntervalScope(chars, interval, version, graphInts, graphChars, result);
    else proxy.parseInterval(chars, interval, version, result);
  }

  public static void parseFirstLines(BaseProxy proxy, char[] source, int numOfLines, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    /*if (proxy == null) ElementParser.parse(source, list);
    else */proxy.parseFirstLines(source, numOfLines, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static void parseFileStructure(BaseProxy proxy, char[] source, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    /*if (proxy == null) ElementParser.parse(source, list);
    else */if (proxy == javaProxy) javaProxy.parseStructure(source, list);
    else proxy.parseFullFile(source, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static void parseFullFile(BaseProxy proxy, char[] source, Consumer<Object[]> result) {
    ArrayList<Object> list = new ArrayList<>();
    /*if (proxy == null) ElementParser.parse(source, list);
    else */if (proxy == javaProxy || proxy == cppProxy) proxy.parseFullFileScopes(source, list);
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
      case TS_FILE -> typescriptProxy;
      case ACTIVITY_FILE -> activityProxy;
      case HTML_FILE -> htmlProxy;
      case JSON_FILE -> jsonProxy;
      default -> textProxy;
    };
  }

  private static char[] prepareChars(byte[] bytes) {
    String src = new String(bytes, StandardCharsets.UTF_8);
    src = src.replace("\r", "");
    return src.toCharArray();
  }
}
