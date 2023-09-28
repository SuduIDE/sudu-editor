package org.sudu.experiments.editor;

import org.sudu.experiments.editor.ui.colors.EditorColorScheme;

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
