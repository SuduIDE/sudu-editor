package org.sudu.experiments.ui;

import org.sudu.experiments.editor.CodeLine;

public class TreeNode {
  public static final int allowRight = 1;
  public static final int allowDown = 2;

  public Runnable onClick, onClickArrow, onDblClick;

  public CodeLine line;
  public int depth;
  public int arrow;

  public TreeNode(String v, int d) {
    this(v, d, 0);
  }

  public TreeNode(String v, int d, int ar) {
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
    arrow = allowDown;
  }

  public void arrowRight() {
    arrow = allowRight;
  }

  public void clearArrow() {
    arrow = 0;
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
}
