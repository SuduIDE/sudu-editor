package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;

// this interface express left and right pane of a diff
public interface DiffRef extends Focusable {

  // getFirstLine and getLastLine return values in view space,
  // for compact mode it means in the view lines, not in document
  // the range returned is inclusive: [first...last]
  int getFirstLine();
  int getLastLine();

  int lineToPos(int line);

  V2i pos();

  V2i size();

  // scroll sync
  void setScrollListeners(Runnable hListener, Runnable vListener);
  boolean setVScrollPosSilent(int pos);
  int lineHeight();
}
