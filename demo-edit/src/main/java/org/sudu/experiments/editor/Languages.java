package org.sudu.experiments.editor;

import org.sudu.experiments.editor.worker.proxy.FileProxy;

import java.util.Locale;
import java.util.Objects;

public interface Languages {

  String TEXT = "text";
  String JAVA = "java";
  String CPP = "cpp";
  String JS = "js";
  String TS = "ts";
  String ACTIVITY = "activity";
  String HTML = "html";
  String JSON = "json";
  String PYTHON = "python";
  String ASCEND_C = "ascend-c";

  static String[] getAllLanguages() {
    return new String[]{TEXT, JAVA, CPP, JS, TS, HTML, JSON, ACTIVITY, PYTHON};
  }

  static String getLanguage(String lang) {
    return switch (lang.toLowerCase(Locale.ENGLISH)) {
      case "text", "txt", "plaintext" -> TEXT;
      case "java" -> JAVA;
      case "cpp", "c++" -> CPP;
      case "js", "mjs", "cjs", "javascript" -> JS;
      case "ts", "typescript" -> TS;
      case "activity" -> ACTIVITY;
      case "html" -> HTML;
      case "json" -> JSON;
      default -> null;
    };
  }

  static String languageFromFilename(String path) {
    int d;
    if (path == null || (d = path.lastIndexOf('.')) == -1) return TEXT;
    String extension = path.substring(d);
    return switch (extension) {
      case ".cpp", ".cc", ".cxx", ".hpp", ".c", ".h" -> CPP;
      case ".java" -> JAVA;
      case ".js", ".mjs", ".cjs" -> JS;
      case ".ts" -> TS;
      case ".activity" -> ACTIVITY;
      case ".html", ".xml" -> HTML;
      case ".json" -> JSON;
      case ".py", ".pyc", ".pyo" -> PYTHON;
      default -> TEXT;
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
      case Languages.PYTHON -> FileProxy.PYTHON_FILE;
      case Languages.JSON -> FileProxy.JSON_FILE;
      case Languages.ASCEND_C -> FileProxy.ASCEND_C_FILE;
      default -> {
        System.err.println("Illegal language: " + lang);
        yield FileProxy.TEXT_FILE;
      }
    };
  }

  static boolean isFullReparseOnEdit(String language) {
    return language.equals(ACTIVITY)
        || language.equals(HTML)
        || language.equals(JSON)
        || language.equals(TEXT);
  }

  static boolean isCppVersion(String lang) {
    return Objects.equals(lang, Languages.CPP)
        || Objects.equals(lang, Languages.ASCEND_C);
  }
}
