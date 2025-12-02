package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.ColorOp;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.ui.UiFont;

public class EditorColorScheme {

  // tree view
  public static final int TreeViewBackground = 0;
  public static final int TreeViewForeground = 1;
  public static final int SelectedItemBackground = 2;
  public static final int SelectedItemForeground = 3;
  public static final int HoveredItemBackground = 4;
  public static final int InactiveSelectionBackground = 5;

  public static final int AddedResourceForeground = 6;
  public static final int DeletedResourceForeground = 7;
  public static final int ModifiedResourceForeground = 8;
  public static final int IgnoredResourceForeground = 22;

  // window title
  public static final int PanelHeaderBackground = 9;
  public static final int PanelHeaderForeground = 10;

  // editor
  public static final int EditorBackground = 11;
  public static final int EditorForeground = 12;
  public static final int CurrentLineBorder = 13;
  public static final int CurrentLineBackground = 14;

  // editor diff
  public static final int DeletedRegionBackground = 15;
  public static final int DeletedTextBackground = 16;
  public static final int InsertedRegionBackground = 17;
  public static final int InsertedTextBackground = 18;

  public static final int LineNumberForeground = 19;
  public static final int ActiveLineNumberForeground = 20;
  public static final int LineNumberActiveForeground = 21;

  public static final int SyntaxKeyword = 30;
  public static final int SyntaxNull = 31;
  public static final int SyntaxBoolean = 32;
  public static final int SyntaxSemi = 33;
  public static final int SyntaxField = 34;
  public static final int SyntaxString = 35;
  public static final int SyntaxError = 36;
  public static final int SyntaxNumeric = 37;
  public static final int SyntaxMethod = 38;
  public static final int SyntaxComment = 39;
  public static final int SyntaxAnnotation = 40;
  public static final int SyntaxType = 41;
  public static final int SyntaxOperator = 42;
  public static final int SyntaxEscapeChar = 43;
  public static final int SyntaxCppDirective = 44;
  public static final int SyntaxDocumentation = 45;

  public static final int LastIndex = SyntaxDocumentation + 1;

  public final EditorColors editor;
  public final FileTreeViewTheme fileTreeView;
  public final DialogItemColors dialogItem;
  public final CodeElementColor[] codeElement;
  public final LineNumbersColors lineNumber;
  public final DiffColors codeDiffBg;
  public final DiffColors codeMapBg;
  public BackgroundHoverColors hoverColors;

  private static final float defaultFontSize = 15;
  private static final float defaultMenuFontSize = 17;
  private static final String defaultFont = Fonts.SegoeUI;
  private static final String defaultUsagesFont = Fonts.Consolas;

