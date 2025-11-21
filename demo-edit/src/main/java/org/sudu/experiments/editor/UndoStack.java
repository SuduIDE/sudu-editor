package org.sudu.experiments.editor;

import org.sudu.experiments.parser.common.Pair;

import java.util.ArrayList;

public class UndoStack extends ArrayList<Pair<CpxDiff, Integer>> {

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
  public boolean add(Pair<CpxDiff, Integer> pair) {
    removeRange(position, size());
    position++;
    return super.add(pair);
  }

  public Pair<CpxDiff, Integer> removeLast() {
    if (position == 0) return null;
    return get(--position);
  }

  public Pair<CpxDiff, Integer> peekLast() {
    if (position == 0) return null;
    return get(position - 1);
  }

  public Pair<CpxDiff, Integer> removeNext() {
    if (position == size()) return null;
    return get(position++);
  }

  public Pair<CpxDiff, Integer> peekNext() {
    if (position == size()) return null;
    return get(position);
  }
}
