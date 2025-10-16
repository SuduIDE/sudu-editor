package org.sudu.experiments.editor;

import org.sudu.experiments.parser.common.Pair;

import java.util.*;
import java.util.function.Function;

public class UndoBuffer {

  private final Map<Document, UndoStack> diffs;
  private int diffCnt;

  public UndoBuffer() {
    this.diffs = new HashMap<>();
    this.diffCnt = 0;
  }

  Function<Document, UndoStack> newStack = key -> new UndoStack();

  public void addDiff(Document doc, Diff[] diff) {
    var stack = diffs.computeIfAbsent(doc, newStack);
    stack.add(Pair.of(diff, diffCnt++));
  }

  public Diff undoLastDiff(Document doc, boolean isRedo) {
    if (diffs.isEmpty()) return null;
    var stack = diffs.get(doc);
    if (stack == null || stack.isEmpty()) return null;
    Diff[] lastDiff = stack.removeLast().first;
    return doc.undoLastDiff(lastDiff, isRedo);
  }

  public void undoLastDiff(EditorComponent editor1, EditorComponent editor2, boolean isRedo) {
    if (!isRedo) undoLastDiff(editor1, editor2);
    else redoLastDiff(editor1, editor2);
  }

  public void undoLastDiff(EditorComponent editor1, EditorComponent editor2) {
    if (diffs.isEmpty()) return;
    Document doc1 = editor1.model.document;
    Document doc2 = editor2.model.document;
    var stack1 = diffs.get(doc1);
    var stack2 = diffs.get(doc2);
    boolean empty1 = stack1 == null || stack1.isEmpty();
    boolean empty2 = stack2 == null || stack2.isEmpty();
    int ind1 = empty1 ? -1 : stack1.peekLast().second;
    int ind2 = empty2 ? -1 : stack2.peekLast().second;
    if (empty1 && empty2) return;
    EditorComponent editor = null;
    Diff[] lastDiff = null;
    if (ind2 > ind1) {
      editor = editor2;
      lastDiff = stack2.removeLast().first;
    }
    if (ind1 > ind2) {
      editor = editor1;
      lastDiff = stack1.removeLast().first;
    }
    if (editor == null || lastDiff == null) return;
    var diff = editor.model().document.undoLastDiff(lastDiff, false);
    var caretReturn = diff.caretReturn;
    editor.setCaretLinePos(caretReturn.x, caretReturn.y, false);
  }

  public void redoLastDiff(EditorComponent editor1, EditorComponent editor2) {
    if (diffs.isEmpty()) return;
    Document doc1 = editor1.model.document;
    Document doc2 = editor2.model.document;
    var stack1 = diffs.get(doc1);
    var stack2 = diffs.get(doc2);
    boolean empty1 = stack1 == null || !stack1.haveNext();
    boolean empty2 = stack2 == null || !stack2.haveNext();
    int ind1 = empty1 ? Integer.MAX_VALUE : stack1.peekNext().second;
    int ind2 = empty2 ? Integer.MAX_VALUE : stack2.peekNext().second;
    if (empty1 && empty2) return;
    EditorComponent editor = null;
    Diff[] lastDiff = null;
    if (ind2 < ind1) {
      editor = editor2;
      lastDiff = stack2.removeNext().first;
    }
    if (ind1 < ind2) {
      editor = editor1;
      lastDiff = stack1.removeNext().first;
    }
    if (editor == null || lastDiff == null) return;
    var diff = editor.model().document.undoLastDiff(lastDiff, true);
    var caretReturn = diff.caretPos;
    editor.setCaretLinePos(caretReturn.x, caretReturn.y, false);
  }

  public Diff[] lastDiff(Document doc) {
    if (diffs.isEmpty()) return null;
    var stack = diffs.get(doc);
    if (stack == null || stack.isEmpty()) return null;
    return stack.peekLast().first;
  }

  public void clear(Document doc) {
    diffs.remove(doc);
  }
}
