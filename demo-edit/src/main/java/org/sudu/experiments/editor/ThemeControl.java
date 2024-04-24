package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;

public interface ThemeControl {
  void applyTheme(EditorColorScheme theme);

  default void setTheme(String theme) {
    var t = resolveTheme(theme);
    if (t != null) {
      applyTheme(t);
    } else {
      Debug.consoleInfo("unknown theme: " + theme);
    }
  }

  static EditorColorScheme resolveTheme(String name) {
    return switch (name) {
      case "light" -> EditorColorScheme.lightIdeaColorScheme();
      case "darcula" -> EditorColorScheme.darculaIdeaColorScheme();
      case "dark" -> EditorColorScheme.darkIdeaColorScheme();
      default -> null;
    };
  }

  default void toggleDarcula() {
    applyTheme(EditorColorScheme.darculaIdeaColorScheme());
  }

  default void toggleDark() {
    applyTheme(EditorColorScheme.darkIdeaColorScheme());
  }

  default void toggleLight() {
    applyTheme(EditorColorScheme.lightIdeaColorScheme());
  }
}
