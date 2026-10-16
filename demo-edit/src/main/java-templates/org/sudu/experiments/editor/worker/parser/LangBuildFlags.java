package org.sudu.experiments.editor.worker.parser;

/**
 * Compile-time language inclusion flags for TeaVM dead-code elimination.
 * Values are filtered from Maven properties {@code sudu.lang.*} at generate-sources.
 * Disabled languages are not linked into {@code worker.js}.
 */
public final class LangBuildFlags {

  private LangBuildFlags() {}

  public static final boolean JAVA = @sudu.lang.java@;
  public static final boolean CPP = @sudu.lang.cpp@;
  public static final boolean JS = @sudu.lang.js@;
  public static final boolean TS = @sudu.lang.ts@;
  public static final boolean ACTIVITY = @sudu.lang.activity@;
  public static final boolean HTML = @sudu.lang.html@;
  public static final boolean JSON = @sudu.lang.json@;
  public static final boolean PYTHON = @sudu.lang.python@;
  public static final boolean ASCEND_C = @sudu.lang.ascendc@;

  public static boolean isCompiled(String lang) {
    if (lang == null) return false;
    return switch (lang) {
      case "text" -> true;
      case "java" -> JAVA;
      case "cpp" -> CPP;
      case "js" -> JS;
      case "ts" -> TS;
      case "activity" -> ACTIVITY;
      case "html" -> HTML;
      case "json" -> JSON;
      case "python" -> PYTHON;
      case "ascend-c" -> ASCEND_C;
      default -> false;
    };
  }
}
