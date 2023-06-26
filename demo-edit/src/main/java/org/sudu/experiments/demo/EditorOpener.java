package org.sudu.experiments.demo;

import org.sudu.experiments.parser.common.Pos;

public interface EditorOpener {
  void open(Uri uri, Selection s, Pos pos);
}
