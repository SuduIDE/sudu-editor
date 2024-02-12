package org.sudu.experiments.ui;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.window.ScrollContent;

import java.util.Objects;
import java.util.function.Consumer;

public class TreeView extends ScrollContent implements Focusable {

  static final float leftGapDp = 15;
  static final float treeShiftDp = 10;
  static final float shiftDp = 8;

  static final char cRArrow = '˃';
  static final char cDArrow = '˅';
  static final int arrowOffset = 2;

  final UiContext uiContext;
  final ClrContext clrContext;

  TreeModel model = new TreeModel();

  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  EditorColorScheme theme;
  UiFont uiFont;
  int firstLineRendered, lastLineRendered;
  int selectedLine = -1;

  GL.Texture arrowR, arrowD;

  public TreeView(UiContext uiContext) {
    this.uiContext = uiContext;
    clrContext = new ClrContext(uiContext.cleartype);
  }

  @Override
  public void dispose() {
    CodeLineRenderer.disposeLines(lines);
    clrContext.dispose();
    disposeArrows();
  }

  private void disposeArrows() {
    arrowR = Disposable.assign(arrowR, null);
    arrowD = Disposable.assign(arrowD, null);
  }

  public void setModel(TreeNode[] list) {
    model = new TreeModel(list);
    if (dpr != 0) updateVirtualSize();
  }

  public void setTheme(EditorColorScheme colors, UiFont newFont) {
    theme = colors;
    if (!Objects.equals(uiFont, newFont)) {
      uiFont = newFont;
      if (dpr != 0) {
        changeFont();
      }
    }
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    clrContext.setSinDpr(newDpr);
    changeFont();
  }

  private void changeFont() {
    CodeLineRenderer.makeContentDirty(lines);
    setFontInternal();
    updateVirtualSize();
  }

  private void updateVirtualSize() {
    setVirtualSize(0, model.lines.length * clrContext.lineHeight);
    layoutScroll();
  }

  @Override
  public void draw(WglGraphics g) {
    V4f bg = theme.editor.bg;
    g.drawRect(pos.x, pos.y, size, bg);
    Objects.requireNonNull(clrContext.font);
    int lineHeight = clrContext.lineHeight;

    int cacheLines = Numbers.iDivRoundUp(size.y, lineHeight) + EditorConst.MIN_CACHE_LINES;
    if (lines.length < cacheLines) {
      lines = CodeLineRenderer.allocRenderLines(
          cacheLines, lines, clrContext,
          firstLineRendered, lastLineRendered, model);
    }

    g.enableBlend(false);
    g.enableScissor(pos, size);

    int docLen = model.lines.length;

    int firstLine = getLine(scrollPos.y, lineHeight, docLen - 1);
    int lastLine = getLine(scrollPos.y + size.y - 1, lineHeight, docLen - 1);

    firstLineRendered = firstLine;
    lastLineRendered = lastLine;

    int width = size.x;
    int hScrollPos = 0;

    int leftGap = toPx(leftGapDp);
    int treeShift = toPx(treeShiftDp);
    int afterArrowShift = toPx(shiftDp);
    int arrowWidth = Math.max(arrowD.width(), arrowR.width());

    for (int i = firstLine; i <= lastLine; i++) {
      TreeNode mLine = model.lines[i];
      CodeLineRenderer line = lines[i % lines.length];

      line.updateTexture(mLine.line, g,
          lineHeight, width, hScrollPos, i, i % lines.length);

      int yPosition = lineHeight * i - scrollPos.y;

      LineDiff diff = null;
      int shift = leftGap + (treeShift + arrowWidth) * mLine.depth;

      boolean selected = selectedLine == i;
      if (selected) {
        int y = i * lineHeight - scrollPos.y;
        uiContext.v2i1.set(size.x, lineHeight);
        g.drawRect(pos.x, pos.y + y, uiContext.v2i1, theme.editor.currentLineBg);
      }

      var arrow = switch (mLine.arrow) {
        case TreeNode.allowRight -> arrowR;
        case TreeNode.allowDown -> arrowD;
        default -> null;
      };

      if (arrow != null) {
        var color = theme.codeElement[0];
        clrContext.tRegion.set(0, 0, arrow.width(), arrow.height());
        clrContext.size.set(arrow.size());
        clrContext.drawText(g, arrow,
            pos.x + shift,
            pos.y + yPosition,
            color.colorF,
            selected ? theme.editor.currentLineBg : bg);
      }

      line.draw(
          pos.y + yPosition,
          pos.x + shift + arrowWidth + afterArrowShift, g,
          width, lineHeight, hScrollPos,
          theme, null,
          null, null,
          selected, false,
          diff);
    }

    g.disableScissor();
  }

  @Override
  protected boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (button == MouseListener.MOUSE_BUTTON_LEFT && clickCount == 2) {
      int lineHeight = clrContext.lineHeight;
      int viewY = event.position.y - pos.y + scrollPos.y;
      int line = viewY / lineHeight;
      if (line >= 0 && line < model.lines.length) {
        TreeNode mLine = model.lines[line];
        if (!arrowClicked(event, line) && mLine.onDblClick != null) {
          mLine.onDblClick.run();
        }
      }
    }
    return true;
  }

  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    int lineHeight = clrContext.lineHeight;
    int viewY = event.position.y - pos.y + scrollPos.y;
    int line = viewY / lineHeight;
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      if (line >= 0 && line < model.lines.length) {
        TreeNode mLine = model.lines[line];
        if (arrowClicked(event, line)) {
          if (mLine.onClickArrow != null) mLine.onClickArrow.run();
        } else {
          selectedLine = line;
          if (mLine.onClick != null) mLine.onClick.run();
        }
      }
    }
    return MouseListener.Static.emptyConsumer;
  }

 // todo add keyboard processing
  @Override
  public boolean onKeyPress(KeyEvent event) {
    return false;
  }

  private boolean arrowClicked(MouseEvent event, int line) {
    TreeNode mLine = model.lines[line];
    int leftGap = toPx(leftGapDp);
    int treeShift = toPx(treeShiftDp);
    int arrowWidth = Math.max(arrowD.width(), arrowR.width());
    int shift = leftGap + (treeShift + arrowWidth) * mLine.depth;
    int x0 = pos.x - scrollPos.x + shift;
    int x1 = x0 + arrowWidth;
    return x0 <= event.position.x && event.position.x < x1;
  }

  private void setFontInternal() {
    clrContext.setFonts(uiFont, dpr, uiContext.graphics);
    clrContext.setLineHeight(EditorConst.LINE_HEIGHT_MULTI, uiContext.graphics);
    disposeArrows();
    arrowR = Disposable.assign(arrowR, clrContext.renderSmallString(
        String.valueOf(cRArrow), arrowOffset, uiContext.graphics));
    arrowD = Disposable.assign(arrowD, clrContext.renderSmallString(
        String.valueOf(cDArrow), arrowOffset, uiContext.graphics));
  }

  static int getLine(int y, int lineHeight, int maxLine) {
    return Math.min(y / lineHeight, maxLine);
  }

  static class TreeModel extends CodeLines {
    TreeNode[] lines;

    public TreeModel(TreeNode... lines) {
      this.lines = lines;
    }

    public CodeLine line(int i) { return lines[i].line; }
  }
}
