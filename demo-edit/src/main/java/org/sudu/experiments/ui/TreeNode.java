package org.sudu.experiments.ui;

import org.sudu.experiments.editor.CodeLine;
import org.sudu.experiments.ui.fonts.Codicons;

public class TreeNode {

  public Runnable onClick, onClickArrow, onDblClick;

  public CodeLine line;
  public int depth;
  public char arrow;
  public char icon;

  public TreeNode(String v, int d) {
    this(v, d, (char) 0);
  }

  public TreeNode(String v, int d, char ar) {
    line = new CodeLine(v);
    depth = d;
    arrow = ar;
  }

  public String value() {
    return line.get(0).s;
  }

  public void setBold(boolean b) {
    line.get(0).setBold(b);
  }

  public void arrowDown() {
    arrow = Codicons.chevron_down;
  }

  public boolean isOpened() {
    return arrow == Codicons.chevron_down;
  }

  public boolean isClosed() {
    return arrow == Codicons.chevron_right;
  }

  public void arrowRight() {
    arrow = Codicons.chevron_right;
  }

  public void clearArrow() {
    arrow = 0;
  }

  public void iconFolder() {
    icon = Codicons.folder;
  }

  public void iconFile() {
    icon = Codicons.file;
  }

  public void iconFileCode() {
    icon = Codicons.file_code;
  }

  public void iconFileBinary() {
    icon = Codicons.file_binary;
  }

  public void iconFolderOpened() {
    icon = Codicons.folder_opened;
  }

  public void iconRefresh() {
    icon = Codicons.refresh;
  }

  public void clearIcon() {
    icon = 0;
  }

  public void onClick(Runnable h) {
    onClick = h;
  }

  public void onDblClick(Runnable h) {
    onDblClick = h;
  }

  public void onClickArrow(Runnable h) {
    onClickArrow = h;
  }

  public Runnable onEnter() {
    return onClick != null ? onClick :
        onDblClick != null ? onDblClick : onClickArrow;
  }
}
