package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.ui.UiFont;

public class EditorColorScheme {
  public final EditorColors editor;
  public final FileTreeViewTheme fileTreeView;
  public final DialogItemColors dialogItem;
  public final CodeElementColor[] codeElement;
  public final LineNumbersColors lineNumber;
  public final DiffColors diff;

  public final UiFont popupMenuFont = new UiFont(Fonts.SegoeUI, 17);
  public final UiFont usagesFont = new UiFont(Fonts.Consolas, 15);
  public final UiFont fileViewFont = new UiFont(Fonts.SegoeUI, 15);
  public final UiFont fileViewIcons = new UiFont(Fonts.codicon, 15);

  public Color error() {
    return codeElement[ParserConstants.TokenTypes.ERROR].colorF;
  }

  public static EditorColorScheme darculaIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.darculaIdeaColorScheme(),
        FileTreeViewTheme.darculaIdea(),
        IdeaCodeColors.codeElementColorsDarcula(),
        LineNumbersColors.darcula(),
        Themes.darculaColorScheme(),
        DiffColors.darcula()
    );
  }

  public static EditorColorScheme darkIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.darkIdeaColorScheme(),
        FileTreeViewTheme.darkIdea(),
        IdeaCodeColors.codeElementColorsDark(),
        LineNumbersColors.dark(),
        Themes.darkColorScheme(),
        DiffColors.dark()
    );
  }

  public static EditorColorScheme lightIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.lightIdeaColorScheme(),
        FileTreeViewTheme.lightIdea(),
        IdeaCodeColors.codeElementColorsLight(),
        LineNumbersColors.light(),
        Themes.lightColorScheme(),
        DiffColors.light()
    );
  }

  private EditorColorScheme(
      EditorColors editor, FileTreeViewTheme fileTreeView,
      CodeElementColor[] codeElement,
      LineNumbersColors lineNumber,
      DialogItemColors dialogItem,
      DiffColors diff
  ) {
    this.editor = editor;
    this.fileTreeView = fileTreeView;
    this.codeElement = codeElement;
    this.lineNumber = lineNumber;
    if (codeElement.length < ParserConstants.TokenTypes.TYPES_LENGTH) {
      throw new IllegalArgumentException();
    }
    this.dialogItem = dialogItem;
    this.diff = diff;
  }

  public Color bgColor(Color bg) {
    return bg != null ? bg : editor.bg;
  }
}
