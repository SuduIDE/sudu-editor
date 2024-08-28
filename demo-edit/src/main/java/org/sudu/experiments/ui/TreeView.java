package org.sudu.experiments.ui;

import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
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
  static final float selectionBackgroundMargin = 10;

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
  Consumer<Integer> onSelectedLineChanged;

  GL.Texture arrowR, arrowD;
  GL.Texture folder, folderOpened;
  GL.Texture file, fileCode, fileBinary, fileOnWorker;
  int iconWidth, arrowWidth;
  boolean hasFocus;

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
    fileOnWorker = Disposable.assign(fileOnWorker, renderIcon(refresh, iconsFont));

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
    fileOnWorker = Disposable.assign(fileOnWorker, null);
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
      case Codicons.refresh -> fileOnWorker;
      default -> null;
    };
  }

  public void setModel(TreeNode[] lines) {
    if (selectedLine != null && selectedLine.isEmpty()) {
      int ind = this.model.indexOf(selectedLine);
      if (ind != -1) selectedLine = lines[ind];
      this.model = new TreeModel(lines);
    } else {
      this.model = new TreeModel(lines);
      if (!model.contains(selectedLine)) selectedLine = null;
    }
    if (dpr != 0) updateVirtualHeight();
  }

  public TreeNode[] model() {
    return model.lines;
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

  public void setSelected0() {
    selectedLine = model.lines.length > 0 ? model.lines[0] : null;
  }

  public void setSelected(TreeNode l) {
    selectedLine = l;
  }

  public int getSelectedInd() {
    var model = model();
    for (int i = 0; i < model.length; i++)
      if (model[i] == selectedLine) return i;
    return -1;
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
      var diffType = mLine.diffType;

      int yPosition = lineHeight * i - scrollPos.y;

      LineDiff diff = diffType != DiffTypes.DEFAULT
          ? clrContext.ld.seType(DiffTypes.FOLDER_ALIGN_DIFF_TYPE) : null;
      var bgLineColor = diff == null ? null :
          theme.diff.getDiffColor(theme, diff.type);
      int shift = leftGap + treeShift * mLine.depth;

      boolean selected = hasFocus && selectedLine == mLine;
      if (diff != null) {
        int y = i * lineHeight - scrollPos.y;
        uiContext.v2i1.set(size.x, lineHeight);
        g.drawRect(pos.x, pos.y + y, uiContext.v2i1, bgLineColor);
      }
      if (selected) {
        int y = i * lineHeight - scrollPos.y;
        int indent = toPx(selectionBackgroundMargin);
        uiContext.v2i1.set(size.x - indent * 2, lineHeight);
        g.drawRect(pos.x + indent, pos.y + y,
            uiContext.v2i1, theme.editor.currentLineBg);
      }

      var arrow = getIcon(mLine.arrow);
      var icon = getIcon(mLine.icon);

      if (arrow != null) {
        var color = theme.codeElement[0];
        int arrowX = startX + shift;
        clrContext.drawIcon(g, arrow,
            arrowX,
            pos.y + yPosition,
            selected ? theme.editor.currentLineBg :
                diff != null ? bgLineColor : bg,
            color.colorF);
      }

      if (icon != null) {
        var color = theme.codeElement[0];
        int iconX = startX + shift + arrowWidth + iconMargin1Px;
        clrContext.drawIcon(g, icon,
            iconX,
            pos.y + yPosition,
            selected ? theme.editor.currentLineBg :
                diff != null ? bgLineColor : bg,
            color.colorF);
      }

      CodeLine cl = mLine.line;
      if (cl.totalStrLength == 0)
        continue;

      CodeLineRenderer line = lines[i % lines.length];
      int lineMeasure = line.updateTexture(cl, g,
          lineHeight, width, hScrollPos, i, i % lines.length);

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
          selected,
          selected ? null : diff);
    }

    if (virtualSize.x != virtualSizeX) {
      virtualSize.x = virtualSizeX;
      layoutScroll();
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

  public void focus() {
    uiContext.setFocus(this);
  }

  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    focus();
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
          onSelectedLineChanged(line);
          if (mLine.onClick != null) mLine.onClick.run();
        }
      }
    }
    return MouseListener.Static.emptyConsumer;
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
        clrContext.lineHeight, 0, clrContext.cleartype);
  }

  static int getLine(int y, int lineHeight, int maxLine) {
    return Math.min(y / lineHeight, maxLine);
  }

  static class TreeModel extends CodeLines {
    TreeNode[] lines;

    public TreeModel() {
      this(new TreeNode[]{});
    }

    public TreeModel(TreeNode[] lines) {
      this.lines = lines;
    }

    public CodeLine line(int i) { return lines[i].line; }

    public boolean contains(TreeNode n) {
      return indexOf(n) >= 0;
    }

    public int indexOf(TreeNode n) {
      if (n == null) return -1;
      for (int i = 0; i < lines.length; i++) {
        if (lines[i] == n) return i;
      }
      return -1;
    }
  }

  // focusable
  @Override
  public boolean onKeyPress(KeyEvent event) {
    return switch (event.keyCode) {
      case KeyCode.ARROW_DOWN -> moveDown();
      case KeyCode.ARROW_UP -> moveUp();
      case KeyCode.ARROW_RIGHT -> openIfClosedElseMoveDown();
      case KeyCode.ARROW_LEFT -> closeIfOpenedElseMoveToParent();
      case KeyCode.ENTER -> enterSelected();
      default -> false;
    };
  }

  private boolean enterSelected() {
    if (selectedLine != null) {
      var r = selectedLine.onEnter();
      if (r != null)
        r.run();
    }
    return true;
  }

  private boolean closeIfOpenedElseMoveToParent() {
    if (selectedLine != null && selectedLine.isOpened()){
      if(selectedLine.onClickArrow != null)
        selectedLine.onClickArrow.run();
      return true;
    } else {
      return moveToParent();
    }
  }

  private boolean openIfClosedElseMoveDown() {
    if (selectedLine != null && selectedLine.isClosed()) {
      if (selectedLine.onClickArrow != null)
        selectedLine.onClickArrow.run();
      return true;
    } else {
      return moveDown();
    }
  }

  private boolean moveToParent() {
    int idx = selectedIndex();
    if(idx < 0) return false;
    int parentDepth = model.lines[idx].depth - 1;
    if(parentDepth < 0) return false;
    for(idx--; idx >= 0; idx--){
      if(model.lines[idx].depth == parentDepth) {
        selectedLine = model.lines[idx];
        checkScroll(idx);
        return true;
      }
    }
    return false;
  }

  private boolean moveUp() {
    int idx = selectedIndex() - 1;
    if(idx < 0) return false;
    selectedLine = model.lines[idx];
    checkScroll(idx);
    onSelectedLineChanged(idx);
    return true;
  }

  private boolean moveDown() {
    int idx = selectedIndex() + 1;
    if(idx >= model.lines.length) return false;
    selectedLine = model.lines[idx];
    checkScroll(idx);
    onSelectedLineChanged(idx);
    return true;
  }

  private int selectedIndex() {
    TreeNode[] lines = model.lines;
    TreeNode sLine = selectedLine;
    for (int i = 0, l = lines.length; i < l; i++) {
      if (sLine == lines[i]) return i;
    }
    return -1;
  }

  public void checkScroll(int index) {
    int lineHeight = clrContext.lineHeight;
    int y = index * lineHeight;
    if (y < scrollPos.y) {
      setScrollPosY(y);
      layoutScroll();
    }
    if (y + lineHeight > scrollPos.y + size.y) {
      setScrollPosY(y + lineHeight - size.y);
      layoutScroll();
    }
  }

  public void setOnSelectedLineChanged(Consumer<Integer> onSelectedLineChanged) {
    this.onSelectedLineChanged = onSelectedLineChanged;
  }

  private void onSelectedLineChanged(int newInd) {
    if (onSelectedLineChanged != null) onSelectedLineChanged.accept(newInd);
  }

  @Override
  public boolean onCopy(Consumer<String> setText, boolean isCut) {
    if (selectedLine != null) {
      setText.accept(selectedLine.value());
      return true;
    }
    return false;
  }

  @Override
  public void onFocusGain() {
    Focusable.super.onFocusGain();
    hasFocus = true;
  }

  @Override
  public void onFocusLost() {
    Focusable.super.onFocusLost();
    hasFocus = false;
  }
}
