package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.ui.UiFont;

public class CodeLineColorScheme {
  public final Color currentLineBg;
  public final Color definitionBg;
  public final Color usageBg;
  public final Color selectionBg;
  public final Color defaultBg;
  public final CodeElementColor[] codeElement;
  public final DiffColors diff;

    public CodeLineColorScheme(
        Color currentLineBg, Color definitionBg, Color usageBg,
        Color selectionBg, Color defaultBg,
        CodeElementColor[] codeElement, DiffColors diff
    ) {
        this.currentLineBg = currentLineBg;
        this.definitionBg = definitionBg;
        this.usageBg = usageBg;
        this.selectionBg = selectionBg;
        this.defaultBg = defaultBg;
        this.codeElement = codeElement;
        this.diff = diff;
    }
}
