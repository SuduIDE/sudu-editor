package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;

public interface ThemeControl {
  void applyTheme(EditorColorScheme theme);

  default void setTheme(String theme) {
    switch (theme) {
      case "light" -> toggleLight();
      case "darcula" -> toggleDarcula();
      case "dark" -> toggleDark();
      default -> Debug.consoleInfo("unknown theme: " + theme);
    }
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
