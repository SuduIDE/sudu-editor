package org.sudu.experiments.demo;

import org.sudu.experiments.demo.ui.DialogItemColors;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.ParserConstants;

public class EditorColorScheme {
  public final EditorColors editor;
  public final DialogItemColors dialogItem;
  public final CodeElementColor[] codeElement;
  public final LineNumbersColors lineNumber;

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
