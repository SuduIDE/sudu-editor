package org.sudu.experiments.ui;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V4f;
import org.sudu.experiments.ui.fonts.Codicons;
import org.sudu.experiments.ui.window.ScrollContent;

import java.util.Objects;
import java.util.function.Consumer;

import static org.sudu.experiments.ui.fonts.Codicons.*;

public class TreeView extends ScrollContent implements Focusable {

  static final float leftGapDp = 4;
  static final float treeShiftDp = 1;  // recursive treeShiftDp + arrowWidth
  static final float betweenIcons1 = 3;
  static final float betweenIcons2 = 5;
  static final float iconLift = 1;

  static final char cRArrow = '˃';
  static final char cDArrow = '˅';
  static final int iconTextureMargin = 1;

  final UiContext uiContext;
  final ClrContext clrContext;

  TreeModel model = new TreeModel();

  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  EditorColorScheme theme;
  UiFont uiFont, uiIcons;
  int firstLineRendered, lastLineRendered;
  TreeNode selectedLine;

  GL.Texture arrowR, arrowD;
  GL.Texture folder, folderOpened;
  GL.Texture file, fileCode, fileBinary;
  int iconWidth, arrowWidth;

  public TreeView(UiContext uiContext) {
    this.uiContext = uiContext;
    clrContext = new ClrContext(uiContext.cleartype);
  }

  @Override
  public void dispose() {
    CodeLineRenderer.disposeLines(lines);
    clrContext.dispose();
    disposeIcons();
  }

  private void loadIcons() {
    FontDesk iconsFont = uiContext.fontDesk(uiIcons);
    arrowR = Disposable.assign(arrowR, renderIcon(chevron_right, iconsFont));
    arrowD = Disposable.assign(arrowD, renderIcon(chevron_down, iconsFont));
    folder = Disposable.assign(folder, renderIcon(Codicons.folder, iconsFont));
    folderOpened = Disposable.assign(folderOpened, renderIcon(folder_opened, iconsFont));
    file = Disposable.assign(file, renderIcon(Codicons.file, iconsFont));
    fileCode = Disposable.assign(fileCode, renderIcon(file_code, iconsFont));
    fileBinary = Disposable.assign(fileBinary, renderIcon(file_binary, iconsFont));
    iconWidth = Math.max(Math.max(Math.max(Math.max(
        folder.width(),
        folderOpened.width()),
        file.width()),
        fileBinary.width()),
        fileBinary.width());

    arrowWidth = Math.max(arrowD.width(), arrowR.width());
  }

  private void disposeIcons() {
    arrowR = Disposable.assign(arrowR, null);
    arrowD = Disposable.assign(arrowD, null);
    folder = Disposable.assign(folder, null);
    folderOpened = Disposable.assign(folderOpened, null);
    file = Disposable.assign(file, null);
    fileCode = Disposable.assign(fileCode, null);
    fileBinary = Disposable.assign(fileBinary, null);
  }

  private GL.Texture getIcon(int arrow) {
    return switch (arrow) {
      case Codicons.chevron_right -> arrowR;
      case Codicons.chevron_down -> arrowD;
      case Codicons.folder -> folder;
      case Codicons.folder_opened -> folderOpened;
      case Codicons.file -> file;
      case Codicons.file_code -> fileCode;
      case Codicons.file_binary -> fileBinary;
      default -> null;
    };
  }

  public void setModel(TreeNode[] list) {
    model = new TreeModel(list);
    if (dpr != 0) updateVirtualHeight();
  }

  public void setTheme(EditorColorScheme colors) {
    theme = colors;
    boolean sameFont1 = Objects.equals(uiFont, colors.fileViewFont);
    boolean sameFont2 = Objects.equals(uiIcons, colors.fileViewIcons);
    if (!sameFont1 || !sameFont2) {
      uiFont = colors.fileViewFont;
      uiIcons = colors.fileViewIcons;
      if (dpr != 0) {
        changeFont();
      }
    }
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    clrContext.setSinDpr(newDpr);
    if (uiFont != null) changeFont();
  }

  private void changeFont() {
    CodeLineRenderer.makeContentDirty(lines);
    setFontInternal();
    updateVirtualHeight();
  }

  private void updateVirtualHeight() {
    setVirtualSize(virtualSize.x,
        model.lines.length * clrContext.lineHeight);
    layoutScroll();
  }


