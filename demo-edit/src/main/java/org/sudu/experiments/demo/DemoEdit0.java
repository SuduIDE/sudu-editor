// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.Numbers;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.Objects;
import java.util.function.Consumer;

public class DemoEdit0 extends Scene {

  final V4f clearColor = Color.Cvt.fromRGB(0,0, 64);
  final UiContext uiContext;
  final EditorComponent editor;
  final EditorUi ui;

  FontDesk toolBarFont;

  V2i editorPos = new V2i();

  public DemoEdit0(SceneApi api) {
    super(api);
    uiContext = new UiContext(api);

    ui = new EditorUi(uiContext);
    uiContext.dprListeners.add(this::onDprChange);
    editor = new EditorComponent(uiContext, ui);
    uiContext.initFocus(editor);

    api.input.onMouse.add(new EditInput());
    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onKeyPress.add(new CtrlO(api, editor::openFile));
    api.input.onCopy.add(this::onCopy);
    api.input.onPaste.add(this::onPastePlainText);
    api.input.onFocus.add(uiContext::sendFocusGain);
    api.input.onBlur.add(uiContext::sendFocusLost);
    api.input.onContextMenu.add(this::onContextMenu);
    api.input.onScroll.add(this::onScroll);
    toggleDark();
  }

  public Document document() {
    return editor.model.document;
  }

  public EditorComponent editor() {
    return editor;
  }

  @Override
  public void dispose() {
    ui.dispose();
    editor.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    return editor.update(timestamp);
  }

  @Override
  public void paint() {
    api.graphics.clear(clearColor);
    editor.paint();
    ui.paint();
  }

  protected String[] menuFonts() { return Fonts.editorFonts(false); }

  @Override
  public void onResize(V2i newSize, float newDpr) {
    uiContext.onResize(newSize, newDpr);
    editor.setPos(editorPos, newSize, newDpr);
  }

  private void onDprChange(float oldDpr, float newDpr) {
    int toolbarFontSize = Numbers.iRnd(EditorConst.POPUP_MENU_FONT_SIZE * newDpr);
    toolBarFont = uiContext.graphics.fontDesk(EditorConst.POPUP_MENU_FONT_NAME, toolbarFontSize);
    ui.popupMenu.setFont(toolBarFont);
  }

  public void toggleDark() {
    applyTheme(EditorColorScheme.darkIdeaColorScheme());
  }

  public void toggleLight() {
    applyTheme(EditorColorScheme.lightIdeaColorScheme());
  }

  private void applyTheme(EditorColorScheme theme) {
    Objects.requireNonNull(theme);
    ui.setTheme(theme);
    editor.setTheme(theme);
  }

  public void setTheme(String theme) {
    switch (theme) {
      case "light" -> toggleLight();
      case "dark" -> toggleDark();
      default -> Debug.consoleInfo("unknown theme: " + theme);
    }
  }

  Consumer<String> onPastePlainText() {
    return editor::handleInsert;
  }

  boolean onKeyPress(KeyEvent event) {
    // do not consume browser keyboard to allow page reload and debug
    if (KeyEvent.isCopyPasteRelatedKey(event) || KeyEvent.isBrowserKey(event)) {
      return false;
    }
    if (event.keyCode == KeyCode.F10) {
      api.window.addChild("child", DemoEdit0::new);
      return true;
    }

    return uiContext.onKeyPress(event);
  }

  boolean onCopy(Consumer<String> setText, boolean isCut) {
    return editor.onCopy(setText, isCut);
  }

  boolean onContextMenu(MouseEvent event) {
    if (uiContext.isFocused(editor)) {
      ui.showContextMenu(event, editor, DemoEdit0.this);
    }
    return true;
  }

  boolean onScroll(MouseEvent event, float dX, float dY) {
    return editor.onScroll(dX, dY);
  }

  class EditInput implements MouseListener {

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      return ui.onMousePress(event, button, press, clickCount)
          || editor.onMousePress(event, button, press, clickCount);
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return ui.onMouseMove(event) || editor.onMouseMove(event);
    }
  }
}
