package org.sudu.experiments.editor;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;

import java.util.*;
import java.util.function.BiConsumer;

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

  public void undoLastDiff(Document doc1, Document doc2, BiConsumer<Boolean, V2i> setCaretPos, boolean isRedo) {
    if (!isRedo) undoLastDiff(doc1, doc2, setCaretPos);
    else redoLastDiff(doc1, doc2, setCaretPos);
  }

  public void undoLastDiff(Document editor1, Document editor2, BiConsumer<Boolean, V2i> setCaretPos) {
    if (diffs.isEmpty()) return;
    var stack1 = diffs.get(editor1);
    var stack2 = diffs.get(editor2);
    boolean empty1 = stack1 == null || stack1.isEmpty();
    boolean empty2 = stack2 == null || stack2.isEmpty();
    int ind1 = empty1 ? -1 : stack1.peekLast().second;
    int ind2 = empty2 ? -1 : stack2.peekLast().second;
    if (empty1 && empty2) return;
    if (ind2 > ind1) {
      var diff = editor2.undoLastDiff(stack2.removeLast().first, false);
      setCaretPos.accept(false, diff.caretBefore);
    } else if (ind1 > ind2) {
      var diff = editor1.undoLastDiff(stack1.removeLast().first, false);
      setCaretPos.accept(true, diff.caretBefore);
    } else if (ind1 != -1) {
      var leftDiff = editor1.undoLastDiff(stack1.removeLast().first, false);
      setCaretPos.accept(true, leftDiff.caretBefore);
      var rightDiff = editor2.undoLastDiff(stack2.removeLast().first, false);
      setCaretPos.accept(false, rightDiff.caretBefore);
    }
  }

  public void redoLastDiff(Document editor1, Document editor2, BiConsumer<Boolean, V2i> setCaretPos) {
    if (diffs.isEmpty()) return;
    var stack1 = diffs.get(editor1);
    var stack2 = diffs.get(editor2);
    boolean empty1 = stack1 == null || !stack1.haveNext();
    boolean empty2 = stack2 == null || !stack2.haveNext();
    int ind1 = empty1 ? Integer.MAX_VALUE : stack1.peekNext().second;
    int ind2 = empty2 ? Integer.MAX_VALUE : stack2.peekNext().second;
    if (empty1 && empty2) return;
    if (ind2 < ind1) {
      var diff = editor2.undoLastDiff(stack2.removeLast().first, true);
      setCaretPos.accept(false, diff.caretAfter);
    } else if (ind1 < ind2) {
      var diff = editor1.undoLastDiff(stack1.removeLast().first, true);
      setCaretPos.accept(true, diff.caretAfter);
    } else if (ind1 != Integer.MAX_VALUE) {
      var leftDiff = editor1.undoLastDiff(stack1.removeLast().first, true);
      setCaretPos.accept(true, leftDiff.caretAfter);
      var rightDiff = editor2.undoLastDiff(stack2.removeLast().first, true);
      setCaretPos.accept(false, rightDiff.caretAfter);
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
