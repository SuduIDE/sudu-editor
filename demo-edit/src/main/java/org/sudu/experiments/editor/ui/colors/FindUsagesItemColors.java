package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.V4f;

@SuppressWarnings("ClassCanBeRecord")
public class FindUsagesItemColors {
  public final V4f fileColor;
  public final V4f lineColor;
  public final V4f contentColor;
  public final V4f bgColor;
  public final V4f bgCaretColor;
  public final V4f textCaretColor;

  public FindUsagesItemColors(V4f fileColor, V4f lineColor, V4f contentColor, V4f bgColor, V4f bgCaretColor, V4f textCaretColor) {
    this.fileColor = fileColor;
    this.lineColor = lineColor;
    this.contentColor = contentColor;
    this.bgColor = bgColor;
    this.bgCaretColor = bgCaretColor;
    this.textCaretColor = textCaretColor;
  }

}
