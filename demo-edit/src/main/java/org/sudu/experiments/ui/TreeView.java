package org.sudu.experiments.ui;

import org.sudu.experiments.Cursor;
import org.sudu.experiments.Disposable;
import org.sudu.experiments.GL;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.*;
import org.sudu.experiments.ui.fonts.Codicons;
import org.sudu.experiments.ui.window.ScrollContent;

import java.util.Objects;
import java.util.function.Consumer;

import static org.sudu.experiments.ui.fonts.Codicons.*;

public class TreeView extends ScrollContent implements Focusable {

  static final float treeShiftDp = 1;  // recursive treeShiftDp + arrowWidth
  static final float betweenIcons1 = 3;
  static final float betweenIcons2 = 5;
  static final float iconLift = 1;
  static final float selectionBackgroundMargin = 10;
  static final float leftGapDpDefault = 4;

  static final char cRArrow = '˃';
  static final char cDArrow = '˅';
  static final int iconTextureMargin = 1;

  final UiContext uiContext;
  final ClrContext clrContext;

  float leftGapDp = leftGapDpDefault;

  TreeModel model = new TreeModel();

  CodeLineRenderer[] lines = new CodeLineRenderer[0];
  EditorColorScheme theme;
  CodeLineColorScheme codeLineScheme;
  UiFont uiFont, uiIcons;
  int firstLineRendered, lastLineRendered;
  int selectedIndex = -1;
  int hoveredIndex = -1;
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

  public TreeNode selectedLine() {
    return selectedIndex < 0 ? null : model.lines[selectedIndex];
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

  void setModel(TreeModel model) {
    TreeNode selectedLine = selectedLine();
    this.model = model;

    boolean keepSelectedIndex =
        selectedLine != null && selectedLine.isEmpty();

    if (keepSelectedIndex)
      selectedIndex = Math.min(selectedIndex, lines.length - 1);
    else
      selectedIndex = model.indexOf(selectedLine);

    if (dpr != 0) layout();
  }

  public TreeNode[] model() {
    return model.lines;
  }

  public FolderDiffModel[] diffModel() {
    return model.models;
  }

  public void setTheme(EditorColorScheme colors) {
    theme = colors;
    codeLineScheme = colors.treeViewCodeLineScheme();
    boolean sameFont1 = Objects.equals(uiFont, colors.fileViewFont);
    boolean sameFont2 = Objects.equals(uiIcons, colors.fileViewIcons);
    if (!sameFont1 || !sameFont2) {
      uiFont = colors.treeViewFont;
      uiIcons = colors.fileViewIcons;
      if (dpr != 0)
        changeFont();
    }
  }

  @Override
  protected void onDprChange(float olDpr, float newDpr) {
    clrContext.setSinDpr(newDpr);
    if (uiFont != null)
      changeFont();
  }

  protected void changeFont() {
    for (TreeNode treeNode : model.lines)
      treeNode.line.invalidateCache();
    CodeLineRenderer.disposeLines(lines);
    clrContext.setFonts(uiFont, dpr, uiContext.graphics);
    clrContext.setLineHeight(EditorConst.LINE_HEIGHT_MULTI, uiContext.graphics);
    disposeIcons();
    loadIcons();
    layout();
  }

  void layout() {
    setVirtualSize(virtualSize.x,
        model.lines.length * clrContext.lineHeight);
    layoutScroll();
  }

  public void clearSelection() {
    if (selectedIndex < 0) return;
    selectedIndex = -1;
    onSelectedLineChanged(selectedIndex);
  }

  public void setSelected0() {
    selectedIndex = model.lines.length > 0 ? 0 : -1;
  }

  public void setSelected(TreeNode l) {
    selectedIndex = model.indexOf(l);
  }

  public int selectedIndex() {
    return selectedIndex;
  }

  @Override
  public void draw(WglGraphics g) {
    var bg = theme.fileTreeView.bg;
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

      var bgLineColor = theme.fileTreeView.bg; // diff.getDiffColor(theme, diffType);
      clrContext.ld.type = diffType;
      int shift = leftGap + treeShift * mLine.depth;

      boolean selected = selectedIndex == i;
      boolean hovered = hoveredIndex == i;
      if (diffType != DiffTypes.DEFAULT) {
        int y = i * lineHeight - scrollPos.y;
        uiContext.v2i1.set(size.x, lineHeight);
        g.drawRect(pos.x, pos.y + y, uiContext.v2i1, bgLineColor);
      }

      var background = selected ?
          hasFocus ?
              theme.fileTreeView.selectedBg :
              theme.fileTreeView.inactiveSelectedBg
          : hovered ? theme.hoverColors.bgColor : bg;

      var foreground = selected && hasFocus ?
          theme.fileTreeView.selectedText :
          theme.fileTreeView.textDiffColors.getDiffColor(
              diffType, theme.fileTreeView.textColor);

      if (selected || hovered) {
        int y = i * lineHeight - scrollPos.y;
        int indent = toPx(selectionBackgroundMargin);
        uiContext.v2i1.set(size.x - indent, lineHeight);
        g.drawRect(pos.x, pos.y + y,
            uiContext.v2i1, background);
      }

      var arrow = getIcon(mLine.arrow);
      var icon = getIcon(mLine.icon);

      if (arrow != null) {
        int arrowX = startX + shift;
        clrContext.drawIcon(g, arrow,
            arrowX,
            pos.y + yPosition,
            background,
            foreground);
      }

      if (icon != null) {
        int iconX = startX + shift + arrowWidth + iconMargin1Px;
        clrContext.drawIcon(g, icon,
            iconX,
            pos.y + yPosition,
            background,
            foreground);
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
          codeLineScheme, null,
          null, null,
          selected,
          background, foreground, null);
    }

    if (virtualSize.x != virtualSizeX) {
      virtualSize.x = virtualSizeX;
      layoutScroll();
    }
    g.disableScissor();
  }

