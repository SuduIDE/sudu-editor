package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.editor.EditorConst;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.ColorOp;
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

  // window title
  public static final int PanelHeaderBackground = 9;
  public static final int PanelHeaderForeground = 10;

  // editor
  public static final int EditorBackground = 11;
  public static final int EditorForeground = 12;
  public static final int CurrentLineBackground = 13;

  // editor diff
  public static final int DeletedRegionBackground = 14;
  public static final int DeletedTextBackground = 15;
  public static final int InsertedRegionBackground = 16;
  public static final int InsertedTextBackground = 17;

  public static final int LastIndex = InsertedTextBackground + 1;

  public final EditorColors editor;
  public final FileTreeViewTheme fileTreeView;
  public final DialogItemColors dialogItem;
  public final CodeElementColor[] codeElement;
  public final LineNumbersColors lineNumber;
  public final DiffColors codeDiffBg;
  public BackgroundHoverColors hoverColors;

  private static final float defaultFontSize = 15;
  private static final float defaultMenuFontSize = 17;
  private static final String defaultFont = Fonts.SegoeUI;
  private static final String defaultUsagesFont = Fonts.Consolas;

  public final UiFont popupMenuFont;
  public final UiFont usagesFont;
  public final UiFont fileViewFont;
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
        DiffColors.codeDiffDarcula());
  }

  public static EditorColorScheme darkIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.darkIdeaColorScheme(),
        FileTreeViewTheme.darkIdea(),
        IdeaCodeColors.codeElementColorsDark(),
        LineNumbersColors.dark(),
        Themes.darkColorScheme(),
        DiffColors.codeDiffDark());
  }

  public static EditorColorScheme lightIdeaColorScheme() {
    return new EditorColorScheme(
        EditorColors.lightIdeaColorScheme(),
        FileTreeViewTheme.lightIdea(),
        IdeaCodeColors.codeElementColorsLight(),
        LineNumbersColors.light(),
        Themes.lightColorScheme(),
        DiffColors.codeDiffLight()
    );
  }

  private EditorColorScheme(
      EditorColors editor, FileTreeViewTheme fileTreeView,
      CodeElementColor[] codeElement,
      LineNumbersColors lineNumber,
      DialogItemColors dialogItem,
      DiffColors codeDiffBg
  ) {
    this(editor, fileTreeView, codeElement, lineNumber, dialogItem,
        codeDiffBg,
        new UiFont(defaultFont, defaultMenuFontSize),
        new UiFont(defaultUsagesFont, defaultFontSize),
        new UiFont(defaultFont, defaultFontSize),
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
      UiFont popupMenuFont,
      UiFont usagesFont,
      UiFont fileViewFont,
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
    this.popupMenuFont = popupMenuFont;
    this.usagesFont = usagesFont;
    this.fileViewFont = fileViewFont;
    this.fileViewIcons = fileViewIcons;
    this.editorFont = editorFont;
    this.treeViewFont = treeViewFont;
    recomputeHoverColors();
  }

  public EditorColorScheme withFontModified(float fontSize) {
    return new EditorColorScheme(
        editor, fileTreeView, codeElement,
        lineNumber, dialogItem, codeDiffBg,
        popupMenuFont.withSize(fontSize),
        usagesFont.withSize(fontSize),
        fileViewFont.withSize(fontSize),
        fileViewIcons.withSize(fontSize),
        editorFont.withSize(fontSize),
        treeViewFont.withSize(fontSize));
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

  public void modify(int m, Color c) {
    switch (m) {
      case TreeViewBackground -> {
        fileTreeView.bg = c;
        recomputeHoverColors();
      }
      case TreeViewForeground ->
          fileTreeView.textColor = c;

      case SelectedItemBackground -> {
        fileTreeView.selectedBg = c;
        recomputeHoverColors();
      }
      case SelectedItemForeground ->
          fileTreeView.selectedText =c;

      case HoveredItemBackground -> {
        fileTreeView.hoveredBg = c;
        recomputeHoverColors();
      }
      case InactiveSelectionBackground ->
          fileTreeView.inactiveSelectedBg = c;

      case CurrentLineBackground ->
          editor.currentLineBg = c;

      case EditorBackground -> {
        editor.bg = c;
        lineNumber.bgColor = c;
      }

      case EditorForeground ->
        codeElement[0].colorF = c;

      case PanelHeaderBackground -> {
        System.out.println("PanelHeaderBackground set to " + c);
        dialogItem.windowColors.windowTitleBgColor = c;
      }
      case PanelHeaderForeground -> {
        System.out.println("PanelHeaderForeground set to " + c);
        dialogItem.windowColors.windowTitleTextColor = c;
      }

      case DeletedRegionBackground -> {}
      case DeletedTextBackground -> {}
      case InsertedRegionBackground -> {}
      case InsertedTextBackground -> {}

      case AddedResourceForeground ->
        fileTreeView.textDiffColors.insertedColor = c;

      case DeletedResourceForeground ->
          fileTreeView.textDiffColors.deletedColor = c;

      case ModifiedResourceForeground ->
          fileTreeView.textDiffColors.editedColor = c;

//      case DeletedRegionBackground -> {
//        System.out.println("DeletedRegionBackground = " + c);
//        diff.deletedBgColor = ColorOp.blend(
//            editor.bg, c);
      //  recomputeHoverColors();
//      }

//      case DeletedTextBackground -> {
//        System.out.println("DeletedRegionBackground = " + c);
//        diff.deletedBgColor = ColorOp.blend(
//            editor.bg, c);
//      }
    }
  }

  public MergeButtonsColors codeDiffMergeButtons() {
    return new MergeButtonsColors(
        null, codeDiffBg,
        lineNumber.caretTextColor,
        lineNumber.bgColor,
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
}
