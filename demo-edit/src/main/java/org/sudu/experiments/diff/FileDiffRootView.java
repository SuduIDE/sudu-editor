package org.sudu.experiments.diff;

import org.sudu.experiments.BooleanConsumer;
import org.sudu.experiments.editor.*;
import org.sudu.experiments.editor.Diff;
import org.sudu.experiments.editor.MergeButtonsModel;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.parser.common.TriConsumer;
import org.sudu.experiments.ui.window.WindowManager;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

class FileDiffRootView extends DiffRootView {
  final EditorUi ui;
  final EditorComponent editor1;
  final EditorComponent editor2;

  final DiffSync diffSync;

  DiffInfo diffModel;
  private int modelFlags;
  boolean compactViewRequest;

  Consumer<Model> onLeftDiffMade, onRightDiffMade;

  boolean firstDiffRevealed = false, needScrollSync = false;
  private static final boolean showNavigateLog = true;
  private Runnable onRefresh, onDiffModelSet;
  public final boolean isCodeReview;
  private final UndoBuffer undoBuffer;

  protected long sendDiffTime = System.currentTimeMillis();
  protected long fullParseTime = System.currentTimeMillis();
  protected final boolean printTime = true;

  FileDiffRootView(WindowManager wm, boolean disableParser, boolean isCodeReview) {
    super(wm.uiContext);
    ui = new EditorUi(wm);
    this.isCodeReview = isCodeReview;
    editor1 = new EditorComponent(ui);
    editor2 = new EditorComponent(ui);
    middleLine.setLeftRight(editor1, editor2);
    undoBuffer = new UndoBuffer();
    Consumer<EditorComponent> parseListener = this::fullFileParseListener;
    TriConsumer<EditorComponent, Integer, Integer> iterativeParseListener = this::iterativeParseFileListener;
    SyncPoints syncPoints = new SyncPoints(() -> sendToDiff(false));

    editor1.setFullFileLexedListener(parseListener);
    editor1.setIterativeParseFileListener(iterativeParseListener);
    editor1.setUpdateModelOnDiffListener(this::updateModelOnDiffMadeListener);
    editor1.setOnDiffMadeListener(this::onDiffMadeListener);
    editor1.highlightResolveError(false);
    editor1.setMirrored(true);
    editor1.setSyncPoints(syncPoints, true);
    editor1.setDisableParser(disableParser);
    editor1.setUndoBuffer(undoBuffer);

    editor2.setFullFileLexedListener(parseListener);
    editor2.setIterativeParseFileListener(iterativeParseListener);
    editor2.setUpdateModelOnDiffListener(this::updateModelOnDiffMadeListener);
    editor2.setOnDiffMadeListener(this::onDiffMadeListener);
    editor2.highlightResolveError(false);
    editor2.setSyncPoints(syncPoints, false);
    editor2.setDisableParser(disableParser);
    editor2.setUndoBuffer(undoBuffer);

    diffSync = new DiffSync(editor1, editor2, true);
    middleLine.setOnMidSyncPointHover(i -> onMidSyncLineHover(syncPoints, i));
    middleLine.setOnMidSyncPointClick(i -> onMidSyncLineClick(syncPoints, i));

    middleLine.setSyncPoints(syncPoints);
    setViews(editor1, editor2, middleLine);
    setEmptyDiffModel();
  }

  public void setModel(Model m1, Model m2) {
    editor1.setModel(m1);
    editor2.setModel(m2);
    sendToDiff(true);
  }

  public Model getLeftModel() {
    return editor1.model();
  }

