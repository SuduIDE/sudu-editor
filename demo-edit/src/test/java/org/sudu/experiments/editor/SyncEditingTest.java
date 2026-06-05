package org.sudu.experiments.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.math.ArrayOp;
import org.sudu.experiments.parser.common.Pos;
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

    Assertions.assertEquals("", left.document.makeString());
    Assertions.assertEquals(7, right.document.length());
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
