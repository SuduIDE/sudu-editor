package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;

import java.util.*;

public class UndoBuffer {

  private final Map<Document, Deque<Pair<Diff[], Integer>>> diffs;
  private int diffCnt;

  public UndoBuffer() {
    this.diffs = new HashMap<>();
    this.diffCnt = 0;
  }

  public void addDiff(Document doc, Diff[] diff) {
    diffs.putIfAbsent(doc, new ArrayDeque<>());
    diffs.get(doc).add(Pair.of(diff, diffCnt++));
  }

  public V2i undoLastDiff(Document doc) {
    if (diffs.isEmpty()) return null;
    var deque = diffs.get(doc);
    if (deque == null || deque.isEmpty()) return null;
    Diff[] lastDiff = deque.removeLast().first;
    return doc.undoLastDiff(lastDiff);
  }

  public V2i undoLastDiff(Document doc1, Document doc2) {
    if (diffs.isEmpty()) return null;
    var deque1 = diffs.get(doc1);
    var deque2 = diffs.get(doc2);
    boolean empty1 = deque1 == null || deque1.isEmpty();
    boolean empty2 = deque2 == null || deque2.isEmpty();
    int ind1 = empty1 ? -1 : deque1.peekLast().second;
    int ind2 = empty2 ? -1 : deque2.peekLast().second;
    if (empty1 && empty2) return null;
    if (ind2 > ind1) {
      Diff[] lastDiff = deque2.removeLast().first;
      return doc2.undoLastDiff(lastDiff);
    }
    if (ind1 > ind2) {
      Diff[] lastDiff = deque1.removeLast().first;
      return doc1.undoLastDiff(lastDiff);
    }
    return null;
  }

  public Diff[] lastDiff(Document doc) {
    if (diffs.isEmpty()) return null;
    var deque = diffs.get(doc);
    if (deque == null || deque.isEmpty()) return null;
    return deque.peekLast().first;
  }

  public void clear(Document doc) {
    diffs.remove(doc);
  }
}
