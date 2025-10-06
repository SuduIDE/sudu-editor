package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.ui.Focusable;
import org.sudu.experiments.ui.ScrollBar;
import org.sudu.experiments.ui.UiContext;
import org.sudu.experiments.ui.window.View;

import java.util.Objects;

public class InlineDiffView extends View implements Focusable
{
  final UiContext context;
  final ClrContext lrContext;
  final Caret caret = new Caret();
  final ScrollBar vScroll = new ScrollBar();
  final ScrollBar hScroll = new ScrollBar();
  EditorColorScheme colors;
  CodeLineColorScheme codeLineColors;
  float fontVirtualSize = EditorConst.DEFAULT_FONT_SIZE;
  String fontFamilyName = EditorConst.FONT;
  boolean hasFocus;

  // render cache
  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  int firstLineRendered, lastLineRendered;

  public InlineDiffView(UiContext uiContext) {
    context = uiContext;
    lrContext = new ClrContext(uiContext.cleartype);
  }

  public void setTheme(EditorColorScheme theme) {
    colors = theme;
    codeLineColors = colors.editorCodeLineScheme();
    caret.setColor(theme.editor.cursor);
    vScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    hScroll.setColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    if (!theme.editorFont.equals(fontFamilyName, fontVirtualSize)) {
      changeFont(theme.editorFont.familyName, theme.editorFont.size);
    }
//    if (codeMap != null)
//      buildDiffMap();
  }


  @Override
  public boolean onKeyPress(KeyEvent event) {
    return false;
  }

  public void changeFont(String name, float virtualSize) {
    if (context.dpr != 0) {
      doChangeFont(name, virtualSize);
      context.window.repaint();
    }
    fontVirtualSize = virtualSize;
    fontFamilyName = name;
  }

  private void doChangeFont(String name, float virtualSize) {
    float newPixelFontSize = virtualSize * dpr;
    float oldPixelFontSize = lrContext.fontSize();
    if (newPixelFontSize != oldPixelFontSize || !Objects.equals(name, fontFamilyName)) {
      lineNumbers1.dispose();
      lineNumbers2.dispose();
      invalidateFont();
      setFont(name, newPixelFontSize);
      recomputeCaretPosY();
      updateLineNumbersFont();
      internalLayout();
      adjustEditorScrollToCaret();
    }
  }

  private void updateLineNumbersFont() {
    lineNumbers.setFont(lrContext.font, lrContext.lineHeight, context.cleartype);
  }

  private void invalidateFont() {
//    Debug.consoleInfo("invalidateFont");

    CodeLineRenderer.disposeLines(lines);
    model1.document.invalidateFont();
    model2.document.invalidateFont();
  }

  private void setFont(String name, float pixelSize) {
    lrContext.setFonts(name, pixelSize, context.graphics);
    lrContext.setLineHeight(EditorConst.LINE_HEIGHT_MULTI, context.graphics);
    caret.setHeight(lrContext.font.caretHeight(lrContext.lineHeight));

    Debug.consoleInfo("editor font: " + name + " " + pixelSize
//        + ", ascent+descent = " + lrContext.font.lineHeight()
            + ", lineHeight = " + lrContext.lineHeight
        /* + ", caretHeight = " + caret.height() */ );

    if (CodeLineRenderer.useTop) {
      Debug.consoleInfo("font.topBase(lineHeight) = "
          + lrContext.font.topBase(lrContext.lineHeight));
    }
  }

}