  public final UiFont popupMenuFont;
  public final UiFont usagesFont;
  public final UiFont treeViewFont;
  public final UiFont fileViewIcons;
  public final UiFont editorFont;

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
        DiffColors.codeDiffDarcula(),
        DiffColors.codeMapDarcula()
        );
  }

  public static EditorColorScheme darkIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.darkIdeaColorScheme(),
        FileTreeViewTheme.darkIdea(),
        IdeaCodeColors.codeElementColorsDark(),
        LineNumbersColors.dark(),
        Themes.darkColorScheme(),
        DiffColors.codeDiffDark(),
        DiffColors.codeMapDark()
    );
  }

  public static EditorColorScheme lightIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.lightIdeaColorScheme(),
        FileTreeViewTheme.lightIdea(),
        IdeaCodeColors.codeElementColorsLight(),
        LineNumbersColors.light(),
        Themes.lightColorScheme(),
        DiffColors.codeDiffLight(),
        DiffColors.codeMapLight()
    );
  }

  private EditorColorScheme(
      EditorColors editor, FileTreeViewTheme fileTreeView,
      CodeElementColor[] codeElement,
      LineNumbersColors lineNumber,
      DialogItemColors dialogItem,
      DiffColors codeDiffBg,
      DiffColors codeMapBg
  ) {
    this(editor, fileTreeView, codeElement, lineNumber, dialogItem,
        codeDiffBg, codeMapBg,
        new UiFont(defaultFont, defaultMenuFontSize),
        new UiFont(defaultUsagesFont, defaultFontSize),
        new UiFont(Fonts.codicon, defaultFontSize),
        new UiFont(EditorConst.FONT, EditorConst.DEFAULT_FONT_SIZE),
        new UiFont(defaultFont, defaultFontSize)
    );
  }

  private EditorColorScheme(
      EditorColors editor, FileTreeViewTheme fileTreeView,
      CodeElementColor[] codeElement,
      LineNumbersColors lineNumber,
      DialogItemColors dialogItem,
      DiffColors codeDiffBg,
      DiffColors codeMapBg,
      UiFont popupMenuFont,
      UiFont usagesFont,
      UiFont fileViewIcons,
      UiFont editorFont,
      UiFont treeViewFont
  ) {
    this.editor = editor;
    this.fileTreeView = fileTreeView;
    this.codeElement = codeElement;
    this.lineNumber = lineNumber;
    if (codeElement.length < ParserConstants.TokenTypes.TYPES_LENGTH) {
      throw new IllegalArgumentException();
    }
    this.dialogItem = dialogItem;
    this.codeDiffBg = codeDiffBg;
    this.codeMapBg = codeMapBg;
    this.popupMenuFont = popupMenuFont;
    this.usagesFont = usagesFont;
    this.fileViewIcons = fileViewIcons;
    this.editorFont = editorFont;
    this.treeViewFont = treeViewFont;
    recomputeHoverColors();
  }

  public CodeLineColorScheme editorCodeLineScheme() {
    return new CodeLineColorScheme(
        editor.currentLineBg,
        editor.definitionBg,
        editor.usageBg,
        editor.selectionBg,
        editor.bg,
        codeElement, codeDiffBg);
  }

  public CodeLineColorScheme treeViewCodeLineScheme() {
    return new CodeLineColorScheme(
        fileTreeView.selectedBg,
        editor.definitionBg,
        editor.usageBg,
        editor.selectionBg,
        editor.bg,
        codeElement, codeDiffBg);
  }

  public boolean modify(int m, Color c) {
    switch (m) {
      case TreeViewBackground -> {
        fileTreeView.bg = c;
        recomputeHoverColors();
      }
      case TreeViewForeground ->
          fileTreeView.textColor = c;

      case SelectedItemBackground -> {
        fileTreeView.selectedBg = ColorOp.blend(fileTreeView.bg, c);
        recomputeHoverColors();
      }
      case SelectedItemForeground ->
          fileTreeView.selectedText =c;
      case HoveredItemBackground -> {
        fileTreeView.hoveredBg = ColorOp.blend(fileTreeView.bg, c);
        recomputeHoverColors();
      }
      case InactiveSelectionBackground ->
          fileTreeView.inactiveSelectedBg = ColorOp.blend(fileTreeView.bg, c);

      case EditorBackground ->
        editor.bg = c;

      case EditorForeground -> {
        codeElement[0].colorF = c;
        lineNumber.textColor = c;
      }

      case CurrentLineBorder -> editor.currentLineBg =
          ColorOp.blend(editor.bg, c, 0.25f);

      case CurrentLineBackground ->
          editor.currentLineBg = ColorOp.blend(editor.bg, c);

      case PanelHeaderBackground ->
        dialogItem.windowColors.windowTitleBgColor = c;

      case PanelHeaderForeground ->
        dialogItem.windowColors.windowTitleTextColor = c;

      case DeletedRegionBackground -> {
//        codeDiffBg.deletedColor = ColorOp.blend(editor.bg, c);
        return false;
      }

      case DeletedTextBackground -> {
        return false;
      }

      case InsertedRegionBackground -> {
//        codeDiffBg.insertedColor = ColorOp.blend(editor.bg, c);
        return false;
      }

      case InsertedTextBackground -> {
        return false;
      }

      case AddedResourceForeground ->
        fileTreeView.textDiffColors.insertedColor = c;

      case DeletedResourceForeground ->
          fileTreeView.textDiffColors.deletedColor = c;

      case ModifiedResourceForeground ->
          fileTreeView.textDiffColors.editedColor = c;

      case IgnoredResourceForeground ->
          fileTreeView.textDiffColors.excludedColor = c;

      case LineNumberForeground -> {
        lineNumber.textColor = ColorOp.blend(editor.bg, c);
      }

      case ActiveLineNumberForeground -> {
        return false;
      }

      case LineNumberActiveForeground ->
        lineNumber.caretTextColor = ColorOp.blend(editor.bg, c);

      // syntax
      case SyntaxKeyword       -> setCodeForeground(ParserConstants.TokenTypes.KEYWORD, c);
      case SyntaxNull          -> setCodeForeground(ParserConstants.TokenTypes.NULL, c);
      case SyntaxBoolean       -> setCodeForeground(ParserConstants.TokenTypes.BOOLEAN, c);
      case SyntaxSemi          -> setCodeForeground(ParserConstants.TokenTypes.SEMI, c);
      case SyntaxField         -> setCodeForeground(ParserConstants.TokenTypes.FIELD, c);
      case SyntaxString        -> setCodeForeground(ParserConstants.TokenTypes.STRING, c);
      case SyntaxError         -> setCodeForeground(ParserConstants.TokenTypes.ERROR, c);
      case SyntaxNumeric       -> setCodeForeground(ParserConstants.TokenTypes.NUMERIC, c);
      case SyntaxMethod        -> setCodeForeground(ParserConstants.TokenTypes.METHOD, c);
      case SyntaxComment       -> setCodeForeground(ParserConstants.TokenTypes.COMMENT, c);
      case SyntaxAnnotation    -> setCodeForeground(ParserConstants.TokenTypes.ANNOTATION, c);
      case SyntaxType          -> setCodeForeground(ParserConstants.TokenTypes.TYPE, c);
      case SyntaxOperator      -> setCodeForeground(ParserConstants.TokenTypes.OPERATOR, c);
      case SyntaxEscapeChar    -> setCodeForeground(ParserConstants.TokenTypes.ESCAPE_CHAR, c);
      case SyntaxCppDirective  -> setCodeForeground(ParserConstants.TokenTypes.CPP_DIRECTIVE, c);
      case SyntaxDocumentation -> setCodeForeground(ParserConstants.TokenTypes.DOCUMENTATION, c);
    }

    return true;
  }

  private void setCodeForeground(int id, Color c) {
    codeElement[id] = new CodeElementColor(
        c, codeElement[id].colorB
    );
  }

  public MergeButtonsColors codeDiffMergeButtons() {
    return new MergeButtonsColors(
        null, codeDiffBg,
        lineNumber.caretTextColor,
        editor.bg,
        lineNumber.caretBgColor
    );
  }

  public MergeButtonsColors fileTreeMergeButtons() {
    return new MergeButtonsColors(
        fileTreeView.textDiffColors,
        null,
        fileTreeView.textColor,
        fileTreeView.bg,
        fileTreeView.hoveredBg
    );
  }

  private void recomputeHoverColors() {
    var hoverColor = fileTreeView.hoveredBg;
    hoverColors = new BackgroundHoverColors(
        codeDiffBg.blendWith(hoverColor),
        ColorOp.blend(fileTreeView.bg, hoverColor),
        ColorOp.blend(fileTreeView.selectedBg, hoverColor)
    );
  }

  public V4f getDiffColor(byte[] colors, int i) {
    return i >= colors.length || colors[i] == 0
        ? editor.bg : codeDiffBg.getDiffColor(colors[i], editor.bg);
  }

  public static String name(int n) {
    return switch (n) {
      case TreeViewBackground -> "TreeViewBackground";
      case TreeViewForeground -> "TreeViewForeground";
      case SelectedItemBackground -> "SelectedItemBackground";
      case SelectedItemForeground -> "SelectedItemForeground";
      case HoveredItemBackground -> "HoveredItemBackground";
      case InactiveSelectionBackground -> "InactiveSelectionBackground";
      case AddedResourceForeground -> "AddedResourceForeground";
      case DeletedResourceForeground -> "DeletedResourceForeground";
      case ModifiedResourceForeground -> "ModifiedResourceForeground";
      case IgnoredResourceForeground -> "IgnoredResourceForeground";
      case PanelHeaderBackground -> "PanelHeaderBackground";
      case PanelHeaderForeground -> "PanelHeaderForeground";
      case EditorBackground -> "EditorBackground";
      case EditorForeground -> "EditorForeground";
      case CurrentLineBorder -> "CurrentLineBorder";
      case CurrentLineBackground -> "CurrentLineBackground";
      case DeletedRegionBackground -> "DeletedRegionBackground";
      case DeletedTextBackground -> "DeletedTextBackground";
      case InsertedRegionBackground -> "InsertedRegionBackground";
      case InsertedTextBackground -> "InsertedTextBackground";
      case LineNumberForeground -> "LineNumberForeground";
      case ActiveLineNumberForeground -> "ActiveLineNumberForeground";
      case LineNumberActiveForeground -> "LineNumberActiveForeground";
      case SyntaxKeyword -> " SyntaxKeyword";
      case SyntaxNull -> " SyntaxNull";
      case SyntaxBoolean -> " SyntaxBoolean";
      case SyntaxSemi -> " SyntaxSemi";
      case SyntaxField -> " SyntaxField";
      case SyntaxString -> " SyntaxString";
      case SyntaxError -> " SyntaxError";
      case SyntaxNumeric -> " SyntaxNumeric";
      case SyntaxMethod -> " SyntaxMethod";
      case SyntaxComment -> " SyntaxComment";
      case SyntaxAnnotation -> " SyntaxAnnotation";
      case SyntaxType -> " SyntaxType";
      case SyntaxOperator -> " SyntaxOperator";
      case SyntaxEscapeChar -> " SyntaxEscapeChar";
      case SyntaxCppDirective -> " SyntaxCppDirective";
      case SyntaxDocumentation -> " SyntaxDocumentation";

      default -> "bad name " + n;
    };
  }
}