  public Model getRightModel() {
    return editor2.model();
  }

  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    editor1.readonly = leftReadonly;
    editor2.readonly = rightReadonly;
  }

  public void setDisableParser(boolean disableParser) {
    editor1.setDisableParser(disableParser);
    editor2.setDisableParser(disableParser);
  }

  public EditorUi.FontApi fontApi() {
    return new FontApi2(editor1, editor2, ui.windowManager.uiContext);
  }

  private void fullFileParseListener(EditorComponent editor) {
    if (printTime) {
      long currentTime = System.currentTimeMillis();
      System.out.println("FileDiffRootView.fullFileParseListener: " +
          "left = " + (editor1 == editor) +
          ", time = " + (currentTime - fullParseTime) + "ms"
      );
      fullParseTime = currentTime;
    }
    if (editor1 == editor) modelFlags |= 1;
    if (editor2 == editor) modelFlags |= 2;
    if ((modelFlags & 3) == 3) {
      sendToDiff(false);
    }
  }

  private void iterativeParseFileListener(EditorComponent editor, int start, int stop) {
    boolean isL = editor == editor1;

    if (diffModel == null) return;
    int startLine = editor.model().document.getLine(start).x;
    int stopLine = editor.model().document.getLine(stop).x;
    var fromRangeInd = diffModel.leftNotEmptyBS(startLine, isL);
    var toRangeInd = diffModel.rightNotEmptyBS(stopLine, isL);

    if (fromRangeInd != 0 && diffModel.ranges[fromRangeInd].type != DiffTypes.DEFAULT) fromRangeInd--;
    if (toRangeInd != diffModel.rangeCount() - 1 && diffModel.ranges[toRangeInd].type != DiffTypes.DEFAULT)
      toRangeInd++;

    var fromRange = diffModel.ranges[fromRangeInd];
    var toRange = diffModel.ranges[toRangeInd];

    sendIntervalToDiff(fromRange.fromL, toRange.toL(), fromRange.fromR, toRange.toR());
  }

  private void applyDiff(DiffRange range, boolean isL) {
    if (range.type == DiffTypes.DEFAULT) return;
    var fromModel = isL ? editor1.model() : editor2.model();
    int fromStartLine = isL ? range.fromL : range.fromR;
    int fromEndLine = isL ? range.toL() : range.toR();
    var lines = fromModel.document.getLines(fromStartLine, fromEndLine);

    int toStartLine = !isL ? range.fromL : range.fromR;
    int toEndLine = !isL ? range.toL() : range.toR();

    var toModel = !isL ? editor1.model() : editor2.model();
    toModel.document.applyChange(toStartLine, toEndLine, lines);
  }

  private void updateModelOnDiffMadeListener(EditorComponent editor, Diff diff, boolean isUndo) {
    boolean isDelete = diff.isDelete ^ isUndo;
    boolean isL = editor == editor1;
    if (isDelete) onDeleteDiffMadeListener(diff, isL);
    else onInsertDiffMadeListener(diff, isL);
    if (diffModel != null) {
      editor.setDiffModel(isL ? diffModel.lineDiffsL : diffModel.lineDiffsR);
      middleLine.setModel(diffModel);
      if (diffModel.isCompactedView())
        applyCodeMapping(diffModel.getExpander(this::applyCodeMapping));
    }
  }

  private void onDiffMadeListener(EditorComponent editor) {
    boolean left = editor == editor1;
    if (left) onLeftDiffMade();
    else onRightDiffMade();
  }

  private void onInsertDiffMadeListener(Diff diff, boolean isL) {
    if (diffModel != null) {
      diffModel.insertAt(diff.line, diff.lineCount(), isL);
    }
  }

  private void onDeleteDiffMadeListener(Diff diff, boolean isL) {
    if (diffModel != null) {
      diffModel.deleteAt(diff.line, diff.lineCount(), isL);
    }
  }

  public void applyTheme(EditorColorScheme theme) {
    ui.setTheme(theme);
    middleLine.setTheme(
        theme.codeDiffBg,
        theme.editor.bg,
        theme.lineNumber.syncPoint,
        theme.lineNumber.midLineHoverSyncPoint,
        theme.lineNumber.currentSyncPoint,
        theme.lineNumber.hoverSyncPoint
    );
    editor1.setTheme(theme);
    editor2.setTheme(theme);
  }

  public void setEmptyDiffModel() {
    var leftLine = new LineDiff(DiffTypes.DEFAULT);
    var rightLine = new LineDiff(DiffTypes.DEFAULT);
    var range = new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT);
    var diffInfo = new DiffInfo(
        new LineDiff[]{leftLine},
        new LineDiff[]{rightLine},
        new DiffRange[]{range}
    );
    setDiffModel(diffInfo, docVersions());
    firstDiffRevealed = false;
  }

  public void setDiffModel(DiffInfo diffInfo, int[] versions) {
    if (!Arrays.equals(versions, docVersions())) return;
    long currentTime = System.currentTimeMillis();
    if (printTime) {
      System.out.println("FileDiffRootView.setDiffModel: time = " + (currentTime - sendDiffTime) + "ms");
      sendDiffTime = currentTime;
    }
    boolean compact = compactViewRequest;
    diffModel = diffInfo;
    editor1.setDiffModel(diffModel.lineDiffsL);
    editor2.setDiffModel(diffModel.lineDiffsR);
    diffSync.setModel(diffModel);
    middleLine.setModel(diffModel);

    var pair = MergeButtonsModel.getModels(
        diffInfo,
        editor1.readonly, editor2.readonly,
        editor1.syncPoints(), editor2.syncPoints(),
        this::applyDiff
    );
    MergeButtonsModel m1 = pair.first[0], m2 = pair.first[1];
    BooleanConsumer[] acceptReject = pair.second;
    editor1.setMergeButtons(m1.actions, null, m1.lines, false);
    editor2.setMergeButtons(m2.actions, null, m2.lines, isCodeReview);

    if (!firstDiffRevealed) revealFirstDiff();
    if (onDiffModelSet != null) onDiffModelSet.run();
    if (compact && !diffModel.isEmpty()) {
      buildCompactModel();
    }
    ui.windowManager.uiContext.window.repaint();
  }

  public void updateDiffModel(
      int fromL, int toL,
      int fromR, int toR,
      int[] versions,
      DiffInfo updateInfo
  ) {
    if (!Arrays.equals(versions, docVersions())) return;
    diffModel.updateDiffInfo(fromL, toL, fromR, toR, updateInfo);
    setDiffModel(diffModel, versions);
  }

  protected void sendToDiff(boolean cmpOnlyLines) {
    if (EditorComponent.debugDiffModel)
      System.out.println("EditorComponent.sendToDiff: cmpOnlyLines = " + cmpOnlyLines +
          ", editor1.docL = " + editor1.model().document.length() +
          ", editor2.docL = " + editor2.model().document.length());
    sendDiffTime = System.currentTimeMillis();
    int[] syncL = editor1.copiedSyncPoints();
    int[] syncR = editor2.copiedSyncPoints();
    if (syncL.length != syncR.length) return;
    DiffUtils.findDiffs(
        editor1.model().document,
        editor2.model().document,
        cmpOnlyLines,
        syncL, syncR,
        this::setDiffModel,
        ui.windowManager.uiContext.window.worker());
  }

  protected void sendIntervalToDiff(
      int fromL, int toL,
      int fromR, int toR
  ) {
    if (editor1.hasSyncPoints() || editor2.hasSyncPoints()) {
      sendToDiff(false);
    } else {
      sendDiffTime = System.currentTimeMillis();
      DiffUtils.findIntervalDiffs(
          editor1.model().document,
          editor2.model().document,
          (upd, versions) -> updateDiffModel(fromL, toL, fromR, toR, versions, upd),
          ui.windowManager.uiContext.window.worker(),
          fromL, toL, fromR, toR
      );
    }
  }

  public void setOnDiffMade(
      Consumer<Model> onLeftDiffMade,
      Consumer<Model> onRightDiffMade
  ) {
    this.onLeftDiffMade = onLeftDiffMade;
    this.onRightDiffMade = onRightDiffMade;
  }

  private void onLeftDiffMade() {
    if (onLeftDiffMade != null)
      onLeftDiffMade.accept(editor1.model());
  }

  private void onRightDiffMade() {
    if (onRightDiffMade != null)
      onRightDiffMade.accept(editor2.model());
  }

  private void revealFirstDiff() {
    for (var range: diffModel.ranges) {
      if (range.type == DiffTypes.DEFAULT) continue;
      int curL = editor1.caretLine(), curR = editor2.caretLine();
      if (range.inside(curL, true) || range.inside(curR, false)) return;
      setPositionsAtRange(range);
      firstDiffRevealed = true;
      break;
    }
  }

  public boolean canNavigateUp(EditorComponent focused) {
    int lineInd = focused.caretLine();
    boolean left = focused == editor1;
    int rangeInd = diffModel.leftBS(lineInd, left);
    for (int i = rangeInd - 1; i >= 0; i--) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return true;
    }
    return false;
  }

  public void navigateUp(EditorComponent focused) {
    int lineInd = focused.caretLine();
    boolean left = focused == editor1;
    int rangeInd = diffModel.leftBS(lineInd, left);
    for (int i = rangeInd - 1; i >= 0; i--) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) {
        setPositionsAtRange(diffModel.ranges[i]);
        return;
      }
    }
  }

  public boolean canNavigateDown(EditorComponent focused) {
    int lineInd = focused.caretLine();
    boolean left = focused == editor1;
    int rangeInd = diffModel.rangeBinSearch(lineInd, left);
    for (int i = rangeInd + 1; i < diffModel.ranges.length; i++) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) return true;
    }
    return false;
  }

  public void navigateDown(EditorComponent focused) {
    int lineInd = focused.caretLine();
    boolean left = focused == editor1;
    int rangeInd = diffModel.rangeBinSearch(lineInd, left);
    for (int i = rangeInd + 1; i < diffModel.ranges.length; i++) {
      if (diffModel.ranges[i].type != DiffTypes.DEFAULT) {
        setPositionsAtRange(diffModel.ranges[i]);
        return;
      }
    }
  }

  private void setPositionsAtRange(DiffRange range) {
    editor1.setPosition(0, range.fromL);
    editor1.revealLineInCenter(range.fromL);
    editor2.setPosition(0, range.fromR);
    editor2.revealLineInCenter(range.fromR);
    ui.windowManager.uiContext.window.repaint();
    if (showNavigateLog) {
      System.out.println("Navigated on lines " + (range.fromL + 1) + " and " + (range.fromR + 1));
    }
  }

  public void setOnRefresh(Runnable onRefresh) {
    this.onRefresh = onRefresh;
  }

  public void setOnDiffModelSet(Runnable onDiffModelSet) {
    this.onDiffModelSet = onDiffModelSet;
  }

  public void refresh() {
    System.out.println("FileDiffRootView.refresh");
    needScrollSync = true;
    if (onRefresh != null) onRefresh.run();
  }

  public void undoLastDiff(boolean isRedo) {
    undoBuffer.undoLastDiff(editor1, editor2, isRedo);
  }

  public void unsetModelFlagsBit(int bit) {
    modelFlags &= ~bit;
  }

  public void onMidSyncLineClick(SyncPoints syncPoints, int line) {
    syncPoints.remove(line);
  }

  public void onMidSyncLineHover(SyncPoints syncPoints, int line) {
    syncPoints.midLineHoverSyncPoint = line;
  }

  public void setCompactView(boolean compact) {
    compactViewRequest = compact;
    if (compact) {
      if (!diffModel.isEmpty())
        buildCompactModel();
    } else {
      diffModel.clearCompactView();
      editor1.clearCompactViewModel();
      editor2.clearCompactViewModel();
      editor1.revealLineInCenter(editor1.caretLine());
      editor2.revealLineInCenter(editor2.caretLine());
    }
    ui.windowManager.uiContext.window.repaint();
  }

  int[] docVersions() {
    return new int[]{editor1.docVersion(), editor2.docVersion()};
  }

  private void buildCompactModel() {
    diffModel.buildCompactView(this::applyCodeMapping);
  }

  private void applyCodeMapping(IntConsumer actions) {
    editor1.setCompactViewModel(diffModel.codeMappingL, actions);
    editor2.setCompactViewModel(diffModel.codeMappingR, actions);
  }

  public boolean isCompactedView() {
    return compactViewRequest;
  }
}
