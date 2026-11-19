package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelMultiEditorTest {

  @Test
  void documentEventsReachEveryAttachedEditor() {
    Model model = new Model(new String[]{"a"}, Languages.TEXT, null);
    CountingEditor first = new CountingEditor();
    CountingEditor second = new CountingEditor();
    model.attachEditor(first, null);
    model.attachEditor(second, null);

    model.document.insertLines(0, 1, new String[]{"x"});

    assertEquals(1, first.onDiffMade);
    assertEquals(1, second.onDiffMade);
    assertEquals(1, first.updateModelOnDiff);
    assertEquals(1, second.updateModelOnDiff);

    model.detachEditor(first);
    assertSame(second, model.editor);
    model.document.insertLines(0, 2, new String[]{"y"});
    assertEquals(1, first.onDiffMade);
    assertEquals(2, second.onDiffMade);
  }

  @Test
  void detachRestoresExecutorOfALiveEditor() {
    Model model = new Model(new String[]{"a"}, Languages.TEXT, null);
    CountingEditor first = new CountingEditor();
    CountingEditor second = new CountingEditor();
    NamedExecutor workerA = new NamedExecutor();
    NamedExecutor workerB = new NamedExecutor();
    model.attachEditor(first, workerA);
    model.attachEditor(second, workerB);

    assertSame(first, model.editor);
    assertSame(workerA, model.executor);

    model.detachEditor(second);
    assertSame(first, model.editor);
    assertSame(workerA, model.executor);

    model.attachEditor(second, workerB);
    model.detachEditor(first);
    assertSame(second, model.editor);
    assertSame(workerB, model.executor);
  }

  @Test
  void parserRunsIfAnyAttachedEditorEnablesIt() {
    Model model = new Model(new String[]{"a"}, Languages.TEXT, null);
    CountingEditor disabled = new CountingEditor();
    CountingEditor enabled = new CountingEditor();
    enabled.disableParser = false;
    model.attachEditor(disabled, null);
    assertTrue(model.isDisableParser());
    model.attachEditor(enabled, null);
    assertFalse(model.isDisableParser());
  }

  @Test
  void documentHighlightsReachEveryAttachedEditor() {
    Model model = new Model(new String[]{"a"}, Languages.TEXT, null);
    CountingEditor first = new CountingEditor();
    CountingEditor second = new CountingEditor();
    model.attachEditor(first, null);
    model.attachEditor(second, null);
    model.computeUsages();
    assertEquals(1, first.highlightProvider);
    assertEquals(1, second.highlightProvider);
  }

  static final class CountingEditor implements Model.EditorToModel {
    int onDiffMade;
    int updateModelOnDiff;
    int highlightProvider;
    boolean disableParser = true;

    public void useDocumentHighlightProvider(int line, int column) {
      highlightProvider++;
    }
    public void fireFileLexed() {}
    public void fireFileIterativeParsed(int start, int stop) {}
    public void updateModelOnDiff(Diff diff, boolean isUndo) { updateModelOnDiff++; }
    public void onDiffMade() { onDiffMade++; }
    public boolean isDisableParser() { return disableParser; }
    public CodeLineColorScheme getColorScheme() { return null; }
  }

  static final class NamedExecutor implements WorkerJobExecutor {
    public void sendToWorker(
        boolean priority, Consumer<Object[]> handler, String method, Object... args
    ) {}
  }
}
