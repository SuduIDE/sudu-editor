package org.sudu.experiments.ui;

import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.diff.folder.FolderDiffModel;
import org.sudu.experiments.diff.folder.ModelFilter;
import org.sudu.experiments.editor.MergeButtons;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.ui.window.ScrollView;

import java.util.function.Consumer;

public class FileTreeView extends TreeView {
  FileTreeNode root;
  MergeButtons mergeButtons;
  int mbWidth;
  boolean isLeft;

  public FileTreeView(UiContext uiContext) {
    super(uiContext);
  }

  @Override
  public void dispose() {
    super.dispose();
    if (mergeButtons != null) {
      mergeButtons.dispose();
      mergeButtons = null;
    }
  }

  public void updateModel() {
    updateModel(FolderDiffModel.DEFAULT, null, ModelFilter.NO_FILTER);
  }

  public void updateModel(FolderDiffModel model, int filter) {
    updateModel(model, null, filter);
  }

  public void updateModel(FolderDiffModel model, FileTreeNode another, int filter) {
    setModel(root.getModel(model, another, filter));
  }

  int scrollWidth() {
    return scrollView == null ? 0 : scrollView.scrollWidthPx();
  }

  public void enableMergeButtons(Runnable[] actions, int[] lines, byte[] colors, boolean left) {
    isLeft = left;
    if (mergeButtons == null) {
      mergeButtons = new MergeButtons();
      if (dpr != 0) {
        applyMergeButtonsFont();
      }
    }
    mergeButtons.setModel(actions, lines);
    mergeButtons.setColors(colors);
  }

  public void setRoot(FileTreeNode root) {
    this.root = root;
    updateModel();
    setSelected0();
  }

  public ScrollView applyTheme(ScrollView view) {
    view.setScrollColor(theme.editor.scrollBarLine, theme.editor.scrollBarBg);
    return view;
  }

  @Override
  protected void changeFont() {
    System.out.println("FileTreeView.changeFont");
    super.changeFont();
    if (mergeButtons != null)
      applyMergeButtonsFont();
  }

  private void applyMergeButtonsFont() {
    mergeButtons.setFont(clrContext.lineHeight, !isLeft, clrContext.font);
    mbWidth = mergeButtons.measure(clrContext.font, uiContext.graphics.mCanvas, dpr);
    leftGapDp = isLeft ? leftGapDpDefault : toDp(mbWidth);

    System.out.println("mergeButtons.measure = " + mbWidth);
  }

  @Override
  public void draw(WglGraphics g) {
    super.draw(g);
    if (mergeButtons != null) {
      if (mbWidth == 0)
        throw new IllegalStateException("draw: mbWidth == 0");
      mergeButtons.setScrollPos(scrollPos.y);
      this.layoutScroll();
      int xPos = isLeft ? pos.x + size.x - scrollWidth() - mbWidth : pos.x;
      mergeButtons.setPosition(xPos, pos.y, mbWidth, size.y, dpr);
      mergeButtons.draw(
          firstLineRendered, lastLineRendered, selectedIndex,
          g, theme, clrContext);
    }
  }

  @Override
  protected Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    if (mergeButtons != null) {
      var r = mergeButtons.onMouseDown(event, button, uiContext.windowCursor);
      if (r != null) return r;
    }
    return super.onMouseDown(event, button);
  }

  @Override
  protected boolean onMouseUp(MouseEvent event, int button) {
    if (mergeButtons != null)
      if (mergeButtons.onMouseUp(event, button))
        return true;
    return super.onMouseUp(event, button);
  }

  @Override
  public boolean onMouseMove(MouseEvent event, SetCursor setCursor) {
    if (mergeButtons != null)
      if (mergeButtons.onMouseMove(event, setCursor))
        return true;
    return super.onMouseMove(event, setCursor);
  }
}
