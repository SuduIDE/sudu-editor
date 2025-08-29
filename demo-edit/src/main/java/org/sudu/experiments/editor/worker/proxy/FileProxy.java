package org.sudu.experiments.editor.worker.proxy;

import org.sudu.experiments.editor.worker.ArgsCast;
import org.sudu.experiments.math.ArrayOp;

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

  public static void asyncParseFile(char[] source, int[] langVersion, Consumer<Object[]> result) {
    if (langVersion.length < 2) throw new IllegalArgumentException("Lang type is empty");
    BaseProxy proxy = getBaseProxy(langVersion[0]);
    parseFileStructure(proxy, source, langVersion[1], result);
  }

  public static final String asyncParseFullFile = "asyncFullParseFile";

  public static void asyncParseFullFile(char[] source, int[] langVersion, Consumer<Object[]> result) {
    if (langVersion.length < 2) throw new IllegalArgumentException("Lang type is empty");
    BaseProxy proxy = getBaseProxy(langVersion[0]);
    parseFullFile(proxy, source, langVersion[1], result);
  }

  public static final String asyncLexer = "asyncLexer";

  public static void asyncLexer(char[] source, int[] langLinesVersion, Consumer<Object[]> result) {
    if (langLinesVersion.length < 3) throw new IllegalArgumentException("Lang type or number of lines is empty");
    int lang = langLinesVersion[0], lines = langLinesVersion[1], version = langLinesVersion[2];
    BaseProxy proxy = getBaseProxy(lang);
    parseFirstLines(proxy, source, lines, version, result);
  }

  public static final String asyncIterativeParsing = "asyncIterativeParsing";

  public static void asyncIterativeParsing(Object[] a, Consumer<Object[]> r) {
    char[] chars = ArgsCast.array(a, 0).chars();
    int[] type = ArgsCast.array(a, 1).ints();
    int[] interval = ArgsCast.array(a, 2).ints();
    int version = ArgsCast.array(a, 3).ints()[0];
    int[] graphInts = ArgsCast.array(a, 4).ints();
    char[] graphChars = ArgsCast.array(a, 5).chars();
    asyncIterativeParsing(chars, type, interval, version, graphInts, graphChars, r);
  }

  public static void asyncIterativeParsing(
      char[] chars, int[] type,
      int[] interval, int version,
      int[] graphInts, char[] graphChars,
      Consumer<Object[]> result
  ) {
    var proxy = getBaseProxy(type[0]);
    if (proxy == javaProxy || proxy == cppProxy)
      proxy.parseIntervalScope(chars, interval, version, graphInts, graphChars, result);
    else proxy.parseInterval(chars, interval, version, result);
  }

  public static void parseFirstLines(
      BaseProxy proxy,
      char[] source,
      int numOfLines,
      int version,
      Consumer<Object[]> result
  ) {
    ArrayList<Object> list = new ArrayList<>();
    proxy.parseFirstLines(source, numOfLines, version, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static void parseFileStructure(
      BaseProxy proxy,
      char[] source,
      int version,
      Consumer<Object[]> result
  ) {
    ArrayList<Object> list = new ArrayList<>();
    if (proxy == javaProxy) javaProxy.parseStructure(source, version, list);
    else proxy.parseFullFile(source, version, list);
    ArrayOp.sendArrayList(list, result);
  }

  public static void parseFullFile(
      BaseProxy proxy,
      char[] source,
      int version,
      Consumer<Object[]> result
  ) {
    ArrayList<Object> list = new ArrayList<>();
    if (proxy == javaProxy || proxy == cppProxy) proxy.parseFullFileScopes(source, version, list);
    else proxy.parseFullFile(source, version, list);
    ArrayOp.sendArrayList(list, result);
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
}
