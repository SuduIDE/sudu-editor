package org.sudu.experiments.demo;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.WglGraphics;
import org.sudu.experiments.demo.ui.Focusable;
import org.sudu.experiments.demo.ui.colors.EditorColorScheme;
import org.sudu.experiments.demo.worker.diff.DiffInfo;
import org.sudu.experiments.demo.worker.diff.DiffUtils;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4i;
import org.sudu.experiments.worker.ArrayView;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Diff0 extends Scene1 implements
    MouseListener,
    EditorTheme,
    EditorUi.FontApi,
    InputListeners.ScrollHandler,
    InputListeners.CopyHandler,
    InputListeners.PasteHandler {

  final EditorComponent editor1;
  final EditorComponent editor2;
  final EditorUi ui;
  private int modelFlags;
  private DiffInfo diffModel;
  static final float middleLineThicknessDp = 20;
  static final float lineWidthDp = 2;
  private final V4i middleLine = new V4i();

  final V2i p11 = new V2i();
  final V2i p12 = new V2i();
  final V2i p21 = new V2i();
  final V2i p22 = new V2i();

  DemoRect rect = new DemoRect();

  public Diff0(SceneApi api) {
    super(api);

    ui = new EditorUi(uiContext);
    editor1 = new EditorComponent(uiContext, ui);
    editor2 = new EditorComponent(uiContext, ui);
    editor1.setMirrored(true);

    IntConsumer leftScrollChanged = this::leftScrollChanged;
    IntConsumer rightScrollChanged = this::rightScrollChanged;
    editor1.setScrollListeners(leftScrollChanged, leftScrollChanged);
    editor2.setScrollListeners(rightScrollChanged, rightScrollChanged);

    editor1.setFullFileParseListener(this::fullFileParseListener);
    editor2.setFullFileParseListener(this::fullFileParseListener);

    uiContext.initFocus(editor1);

    api.input.onMouse.add(ui);

    api.input.onScroll.add(ui);
    api.input.onScroll.add(this);

    api.input.onMouse.add(this);

    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onKeyPress.add(new CtrlO(api, this::openFile));

    api.input.onCopy.add(this);
    api.input.onPaste.add(this);
    api.input.onContextMenu.add(this::onContextMenu);

    toggleDark();
  }

  void fullFileParseListener(EditorComponent editor) {
    if (editor1 == editor) modelFlags |= 1;
    if (editor2 == editor) modelFlags |= 2;
    if ((modelFlags & 3) == 3) {
      sendToDiff();
    }
  }

  public boolean onCopy(Consumer<String> consumer, boolean b) {
    if (uiContext.isFocused(editor1)) return editor1.onCopy(consumer, b);
    if (uiContext.isFocused(editor2)) return editor2.onCopy(consumer, b);
    return false;
  }

  // paste handler
  @Override
  public Consumer<String> get() {
    if (uiContext.isFocused(editor1)) return pasteHandler(editor1);
    if (uiContext.isFocused(editor2)) return pasteHandler(editor2);
    return null;
  }

  private Consumer<String> pasteHandler(EditorComponent editor) {
    return s -> {
      editor.handleInsert(s);
      editor.setDiffModel(null);
      editor.parseFullFile();
    };
  }

  private void leftScrollChanged(int ignored) {
    sync(editor1, editor2);
  }

  private void rightScrollChanged(int ignored) {
    sync(editor2, editor1);
  }

  private void sync(EditorComponent from, EditorComponent to) {
    if (this.diffModel == null || this.diffModel.ranges == null) return;
    boolean isLeft = from == editor1;

    int fromFirstLine = from.getFirstLine();
    int fromLastLine = from.getLastLine();
    int syncLine = (fromLastLine + fromFirstLine) / 2;
    int linesDelta = syncLine - fromFirstLine;

    var fromRange = diffModel.range(syncLine, isLeft);

    int rangeDelta = syncLine - (isLeft ? fromRange.fromL : fromRange.fromR);
    int scrollDelta = from.vScrollPos - fromFirstLine * from.lineHeight;
    int toRangeStart = isLeft ? fromRange.fromR : fromRange.fromL;
    int toNewLine = (toRangeStart + rangeDelta - linesDelta);
    to.setVScrollPosSilent(toNewLine * to.lineHeight + scrollDelta);
  }

  private void openFile(FileHandle handle) {
    EditorComponent activeEditor = getActiveEditor();
    if (activeEditor != null) {
      activeEditor.openFile(handle);
      diffModel = null;
    }
  }

  public void sendToDiff() {
    System.out.println("sendToDiff");
    Model model1 = editor1.model;
    Model model2 = editor2.model;
    char[] chars1 = model1.document.getChars();
    char[] chars2 = model2.document.getChars();
    int[] intervals1 = DiffUtils.makeIntervals(model1.document);
    int[] intervals2 = DiffUtils.makeIntervals(model2.document);

    api.window.sendToWorker(this::onDiffResult, DiffUtils.FIND_DIFFS,
          chars1, intervals1, chars2, intervals2);
  }

  private void drawShader() {
    if (this.diffModel == null || this.diffModel.ranges == null) return;
    int lineWidth = uiContext.toPx(lineWidthDp);

    int leftStartLine = editor1.getFirstLine();
    int leftLastLine = editor1.getLastLine();
    int rightStartLine = editor2.getFirstLine();
    int rightLastLine = editor2.getLastLine();

    int leftStartRangeInd = diffModel.rangeBinSearch(leftStartLine, true);
    int leftLastRangeInd = diffModel.rangeBinSearch(leftLastLine, true);
    int rightStartRangeInd = diffModel.rangeBinSearch(rightStartLine, false);
    int rightLastRangeInd = diffModel.rangeBinSearch(rightLastLine, false);

    for (int i = Math.min(leftStartRangeInd, rightStartRangeInd);
         i <= Math.max(leftLastRangeInd, rightLastRangeInd); i++) {
      var range = diffModel.ranges[i];
      if (range.type == 0) continue;

      int yLeftStartPosition = editor1.lineHeight * range.fromL - editor1.vScrollPos;
      int yLeftLastPosition = yLeftStartPosition + range.lenL * editor1.lineHeight;

      int yRightStartPosition = editor2.lineHeight * range.fromR - editor2.vScrollPos;
      int yRightLastPosition = yRightStartPosition + range.lenR * editor2.lineHeight;

      p11.set(middleLine.x, yLeftStartPosition);
      p21.set(middleLine.x, yLeftLastPosition);
      p12.set(middleLine.x + middleLine.z, yRightStartPosition);
      p22.set(middleLine.x + middleLine.z, yRightLastPosition);

      int rectY = Math.min(yLeftStartPosition, yRightStartPosition);
      int rectW = Math.max(yLeftLastPosition, yRightLastPosition) - rectY;

      rect.set(middleLine.x, rectY, middleLine.z, rectW);
      rect.bgColor.set(ui.theme.lineNumber.bgColor);
      rect.color.set(ui.theme.diff.getDiffColor(ui.theme, range.type));

      var g = uiContext.graphics;
      g.enableBlend(true);
      if (yLeftStartPosition == yLeftLastPosition) {
        drawLeftLine(g, yLeftStartPosition, yRightStartPosition, lineWidth);
      }
      if (yRightStartPosition == yRightLastPosition) {
        drawRightLine(g, yRightStartPosition, yLeftStartPosition, lineWidth);
      }
      g.enableScissor(middleLine);
      g.drawLineFill(rect.pos.x, rect.pos.y, rect.size,
          p11, p12, p21, p22, rect.color);
      g.disableScissor();
      g.enableBlend(false);
    }
  }

  private void drawLeftLine(
      WglGraphics g,
      int yLeftStartPosition, int yRightStartPosition, int lineWidth
  ) {
    V2i temp = uiContext.v2i1;
    temp.set(middleLine.x - editor1.pos.x, lineWidth);
    int y = yLeftStartPosition;
    if (yRightStartPosition < yLeftStartPosition) {
      y -= lineWidth;
      p11.set(p11.x, p11.y - lineWidth);
    } else p21.set(p21.x, p21.y + lineWidth);
    g.drawRect(editor1.pos.x, y, temp, rect.color);
  }

  private void drawRightLine(
      WglGraphics g,
      int yRightStartPosition, int yLeftStartPosition, int lineWidth
  ) {
    V2i temp = uiContext.v2i1;
    temp.set(editor2.size.x, lineWidth);
    int y = yRightStartPosition;
    if (yLeftStartPosition < yRightStartPosition) {
      y -= lineWidth;
      p12.set(p12.x, p12.y - lineWidth);
    } else p22.set(p22.x, p22.y + lineWidth);
    g.drawRect(middleLine.x + middleLine.z, y, temp, rect.color);
  }

  private void onDiffResult(Object[] result) {
    int[] reply = ((ArrayView) result[0]).ints();

    diffModel = DiffUtils.readDiffInfo(reply);
    editor1.setDiffModel(diffModel.lineDiffsL);
    editor2.setDiffModel(diffModel.lineDiffsR);
  }

  private EditorComponent getActiveEditor() {
    if (uiContext.isFocused(editor1)) return editor1;
    if (uiContext.isFocused(editor2)) return editor2;
    return null;
  }

  public void setReadonly(boolean f) {
    editor1.readonly = f;
    editor2.readonly = f;
  }

  public void setLeftModel(Model m) {
    editor1.setModel(m);
  }

  public void setRightModel(Model m) {
    editor2.setModel(m);
  }

  public Model getLeftModel() {
    return editor1.model();
  }

  public Model getRightModel() {
    return editor2.model();
  }

  @Override
  public void dispose() {
    ui.dispose();
    editor1.dispose();
    editor2.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    boolean u1 = editor1.update(timestamp);
    boolean u2 = editor2.update(timestamp);
    return u1 || u2;
  }

  @Override
  public void paint() {
    super.paint();
    editor1.paint();
    editor2.paint();

    // Draw middle line
    V2i size = uiContext.v2i1;
    size.set(middleLine.z, middleLine.w);
    uiContext.graphics.drawRect(
        middleLine.x, middleLine.y,
        size,
        ui.theme.editor.bg);

    drawShader();
    ui.paint();
  }

  protected String[] menuFonts() { return Fonts.editorFonts(false); }

  @Override
  public void increaseFont() {
    editor1.increaseFont();
    editor2.increaseFont();
  }

  @Override
  public void decreaseFont() {
    editor1.decreaseFont();
    editor2.decreaseFont();
  }

  @Override
  public void changeFont(String f) {
    editor1.changeFont(f);
    editor2.changeFont(f);
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
    layout(newSize, newDpr);
  }

  protected void layout(V2i newSize, float dpr) {
    V2i pos = new V2i();
    int px = DprUtil.toPx(middleLineThicknessDp, dpr);
    V2i size = new V2i(newSize.x / 2 - px / 2, newSize.y);
    editor1.setPos(pos, size, dpr);
    pos.x = newSize.x - newSize.x / 2 + px / 2;
    editor2.setPos(pos, size, dpr);
    middleLine.set(size.x, pos.y, pos.x - size.x, size.y);
  }

  public void applyTheme(EditorColorScheme theme) {

    Objects.requireNonNull(theme);
    ui.setTheme(theme);
    editor1.setTheme(theme);
    editor2.setTheme(theme);
  }

  public void setFontFamily(String fontFamily) {
    editor1.changeFont(fontFamily, editor1.getFontVirtualSize());
    editor2.changeFont(fontFamily, editor2.getFontVirtualSize());
  }

  public void setFontSize(int fontSize) {
    editor1.changeFont(editor1.getFontFamily(), fontSize);
    editor2.changeFont(editor2.getFontFamily(), fontSize);
  }

  boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.F10) {
      api.window.addChild("child", Diff0::new);
      return true;
    }
    return false;
  }

  boolean onContextMenu(MouseEvent event) {
    if (uiContext.isFocused(editor1)) {
      ui.showContextMenu(event, editor1, this, this, this::menuFonts);
    }
    if (uiContext.isFocused(editor2)) {
      ui.showContextMenu(event, editor2, this, this, this::menuFonts);
    }
    return true;
  }

  @Override
  public boolean onMouseMove(MouseEvent event) {
    boolean b1 = editor1.hitTest(event.position) && editor1.onMouseMove(event);
    boolean b2 = editor2.hitTest(event.position) && editor2.onMouseMove(event);
    return b1 || b2;
  }

  @Override
  public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
    boolean b1 = editor1.hitTest(event.position) &&
        editor1.onMouseClick(event, button, clickCount);
    boolean b2 = editor2.hitTest(event.position) &&
        editor2.onMouseClick(event, button, clickCount);
    return b1 || b2;
  }

  @Override
  public Consumer<MouseEvent> onMouseDown(MouseEvent event, int button) {
    boolean hit1 = editor1.hitTest(event.position);
    boolean hit2 = editor2.hitTest(event.position);
    Focusable focused = uiContext.focused();
    boolean noFocus = focused == null;
    boolean focus1 = focused == editor1;
    boolean focus2 = focused == editor2;

    if (hit1 && (noFocus || focus2)) {
      uiContext.setFocus(editor1);
    }
    if (hit2 && (noFocus || focus1)) {
      uiContext.setFocus(editor2);
    }

    if (hit1) {
      Consumer<MouseEvent> lock = editor1.onMouseDown(event, button);
      if (lock != null) return lock;
    }

    return hit2 ? editor2.onMouseDown(event, button) : null;
  }

  @Override
  public boolean onMouseUp(MouseEvent event, int button) {
    boolean hit1 = editor1.hitTest(event.position);
    boolean hit2 = editor2.hitTest(event.position);
    boolean b1 = hit1 && editor1.onMouseUp(event, button);
    boolean b2 = hit2 && editor2.onMouseUp(event, button);
    return b1 || b2;
  }

  @Override
  public boolean onScroll(MouseEvent event, float dX, float dY) {
    boolean hit1 = editor1.hitTest(event.position);
    boolean hit2 = editor2.hitTest(event.position);
    boolean b1 = hit1 && editor1.onScroll(dX, dY);
    boolean b2 = hit2 && editor2.onScroll(dX, dY);
    return b1 || b2;
  }
}