  public void setSelected(TreeNode l) {
    selectedLine = l;
  }

  @Override
  public void draw(WglGraphics g) {
    V4f bg = theme.editor.bg;
    g.drawRect(pos.x, pos.y, size, bg);
    Objects.requireNonNull(clrContext.font);
    int lineHeight = clrContext.lineHeight;
    int docLen = model.lines.length;
    if (docLen == 0) return;

    int cacheLines = Math.min(docLen,
        Numbers.iDivRoundUp(size.y, lineHeight) + EditorConst.MIN_CACHE_LINES);
    if (lines.length < cacheLines) {
      lines = CodeLineRenderer.allocRenderLines(
          cacheLines, lines, clrContext,
          firstLineRendered, lastLineRendered, model);
    }

    g.enableScissor(pos, size);

    int firstLine = getLine(scrollPos.y, lineHeight, docLen - 1);
    int lastLine = getLine(scrollPos.y + size.y - 1, lineHeight, docLen - 1);

    firstLineRendered = firstLine;
    lastLineRendered = lastLine;

    int width = size.x;
    int hScrollPos = 0;

    int leftGap = toPx(leftGapDp);
    int treeShift = toPx(treeShiftDp) + arrowWidth;
    int iconMargin1Px = toPx(betweenIcons1);
    int iconMargin2Px = toPx(betweenIcons2);
    int iconLiftPx = toPx(iconLift);

    int virtualSizeX = 0;
    int startX = pos.x - scrollPos.x;
    int scrollW = toPx(1) +
        (scrollView != null ? scrollView.scrollWidthPx() : 0);

    for (int i = firstLine; i <= lastLine; i++) {
      TreeNode mLine = model.lines[i];
      CodeLineRenderer line = lines[i % lines.length];

      line.updateTexture(mLine.line, g,
          lineHeight, width, hScrollPos, i, i % lines.length);

      int yPosition = lineHeight * i - scrollPos.y;

      LineDiff diff = null;
      int shift = leftGap + treeShift * mLine.depth;

      boolean selected = selectedLine == mLine;
      if (selected) {
        int y = i * lineHeight - scrollPos.y;
        uiContext.v2i1.set(size.x, lineHeight);
        g.drawRect(pos.x, pos.y + y, uiContext.v2i1, theme.editor.currentLineBg);
      }

      var arrow = getIcon(mLine.arrow);
      var icon = getIcon(mLine.icon);

      if (arrow != null) {
        var color = theme.codeElement[0];
        int arrowX = startX + shift;
        drawIcon(g, arrow,
            arrowX,
            pos.y + yPosition - iconLiftPx,
            selected ? theme.editor.currentLineBg : bg,
            color.colorF);
      }

      if (icon != null) {
        var color = theme.codeElement[0];
        int iconX = startX + shift + arrowWidth + iconMargin1Px;
        drawIcon(g, icon,
            iconX,
            pos.y + yPosition - iconLiftPx,
            selected ? theme.editor.currentLineBg : bg,
            color.colorF);
      }

      int lineMeasure = mLine.line.lineMeasure();
      int textShift = shift + arrowWidth + iconMargin1Px
          + iconWidth + iconMargin2Px;
      virtualSizeX = Math.max(virtualSizeX,
          textShift + lineMeasure + scrollW);

      line.draw(
          pos.y + yPosition,
          startX + textShift,
          g, width, lineHeight, hScrollPos,
          theme, null,
          null, null,
          selected, false,
          diff);
    }

    if (virtualSize.x != virtualSizeX) {
      virtualSize.x = virtualSizeX;
      layoutScroll();
    }
    g.disableScissor();
  }

  private void drawIcon(
      WglGraphics g, GL.Texture icon,
      int xPos, int yPos, V4f bgColor, V4f colorF
  ) {
    clrContext.tRegion.set(0, 0, icon.width(), icon.height());
    clrContext.size.set(icon.size());
    clrContext.drawText(g, icon, xPos, yPos,
        colorF, bgColor);
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
          selectedLine = mLine;
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
    disposeIcons();
    loadIcons();
  }

  private GL.Texture renderIcon(char icon, FontDesk font) {
    return uiContext.graphics.renderTexture(
        String.valueOf(icon), font, iconTextureMargin,
        clrContext.lineHeight, clrContext.cleartype);
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
