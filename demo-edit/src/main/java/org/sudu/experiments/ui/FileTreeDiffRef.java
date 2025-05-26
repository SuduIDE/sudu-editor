package org.sudu.experiments.ui;

import org.sudu.experiments.editor.DiffRef;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.window.ScrollView;

import java.util.function.IntConsumer;

public class FileTreeDiffRef implements DiffRef {
  final ScrollView scrollView;
  final TreeView treeView;

  public FileTreeDiffRef(ScrollView view, TreeView tree) {
    scrollView = view;
    treeView = tree;
  }

  public int getFirstLine() {
    return treeView.scrollPos.y / lineHeight();
  }

  public int getLastLine() {
    return Numbers.iDivRoundUp(
        treeView.scrollPos.y + treeView.size.y - 1, lineHeight());
  }

  @Override
  public int lineToPos(int line) {
    return line * lineHeight() + treeView.pos.y - treeView.scrollPos.y;
  }

  public V2i pos() {
    return treeView.pos;
  }

  public V2i size() {
    return treeView.size;
  }

  public void setScrollListeners(Runnable hListener, IntConsumer vListener) {
    scrollView.setListeners(hListener, vListener);
  }

  public boolean setVScrollPosSilent(int pos) {
    return scrollView.setVScrollPosSilent(pos);
  }

  public int lineHeight() {
    return treeView.clrContext.lineHeight;
  }

  public boolean onKeyPress(KeyEvent event) {
    return false;
  }
}

