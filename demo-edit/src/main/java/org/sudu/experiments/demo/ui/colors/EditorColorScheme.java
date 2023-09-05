package org.sudu.experiments.demo.ui.colors;

import org.sudu.experiments.demo.ui.UiFont;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.ParserConstants;

public class EditorColorScheme {
  public final EditorColors editor;
  public final DialogItemColors dialogItem;
  public final CodeElementColor[] codeElement;
  public final LineNumbersColors lineNumber;

  public final UiFont windowTitleFont = new UiFont(Fonts.SegoeUI, 16);
  public final UiFont popupMenuFont = new UiFont(Fonts.SegoeUI, 17);
  public final UiFont usagesFont = new UiFont(Fonts.Consolas, 15);

  public static EditorColorScheme darculaIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.darculaIdeaColorScheme(),
        IdeaCodeColors.codeElementColors(),
        LineNumbersColors.darcula(),
        DialogItemColors.darculaColorScheme()
    );
  }

  public static EditorColorScheme lightIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.lightIdeaColorScheme(),
        IdeaCodeColors.codeElementColorsLight(),
        LineNumbersColors.light(),
        DialogItemColors.lightColorScheme()
    );
  }

  private EditorColorScheme(
      EditorColors editor,
      CodeElementColor[] codeElement,
      LineNumbersColors lineNumber,
      DialogItemColors dialogItem
  ) {
    this.editor = editor;
    this.codeElement = codeElement;
    this.lineNumber = lineNumber;
    if (codeElement.length < ParserConstants.TokenTypes.TYPES_LENGTH) {
      throw new IllegalArgumentException();
    }
    this.dialogItem = dialogItem;
  }

  public Color bgColor(Color bg) {
    return bg != null ? bg : editor.bg;
  }
}
