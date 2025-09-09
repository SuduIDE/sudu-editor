package org.sudu.experiments.editor;

import org.sudu.experiments.parser.common.Pair;

import java.util.ArrayList;

public class UndoStack extends ArrayList<Pair<Diff[], Integer>> {

  private int position;

  public UndoStack() {
    super();
    position = 0;
  }

  @Override
  public boolean isEmpty() {
    return position == 0;
  }

  public boolean haveNext() {
    return position < size();
  }

  @Override
  public boolean add(Pair<Diff[], Integer> pair) {
    removeRange(position, size());
    position++;
    return super.add(pair);
  }

  public Pair<Diff[], Integer> removeLast() {
    if (position == 0) return null;
    return get(--position);
  }

  public Pair<Diff[], Integer> peekLast() {
    if (position == 0) return null;
    return get(position - 1);
  }

  public Pair<Diff[], Integer> removeNext() {
    if (position == size()) return null;
    return get(position++);
  }

  public Pair<Diff[], Integer> peekNext() {
    if (position == size()) return null;
    return get(position);
  }
}