  private int getLineNumber(MouseEvent event) {
    return getLineNumber(event.position);
  }

  private int getLineNumber(V2i position) {
    int lineHeight = clrContext.lineHeight;
    int viewY = position.y - pos.y + scrollPos.y;
    return viewY / lineHeight;
  }

  public void onMouseLeaveWindow() {
    hoveredIndex = -1;
  }

  @Override
  public void onMouseMove(MouseEvent event, SetCursor setCursor) {
    boolean hit = hitTest(event.position);
    if (!hit) {
      if (hoveredIndex >= 0) hoveredIndex = -1;
      return;
    }
    int line = getLineNumber(event);
    if (line >= 0 && line < model.lines.length) {
      hoveredIndex = line;
      setCursor.set(Cursor.pointer);
    } else {
      hoveredIndex = -1;
      setCursor.setDefault();
    }
  }

  @Override
  protected boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    if (button == MouseListener.MOUSE_BUTTON_LEFT && clickCount == 2) {
      int line = getLineNumber(event);
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

  public boolean isFocused() {
    return uiContext.isFocused(this);
  }

  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    focus();
    int line = getLineNumber(event);
    if (button == MouseListener.MOUSE_BUTTON_LEFT) {
      if (line >= 0 && line < model.lines.length) {
        TreeNode mLine = model.lines[line];
        if (arrowClicked(event, line)) {
          if (mLine.onClickArrow != null) mLine.onClickArrow.run();
        } else {
          selectedIndex = line;
          onSelectedLineChanged(line);
          if (mLine.onClick != null) mLine.onClick.run();
        }
      }
    } else if (button == MouseListener.MOUSE_BUTTON_RIGHT) {
      if (line >= 0 && line < model.lines.length) {
        selectedIndex = line;
        onSelectedLineChanged(line);
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
    FolderDiffModel[] models;

    public TreeModel() {
      this(new TreeNode[]{}, new FolderDiffModel[]{});
    }

    public TreeModel(TreeNode[] lines, FolderDiffModel[] models) {
      this.lines = lines;
      this.models = models;
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
      case KeyCode.PAGE_UP -> event.ctrl && ++leftGapDp > 0;
      case KeyCode.PAGE_DOWN -> event.ctrl && --leftGapDp > 0;
      default -> false;
    };
  }

  private boolean enterSelected() {
    if (selectedIndex >= 0) {
      var r = selectedLine().onEnter();
      if (r != null)
        r.run();
    }
    return true;
  }

  private boolean closeIfOpenedElseMoveToParent() {
    TreeNode selectedLine = selectedLine();
    if (selectedLine != null && selectedLine.isOpened()) {
      if (selectedLine.onClickArrow != null)
        selectedLine.onClickArrow.run();
      return true;
    } else {
      return moveToParent();
    }
  }

  private boolean openIfClosedElseMoveDown() {
    TreeNode selectedLine = selectedLine();
    if (selectedLine != null && selectedLine.isClosed()) {
      if (selectedLine.onClickArrow != null)
        selectedLine.onClickArrow.run();
      return true;
    } else {
      return moveDown();
    }
  }

  private boolean moveToParent() {
    int idx = selectedIndex;
    if (idx < 0) return false;
    int parentDepth = model.lines[idx].depth - 1;
    if (parentDepth < 0) return false;
    for (idx--; idx >= 0; idx--) {
      if(model.lines[idx].depth == parentDepth) {
        selectedIndex = idx;
        checkScroll(idx);
        onSelectedLineChanged(idx);
        return true;
      }
    }
    return false;
  }

  private boolean moveUp() {
    int idx = selectedIndex - 1;
    if (idx < 0) return false;
    selectedIndex = idx;
    checkScroll(idx);
    onSelectedLineChanged(idx);
    return true;
  }

  private boolean moveDown() {
    int idx = selectedIndex + 1;
    if (idx >= model.lines.length) return false;
    selectedIndex = idx;
    checkScroll(idx);
    onSelectedLineChanged(idx);
    return true;
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
    if (selectedLine() != null) {
      setText.accept(selectedLine().value());
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

  public boolean onContextMenu(V2i pos) {
    int line = getLineNumber(pos);
    if (line >= 0 && line < model.lines.length && line != selectedIndex) {
      selectedIndex = line;
      hoveredIndex = line;
      onSelectedLineChanged(line);
    }
    return false;
  }

  public void setSelectedIndex(int selectedIndex) {
    this.selectedIndex = selectedIndex;
  }
}
