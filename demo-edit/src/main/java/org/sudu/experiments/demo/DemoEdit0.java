// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.function.Consumer;

public class DemoEdit0 extends Scene {

  WglGraphics g;
  final SetCursor setCursor;

  final V4f bgColor = Color.Cvt.gray(0);

  final Toolbar toolbar = new Toolbar();
  FontDesk toolBarFont;

  EditorComponent editor;
  V2i editorPos = new V2i();
  V2i editorSize = new V2i();
  V2i z2i = new V2i();

  private double devicePR;

  public DemoEdit0(SceneApi api) {
    super(api);
    this.g = api.graphics;
    this.setCursor = SetCursor.wrap(api.window);

    editor = new EditorComponent(api);
    editor.setText(StartFile.getBytes());

    api.input.addListener(new EditInput());
  }

  public Document document() {
    return editor.document;
  }

  public EditorComponent editor() {
    return editor;
  }

  @Override
  public void dispose() {
    toolbar.dispose();
    editor.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    return editor.update(timestamp);
  }

  @Override
  public void paint() {
    g.clear(bgColor);
    editor.paint();
    drawToolBar();
  }

  private void drawToolBar() {
    g.enableBlend(true);
    V2i toolbarSize = toolbar.size();

    V2i editPos = editor.compPos;
    V2i editSize = editor.compSize;

    int scrollWidth = editor.hasVScroll() ? editor.getVScrollSize() : 0;
    int posX =  editSize.x - 2 - scrollWidth - toolbarSize.x;

    toolbar.setPos(posX, editPos.y);
    toolbar.render(g, z2i);
  }

  @SuppressWarnings("CommentedOutCode")
  protected void initToolbar() {
    toolbar.setBgColor(Colors.toolbarBg);
//    toolbar.addButton("Reparse", Colors.toolbarText3, editor::reparse);
//    toolbar.addButton("Open", Colors.toolbarText3, this::showOpenFile);

    toolbar.addButton("Int", Colors.toolbarText3, editor::debugPrintDocumentIntervals);
    toolbar.addButton("Iter", Colors.toolbarText3, editor::iterativeParsing);
    toolbar.addButton("VP", Colors.toolbarText3, editor::parseViewport);
    toolbar.addButton("Rep", Colors.toolbarText3, editor::parseFullFile);
    toolbar.addButton("Open", Colors.toolbarText3, this::showOpenFile);

//    toolbar.addButton("↓", Colors.toolbarText3, this::moveDown);
//    toolbar.addButton("■", Colors.toolbarText3, this::stopMove);
//    toolbar.addButton("↑↑↑", Colors.toolbarText3, this::moveUp);
//
//    toolbar.addButton("C", Colors.toolbarText2, this::toggleContrast);
//    toolbar.addButton("XO", Colors.toolbarText2, this::toggleXOffset);
//    toolbar.addButton("DT", Colors.toolbarText2, this::toggleTails);
//    toolbar.addButton("TE", Colors.toolbarText2, this::toggleTopEdit);
//    toolbar.addButton("TB", Colors.toolbarText2, this::toggleTopBar);
    toolbar.addButton("A↑", Colors.toolbarText3, editor::increaseFont);
    toolbar.addButton("A↓", Colors.toolbarText3, editor::decreaseFont);
    toolbar.addButton("Segoe UI", Colors.rngToolButton(), this::setSegoeUI);
    toolbar.addButton("Verdana", Colors.rngToolButton(), this::setVerdana);
    toolbar.addButton("JetBrains Mono", Colors.rngToolButton(), this::setJetBrainsMono);
    toolbar.addButton("Consolas", Colors.rngToolButton(), this::setConsolas);
  }

  void showOpenFile() {
    api.window.showOpenFilePicker(editor::openFile);
  }

  @Override
  public void onResize(V2i size, double dpr) {
    devicePR = dpr;
    editorSize.set(size);

    editor.setPos(editorPos, editorSize, dpr);

    int toolbarFontSize = Numbers.iRnd(EditorConst.TOOLBAR_FONT_SIZE * dpr);
    toolBarFont = g.fontDesk(EditorConst.TOOLBAR_FONT_NAME, toolbarFontSize);
    toolbar.setFont(toolBarFont);
    toolbar.measure(g.mCanvas, devicePR);
  }

  private void setSegoeUI() {
    editor.changeFont(Fonts.SegoeUI, editor.getFontVirtualSize());
  }

  private void setVerdana() {
    editor.changeFont(Fonts.Verdana, editor.getFontVirtualSize());
  }

  private void setJetBrainsMono() {
    editor.changeFont(Fonts.JetBrainsMono, editor.getFontVirtualSize());
  }

  private void setConsolas() {
    editor.changeFont(Fonts.Consolas, editor.getFontVirtualSize());
  }

  class EditInput implements InputListener {

    @Override
    public void onFocus() {
      editor.onFocusGain();
    }

    @Override
    public void onBlur() {
      editor.onFocusLost();
    }

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      return editor.onMouseWheel(event, dX, dY);
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      return toolbar.onMouseClick(event.position, press)
          || editor.onMousePress(event, button, press, clickCount);
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return toolbar.onMouseMove(event.position, setCursor)
          ||  editor.onMouseMove(event, setCursor);
    }

    @Override
    public boolean onKey(KeyEvent event) {
      return handleKey(event) || editor.onKey(event);
    }

    private boolean handleKey(KeyEvent event) {
      if (!event.isPressed) return false;

      if (event.ctrl && event.keyCode == KeyCode.O) {
        if (event.shift) {
          api.window.showDirectoryPicker(
              s -> Debug.consoleInfo("showDirectoryPicker -> " + s));
        } else {
          showOpenFile();
        }
        return true;
      }
      return false;
    }

    public boolean onContextMenu(MouseEvent event) {
      return Math.random() * 2 > 1;
    }

    @Override
    public boolean onCopy(Consumer<String> setText, boolean isCut) {
      return editor.onCopy(setText, isCut);
    }

    @Override
    public Consumer<String> onPastePlainText() {
      return editor::handleInsert;
    }
  }
}
