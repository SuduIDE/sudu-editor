package org.sudu.experiments.demo;

import org.sudu.experiments.demo.ui.colors.EditorColorScheme;

public interface EditorTheme extends EditorUi.ThemeApi {
  void applyTheme(EditorColorScheme theme);

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
