package org.sudu.experiments.editor;

import org.sudu.experiments.DprUtil;
import org.sudu.experiments.FileHandle;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.diff.DiffSync;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffUtils;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.*;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.ui.Focusable;

import java.util.Objects;
import java.util.function.Consumer;

public class Diff0 extends WindowScene implements
    MouseListener,
    ThemeControl,
    EditorUi.FontApi,
    EditorUi.CleartypeControl,
    InputListeners.ScrollHandler,
    InputListeners.CopyHandler,
    InputListeners.PasteHandler {

  static final float lineWidthDp = 2;

  final EditorComponent editor1;
  final EditorComponent editor2;
  final EditorUi ui;
  private int modelFlags;
  protected DiffInfo diffModel;
  final DiffSync diffSync;
  String leftFile, rightFile;

  final MiddleLine middleLine = new MiddleLine(uiContext);

  public Diff0(SceneApi api) {
    super(api, false);

    ui = new EditorUi(windowManager);
    editor1 = new EditorComponent(ui);
    editor2 = new EditorComponent(ui);
    diffSync = new DiffSync(editor1, editor2);

    editor1.setMirrored(true);
    middleLine.setLeftRight(editor1, editor2);

    editor1.setFullFileParseListener(this::fullFileParseListener);
    editor2.setFullFileParseListener(this::fullFileParseListener);

    highlightResolveErrors(false);

    uiContext.initFocus(editor1);

    api.input.onScroll.add(this);

    // dispatch between editors: move to WindowManager later
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

  void highlightResolveErrors(boolean highlight) {
    editor1.highlightResolveError(highlight);
    editor2.highlightResolveError(highlight);
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

  private void openFile(FileHandle handle) {
    EditorComponent activeEditor = getActiveEditor();
    if (activeEditor != null) {
      activeEditor.openFile(handle, () -> onOpen(activeEditor, handle));
      diffModel = null;
      diffSync.setModel(null);
      middleLine.setModel(null);
    }
  }

  void onOpen(EditorComponent activeEditor, FileHandle handle) {
    String name = handle.getName();
    if (activeEditor == editor1) leftFile = name; else rightFile = name;
    if (leftFile != null && rightFile != null) {
      api.window.setTitle(handle.getName());
    } else {
      if (leftFile != null) api.window.setTitle(leftFile);
      if (rightFile != null) api.window.setTitle(rightFile);
    }
  }

  public void sendToDiff() {
    System.out.println("sendToDiff");
    DiffUtils.findDiffs(editor1.model.document, editor2.model.document,
        this::onDiffResult, api.window);
  }

  private void onDiffResult(DiffInfo result) {
    diffModel = result;
    editor1.setDiffModel(diffModel.lineDiffsL);
    editor2.setDiffModel(diffModel.lineDiffsR);
    diffSync.setModel(diffModel);
    middleLine.setModel(diffModel);
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
    super.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    boolean u1 = editor1.update(timestamp);
    boolean u2 = editor2.update(timestamp);
    return u1 || u2;
  }

  @Override
  public void paint() {
    clear();
    editor1.paint();
    editor2.paint();
    middleLine.paint();
    windowManager.draw();
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
  public void setFontPow(float p) {
    uiContext.setFontPow(p);
  }

  @Override
  public void enableCleartype(boolean en) {
    if (windowManager.enableCleartype(en)) {
      editor1.onTextRenderingSettingsChange();
      editor2.onTextRenderingSettingsChange();
    }
  }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    super.onResize(newSize, newDpr);
    layout(newSize, newDpr);
  }

  protected void layout(V2i newSize, float dpr) {
    V2i pos = new V2i();
    int px = DprUtil.toPx(MiddleLine.middleLineThicknessDp, dpr);
    V2i size = new V2i(newSize.x / 2 - px / 2, newSize.y);
    editor1.setPosition(pos, size, dpr);
    pos.x = newSize.x - newSize.x / 2 + px / 2;
    editor2.setPosition(pos, size, dpr);
    middleLine.pos.set(size.x, pos.y);
    middleLine.size.set(pos.x - size.x, size.y);
  }

  public void applyTheme(EditorColorScheme theme) {
    Objects.requireNonNull(theme);
    ui.setTheme(theme);
    editor1.setTheme(theme);
    editor2.setTheme(theme);
    middleLine.setTheme(theme);
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
      ui.showEditorMenu(event.position, editor1,
          this, this, this, this::menuFonts);
    }
    if (uiContext.isFocused(editor2)) {
      ui.showEditorMenu(event.position, editor2,
          this, this, this, this::menuFonts);
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
    boolean b1 = hit1 && editor1.onScroll(event, dX, dY);
    boolean b2 = hit2 && editor2.onScroll(event, dX, dY);
    return b1 || b2;
  }
}
