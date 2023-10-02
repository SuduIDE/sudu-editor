package org.sudu.experiments.editor;

import org.sudu.experiments.parser.common.Pos;

public interface EditorOpener {
  void open(Uri uri, Range s, Pos pos);
}
