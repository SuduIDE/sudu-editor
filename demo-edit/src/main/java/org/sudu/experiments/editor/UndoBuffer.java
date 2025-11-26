package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.common.TriConsumer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class UndoBuffer {

  private final Map<Document, UndoStack> diffs;
  private int diffCnt;

  public UndoBuffer() {
    this.diffs = new HashMap<>();
    this.diffCnt = 0;
  }

  public void addDiff(Document doc, CpxDiff diff) {
    addDiff(doc, diff, diffCnt++);
  }

  public void addDiff(Document doc, CpxDiff diff, int version) {
    diffs.putIfAbsent(doc, new UndoStack());
    diffs.get(doc).add(Pair.of(diff, version));
  }

  public CpxDiff undoLastDiff(Document doc, boolean isRedo) {
    if (diffs.isEmpty()) return null;
    var stack = diffs.get(doc);
    if (stack == null || stack.isEmpty()) return null;
    CpxDiff lastDiff = stack.removeLast().first;
    return doc.doCpxDiff(lastDiff, isRedo);
  }

  public void undoLastDiff(
      Document doc1, Document doc2,
      TriConsumer<Boolean, V2i, Pair<Pos, Pos>> restore,
      boolean isRedo
  ) {
    if (!isRedo) undoLastDiff(doc1, doc2, restore);
    else redoLastDiff(doc1, doc2, restore);
  }

  public void undoLastDiff(
      Document doc1, Document doc2,
      TriConsumer<Boolean, V2i, Pair<Pos, Pos>> restore
  ) {
    if (diffs.isEmpty()) return;
    var stack1 = diffs.get(doc1);
    var stack2 = diffs.get(doc2);
    boolean empty1 = stack1 == null || stack1.isEmpty();
    boolean empty2 = stack2 == null || stack2.isEmpty();
    int ind1 = empty1 ? -1 : stack1.peekLast().second;
    int ind2 = empty2 ? -1 : stack2.peekLast().second;
    if (empty1 && empty2) return;
    if (ind2 > ind1) {
      var diff = doc2.undoLastDiff(stack2.removeLast().first, false);
      restore.accept(false, diff.caretBefore, diff.selection());
    } else if (ind1 > ind2) {
      var diff = doc1.undoLastDiff(stack1.removeLast().first, false);
      restore.accept(true, diff.caretBefore, diff.selection());
    } else if (ind1 != -1) {
      var leftDiff = doc1.undoLastDiff(stack1.removeLast().first, false);
      restore.accept(true, leftDiff.caretBefore, leftDiff.selection());
      var rightDiff = doc2.undoLastDiff(stack2.removeLast().first, false);
      restore.accept(false, rightDiff.caretBefore, rightDiff.selection());
    }
  }

  public void redoLastDiff(
      Document doc1, Document doc2,
      TriConsumer<Boolean, V2i, Pair<Pos, Pos>> restore
  ) {
    if (diffs.isEmpty()) return;
    var stack1 = diffs.get(doc1);
    var stack2 = diffs.get(doc2);
    boolean empty1 = stack1 == null || !stack1.haveNext();
    boolean empty2 = stack2 == null || !stack2.haveNext();
    int ind1 = empty1 ? Integer.MAX_VALUE : stack1.peekNext().second;
    int ind2 = empty2 ? Integer.MAX_VALUE : stack2.peekNext().second;
    if (empty1 && empty2) return;
    if (ind2 < ind1) {
      var diff = doc2.undoLastDiff(stack2.removeNext().first, true);
      restore.accept(false, diff.caretAfter, diff.selection());
    } else if (ind1 < ind2) {
      var diff = doc1.undoLastDiff(stack1.removeNext().first, true);
      restore.accept(true, diff.caretAfter, diff.selection());
    } else if (ind1 != Integer.MAX_VALUE) {
      var leftDiff = doc1.undoLastDiff(stack1.removeNext().first, true);
      restore.accept(true, leftDiff.caretAfter, leftDiff.selection());
      var rightDiff = doc2.undoLastDiff(stack2.removeNext().first, true);
      restore.accept(false, rightDiff.caretAfter, rightDiff.selection());
    }
  }

  public Pair<CpxDiff, Integer> lastDiffVersion(Document doc) {
    if (diffs.isEmpty()) return null;
    var stack = diffs.get(doc);
    if (stack == null || stack.isEmpty()) return null;
    return stack.peekLast();
  }

  public CpxDiff lastDiff(Document doc) {
    if (diffs.isEmpty()) return null;
    var stack = diffs.get(doc);
    if (stack == null || stack.isEmpty()) return null;
    return stack.peekLast().first;
  }

  public void clear(Document doc) {
    diffs.remove(doc);
  }
}
