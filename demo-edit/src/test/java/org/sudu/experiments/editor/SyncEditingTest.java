package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.worker.ArrayView;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SyncEditingTest {

  private static final String leftDoc = """
      Deleted line 1-1
      Deleted line 1-2
      Common line
      Edited line 1-3
      Edited line 1-4
      Edited line 1-5
      Common line""";

  private static final String rightDoc = """
      Common line
      Edited line 2-1
      Edited line 2-2
      Edited line 2-3
      Edited line 2-4
      Common line
      Inserted line 2-5
      Inserted line 2-6
      Inserted line 2-7""";

  private static final boolean PRINT_DOC = false;

  @Test
  public void testSyncEditing1() {
    var left = new Model(leftDoc, null);
    var right = new Model(leftDoc, null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    model.leftModel.document.deleteChar(2, 7);
    Assertions.assertEquals(left.document.makeString(), right.document.makeString());
    Assertions.assertEquals("Common ine", left.document.line(2).makeString());
    Assertions.assertEquals("Common ine", right.document.line(2).makeString());

    model.rightModel.document.deleteChar(2, 7);
    Assertions.assertEquals(left.document.makeString(), right.document.makeString());
    Assertions.assertEquals("Common ne", left.document.line(2).makeString());
    Assertions.assertEquals("Common ne", right.document.line(2).makeString());

    model.leftModel.document.insertLines(2, 7, ArrayOp.array("i"));
    Assertions.assertEquals(left.document.makeString(), right.document.makeString());
    Assertions.assertEquals("Common ine", left.document.line(2).makeString());
    Assertions.assertEquals("Common ine", right.document.line(2).makeString());

    model.rightModel.document.insertLines(2, 7, ArrayOp.array("l"));
    Assertions.assertEquals(left.document.makeString(), right.document.makeString());
    Assertions.assertEquals("Common line", left.document.line(2).makeString());
    Assertions.assertEquals("Common line", right.document.line(2).makeString());
  }

  @Test
  public void testSyncEditing2() {
    var left = new Model(leftDoc, null);
    var right = new Model(rightDoc, null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    model.leftModel.document.deleteChar(2, 7);
    Assertions.assertEquals("Common ine", left.document.line(2).makeString());
    Assertions.assertEquals("Common ine", right.document.line(0).makeString());

    model.rightModel.document.deleteChar(0, 7);
    Assertions.assertEquals("Common ne", left.document.line(2).makeString());
    Assertions.assertEquals("Common ne", right.document.line(0).makeString());

    model.leftModel.document.insertLines(2, 7, ArrayOp.array("i"));
    Assertions.assertEquals("Common ine", left.document.line(2).makeString());
    Assertions.assertEquals("Common ine", right.document.line(0).makeString());

    model.rightModel.document.insertLines(0, 7, ArrayOp.array("l"));
    Assertions.assertEquals("Common line", left.document.line(2).makeString());
    Assertions.assertEquals("Common line", right.document.line(0).makeString());
  }

  @Test
  public void testSyncEditing3() {
    var left = new Model(leftDoc, null);
    var right = new Model(rightDoc, null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    var leftSel = new Selection();
    leftSel.startPos.set(0, 0);
    leftSel.endPos.set(
        left.document.length() - 1,
        left.document.strLength(left.document.length() - 1));

    left.document.deleteSelected(leftSel);

    printDocs(left, right);
    Assertions.assertEquals(1, left.document.length());
    Assertions.assertEquals("", left.document.makeString());
    Assertions.assertEquals(7, right.document.length());
  }

  @Test
  public void testSyncEditing4() {
    var left = new Model(leftDoc, null);
    var right = new Model(rightDoc, null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    var rightSel = new Selection();
    rightSel.startPos.set(0, 0);
    rightSel.endPos.set(
        right.document.length() - 1,
        right.document.strLength(right.document.length() - 1));

    right.document.deleteSelected(rightSel);

    printDocs(left, right);
    Assertions.assertEquals(1, right.document.length());
    Assertions.assertEquals("", right.document.makeString());
    Assertions.assertEquals(5, left.document.length());
  }

  @Test
  public void testSyncEditing5() {
    var left = new Model("", null);
    var right = new Model("", null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    left.document.insertLines(0, 0, ArrayOp.array("Common Line"));
    printDocs(left, right);
    Assertions.assertEquals(1, left.document.length());
    Assertions.assertEquals("Common Line", left.document.makeString());
    Assertions.assertEquals(1, right.document.length());
    Assertions.assertEquals("Common Line", right.document.makeString());

    right.document.insertLines(0, right.document.line(0).totalStrLength, ArrayOp.array("", "Common Line"));
    printDocs(left, right);
    Assertions.assertEquals(2, left.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", left.document.makeString());
    Assertions.assertEquals(2, right.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", right.document.makeString());

    model.undoLastDiff(false);
    printDocs(left, right);
    Assertions.assertEquals(1, left.document.length());
    Assertions.assertEquals("Common Line", left.document.makeString());
    Assertions.assertEquals(1, right.document.length());
    Assertions.assertEquals("Common Line", right.document.makeString());

    model.undoLastDiff(false);
    printDocs(left, right);
    Assertions.assertEquals(1, left.document.length());
    Assertions.assertEquals("", left.document.makeString());
    Assertions.assertEquals(1, right.document.length());
    Assertions.assertEquals("", right.document.makeString());

    model.undoLastDiff(true);
    printDocs(left, right);
    Assertions.assertEquals(1, left.document.length());
    Assertions.assertEquals("Common Line", left.document.makeString());
    Assertions.assertEquals(1, right.document.length());
    Assertions.assertEquals("Common Line", right.document.makeString());

    model.undoLastDiff(true);
    printDocs(left, right);
    Assertions.assertEquals(2, left.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", left.document.makeString());
    Assertions.assertEquals(2, right.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", right.document.makeString());
  }

  @Test
  public void testSyncEditing6() {
    var left = new Model("Common Line\nCommon Line", null);
    var right = new Model("Common Line\nCommon Line", null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    var rightSel = new Selection();
    rightSel.startPos.set(0, 0);
    rightSel.endPos.set(
        right.document.length() - 1,
        right.document.strLength(right.document.length() - 1));

    right.document.deleteSelected(rightSel);

    printDocs(left, right);
    Assertions.assertEquals(1, left.document.length());
    Assertions.assertEquals("", left.document.makeString());
    Assertions.assertEquals(1, right.document.length());
    Assertions.assertEquals("", right.document.makeString());

    model.undoLastDiff(false);
    printDocs(left, right);
    Assertions.assertEquals(2, left.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", left.document.makeString());
    Assertions.assertEquals(2, right.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", right.document.makeString());
  }

  @Test
  public void testSyncEditing7() {
    var left = new Model("Common Line\nCommon Line", null);
    var right = new Model("Common Line\nCommon Line", null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(false);

    var rightSel = new Selection();
    rightSel.startPos.set(0, 0);
    rightSel.endPos.set(
        right.document.length() - 1,
        right.document.strLength(right.document.length() - 1));

    right.document.deleteSelected(rightSel);

    printDocs(left, right);
    Assertions.assertEquals(2, left.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", left.document.makeString());
    Assertions.assertEquals(1, right.document.length());
    Assertions.assertEquals("", right.document.makeString());

    model.undoLastDiff(false);
    printDocs(left, right);
    Assertions.assertEquals(2, left.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", left.document.makeString());
    Assertions.assertEquals(2, right.document.length());
    Assertions.assertEquals("Common Line\nCommon Line", right.document.makeString());
  }

  @Test
  public void testSyncEditing8() {
    var left = new Model(leftDoc, null);
    var right = new Model(rightDoc, null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    var selection = new Selection();
    selection.startPos.set(1, 8);
    selection.endPos.set(1, 13);
    left.document.insertLines(0, 8, ArrayOp.array("line "));
    left.document.deleteSelected(selection);

    printDocs(left, right);
    Assertions.assertEquals(7, left.document.length());
    Assertions.assertNotEquals(leftDoc, left.document.makeString());
    Assertions.assertEquals(9, right.document.length());
    Assertions.assertEquals(rightDoc, right.document.makeString());

    model.undoLastDiff(false);
    model.undoLastDiff(false);
    printDocs(left, right);
    Assertions.assertEquals(7, left.document.length());
    Assertions.assertEquals(leftDoc, left.document.makeString());
    Assertions.assertEquals(9, right.document.length());
    Assertions.assertEquals(rightDoc, right.document.makeString());
  }

  @Test
  public void testSyncEditing9() {
    var left = new Model(leftDoc, null);
    var right = new Model(rightDoc, null);
    var model = new FileDiffModel(new MockExecutor(), left, right);
    model.setEnableSyncEdit(true);

    var selection = new Selection();
    selection.startPos.set(0, 0);
    selection.endPos.set(2, 0);
    left.document.insertLines(0, 8, ArrayOp.array("line "));
    left.document.deleteSelected(selection);

    printDocs(left, right);
    Assertions.assertEquals(5, left.document.length());
    Assertions.assertEquals(9, right.document.length());
    Assertions.assertEquals(rightDoc, right.document.makeString());

    model.undoLastDiff(false);
    model.undoLastDiff(false);
    printDocs(left, right);
    Assertions.assertEquals(7, left.document.length());
    Assertions.assertEquals(leftDoc, left.document.makeString());
    Assertions.assertEquals(9, right.document.length());
    Assertions.assertEquals(rightDoc, right.document.makeString());
  }

  private static void printDocs(Model left, Model right) {
    if (!PRINT_DOC) return;
    System.out.println("—".repeat(20));
    System.out.println(left.document.makeString());
    System.out.println("—".repeat(10));
    System.out.println(right.document.makeString());
    System.out.println("—".repeat(20));
  }

  private static class MockExecutor implements WorkerJobExecutor {

    private static final boolean PRINT_MOCK = true;

    @Override
    public void sendToWorker(boolean priority, Consumer<Object[]> handler, String method, Object... args) {
      if (!PRINT_MOCK) System.out.printf("MockExecutor.sendToWorker: priority = %s, method = %s\n", priority, method);
      switch (method) {
        case DiffUtils.FIND_DIFFS -> {
          var result = new ArrayList<>();
          DiffUtils.findDiffs(
              (char[]) args[0], (int[]) args[1],
              (char[]) args[2], (int[]) args[3],
              (int[]) args[4], (int[]) args[5],
              (int[]) args[6], result
          );
          handler.accept(result.stream().map(MockArrayView::mkView).toArray());
        }
      }
    }
  }

  private record MockArrayView(
      byte[] bytes,
      int[] ints,
      char[] chars,
      double[] numbers
  ) implements ArrayView {
    public static MockArrayView mkView(Object obj) {
      if (obj instanceof int[] ints) return new MockArrayView(ints);
      if (obj instanceof char[] chars) return new MockArrayView(chars);
      return null;
    }

    public MockArrayView(int[] ints) {
      this(null, ints, null, null);
    }

    public MockArrayView(char[] chars) {
      this(null, null, chars, null);
    }
  }
}
