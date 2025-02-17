package org.sudu.experiments.editor;

import org.sudu.experiments.editor.worker.proxy.FileProxy;

import java.util.Locale;

public interface Languages {

  String TEXT = "text";
  String JAVA = "java";
  String CPP = "cpp";
  String JS = "js";
  String TS = "ts";
  String ACTIVITY = "activity";
  String HTML = "html";

  static String[] getAllLanguages() {
    return new String[]{TEXT, JAVA, CPP, JS, TS, ACTIVITY};
  }

  static String getLanguage(String lang) {
    return switch (lang.toLowerCase(Locale.ENGLISH)) {
      case "text", "txt", "plaintext" -> TEXT;
      case "java" -> JAVA;
      case "cpp", "c++" -> CPP;
      case "js", "javascript" -> JS;
      case "activity" -> ACTIVITY;
      case "html" -> HTML;
      default -> null;
    };
  }

  static String languageFromFilename(String path) {
    if (path == null) return TEXT;
    if (path.endsWith(".cpp")
        || path.endsWith(".cc")
        || path.endsWith(".cxx")
        || path.endsWith(".hpp")
        || path.endsWith(".c")
        || path.endsWith(".h")) return CPP;
    if (path.endsWith(".java")) return JAVA;
    if (path.endsWith(".js")
        || path.endsWith(".mjs")
        || path.endsWith(".cjs")) return JS;
    if (path.endsWith(".ts")) return TS;
    if (path.endsWith(".activity")) return ACTIVITY;
    if (path.endsWith(".html")
        || path.endsWith(".xml")) return HTML;

    return TEXT;
  }

  static String getLanguage(int type) {
    return switch (type) {
      case 0 -> TEXT;
      case 1 -> JAVA;
      case 2 -> CPP;
      case 3 -> JS;
      case 4 -> ACTIVITY;
      default -> null;
    };
  }

  static int getType(String lang) {
    if (lang == null) return FileProxy.TEXT_FILE;
    return switch (lang) {
      case Languages.TEXT -> FileProxy.TEXT_FILE;
      case Languages.JAVA -> FileProxy.JAVA_FILE;
      case Languages.CPP -> FileProxy.CPP_FILE;
      case Languages.JS -> FileProxy.JS_FILE;
      case Languages.TS -> FileProxy.TS_FILE;
      case Languages.ACTIVITY -> FileProxy.ACTIVITY_FILE;
      case Languages.HTML -> FileProxy.HTML_FILE;
      default -> {
        System.err.println("Illegal language: " + lang);
        yield FileProxy.TEXT_FILE;
      }
    };
  }

  static boolean isFullReparseOnEdit(String language) {
    return language.equals(ACTIVITY)
        || language.equals(HTML)
        || language.equals(TEXT);
  }

}
