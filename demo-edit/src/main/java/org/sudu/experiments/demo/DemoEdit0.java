// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.Debug;
import org.sudu.experiments.Scene;
import org.sudu.experiments.SceneApi;
import org.sudu.experiments.demo.ui.UiContext;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.input.MouseListener;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

import java.util.Objects;

public class DemoEdit0 extends Scene {

  final V4f clearColor = Color.Cvt.fromRGB(0,0, 64);
  final UiContext uiContext;
  final EditorComponent editor;
  final EditorUi ui;

  V2i editorPos = new V2i();

  public DemoEdit0(SceneApi api) {
    super(api);
    uiContext = new UiContext(api);

    ui = new EditorUi(uiContext);
    editor = new EditorComponent(uiContext, ui);
    uiContext.initFocus(editor);

    api.input.onMouse.add(new MouseHandler());

    api.input.onKeyPress.add(KeyEvent::handleSpecialKey);
    api.input.onKeyPress.add(this::onKeyPress);
    api.input.onKeyPress.add(new CtrlO(api, editor::openFile));
    api.input.onKeyPress.add(uiContext::onKeyPress);

    api.input.onCopy.add(editor::onCopy);
    api.input.onPaste.add(() -> editor::handleInsert);
    api.input.onScroll.add((e, dX, dY) -> editor.onScroll(dX, dY));
    api.input.onContextMenu.add(this::onContextMenu);

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

  boolean onKeyPress(KeyEvent event) {
    if (event.keyCode == KeyCode.F10) {
      api.window.addChild("child", DemoEdit0::new);
      return true;
    }
    return false;
  }

  boolean onContextMenu(MouseEvent event) {
    if (uiContext.isFocused(editor)) {
      ui.showContextMenu(event, editor, DemoEdit0.this);
    }
    return true;
  }

  class MouseHandler implements MouseListener {

    @Override
    public boolean onMouseClick(MouseEvent event, int button, int clickCount) {
      return ui.onMouseClick(event, button, clickCount)
          || editor.onMouseClick(event, button, clickCount);
    }

    @Override
    public boolean onMouseUp(MouseEvent event, int button) {
      return editor.onMouseUp(event, button);
    }

    @Override
    public boolean onMouseDown(MouseEvent event, int button) {
      return editor.onMouseDown(event, button);
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return ui.onMouseMove(event) || editor.onMouseMove(event);
    }
  }
}
