package org.sudu.experiments.editor;

import org.sudu.experiments.editor.worker.parser.FileParser;

import java.util.Locale;
import java.util.Objects;

public interface Languages {

  String TEXT = "text";
  String JAVA = "java";
  String CPP = "cpp";
  String JS = "js";

  static String getLanguage(String lang) {
    return switch (lang.toLowerCase(Locale.ENGLISH)) {
      case "text", "txt", "plaintext" -> TEXT;
      case "java" -> JAVA;
      case "cpp", "c++" -> CPP;
      case "js", "javascript" -> JS;
      default -> null;
    };
  }

  static String languageFromFilename(String path) {
    if (path == null) return null;
    if (path.endsWith(".cpp")
        || path.endsWith(".cc")
        || path.endsWith(".cxx")
        || path.endsWith(".hpp")
        || path.endsWith(".c")
        || path.endsWith(".h")) return CPP;
    if (path.endsWith(".java")) return JAVA;
    if (path.endsWith(".js")) return JS;
    return null;
  }

  static String getLanguage(int type) {
    return switch (type) {
      case 0 -> TEXT;
      case 1 -> JAVA;
      case 2 -> CPP;
      case 3 -> JS;
      default -> null;
    };
  }

  static int getType(String lang) {
    return switch (lang) {
      case Languages.TEXT -> FileParser.TEXT_FILE;
      case Languages.JAVA -> FileParser.JAVA_FILE;
      case Languages.CPP -> FileParser.CPP_FILE;
      case Languages.JS -> FileParser.JS_FILE;
      default -> -1;
    };
  }

  static String getLanguageOrDefault(int type, String def) {
    return Objects.requireNonNullElse(getLanguage(type), def);
  }

}
