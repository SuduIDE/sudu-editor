package org.sudu.experiments.diff;

import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.ScrollContent;
import org.sudu.experiments.ui.window.ScrollView;

class DiffSidePane extends ScrollView {
  final ScrollContent scrollContent = new ScrollContent();
  final DiffRefView diffRef = new DiffRefView(this, scrollContent);

  public DiffSidePane(UiContext uiContext) {
    super(uiContext);
    setContent(scrollContent);
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    if (olDpr == 0) {
      scrollContent.setVirtualSize(700, 1600);
    }
  }

  public void setTheme(EditorColorScheme theme) {
    setScrollColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
  }
}
