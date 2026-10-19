package org.sudu.experiments.editor;

import org.sudu.experiments.editor.worker.parser.LangBuildFlags;
import org.sudu.experiments.editor.worker.proxy.FileProxy;

import java.util.ArrayList;
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
    ArrayList<String> list = new ArrayList<>();
    list.add(TEXT);
    if (LangBuildFlags.JAVA) list.add(JAVA);
    if (LangBuildFlags.CPP) list.add(CPP);
    if (LangBuildFlags.JS) list.add(JS);
    if (LangBuildFlags.TS) list.add(TS);
    if (LangBuildFlags.HTML) list.add(HTML);
    if (LangBuildFlags.JSON) list.add(JSON);
    if (LangBuildFlags.ACTIVITY) list.add(ACTIVITY);
    if (LangBuildFlags.PYTHON) list.add(PYTHON);
    if (LangBuildFlags.ASCEND_C) list.add(ASCEND_C);
    return list.toArray(new String[0]);
  }

  static String getLanguage(String lang) {
    String resolved = switch (lang.toLowerCase(Locale.ENGLISH)) {
      case "text", "txt", "plaintext" -> TEXT;
      case "java" -> JAVA;
      case "cpp", "c++" -> CPP;
      case "js", "mjs", "cjs", "javascript" -> JS;
      case "ts", "typescript" -> TS;
      case "activity" -> ACTIVITY;
      case "html" -> HTML;
      case "json" -> JSON;
      case "python", "py" -> PYTHON;
      case "ascend-c", "ascendc", "asc" -> ASCEND_C;
      default -> null;
    };
    if (resolved == null) return null;
    return resolveLanguage(resolved);
  }

  static String languageFromFilename(String path) {
    int d;
    if (path == null || (d = path.lastIndexOf('.')) == -1) return TEXT;
    String extension = path.substring(d);
    String lang = switch (extension) {
      case ".cpp", ".cc", ".cxx", ".hpp", ".c", ".h" -> CPP;
      case ".java" -> JAVA;
      case ".js", ".mjs", ".cjs" -> JS;
      case ".ts" -> TS;
      case ".activity" -> ACTIVITY;
      case ".html", ".xml" -> HTML;
      case ".json" -> JSON;
      case ".py", ".pyc", ".pyo" -> PYTHON;
      case ".asc" -> ASCEND_C;
      default -> TEXT;
    };
    return resolveLanguage(lang);
  }

  static int getType(String lang) {
    if (lang == null || !LangBuildFlags.isCompiled(lang)) return FileProxy.TEXT_FILE;
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

  /**
   * Resolve a language against compile-time {@link LangBuildFlags}.
   * For cpp / ascend-c, falls back to the other variant when only one is compiled in.
   */
  static String resolveLanguage(String lang) {
    if (isCppVersion(lang)) {
      boolean wantAscendC = Objects.equals(lang, ASCEND_C);
      if (wantAscendC && LangBuildFlags.ASCEND_C) return ASCEND_C;
      if (!wantAscendC && LangBuildFlags.CPP) return CPP;
      if (LangBuildFlags.CPP) return CPP;
      if (LangBuildFlags.ASCEND_C) return ASCEND_C;
      return TEXT;
    }
    return LangBuildFlags.isCompiled(lang) ? lang : TEXT;
  }
}
