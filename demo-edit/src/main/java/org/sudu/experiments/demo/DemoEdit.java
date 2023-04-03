// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.function.Consumer;

public class DemoEdit extends Scene {

  WglGraphics g;

  final V4f bgColor = Color.Cvt.gray(0);

  EditorComponent editor;
  V2i editorPos = new V2i();
  V2i editorSize = new V2i();

  public DemoEdit(SceneApi api) {
    super(api);
    this.g = api.graphics;

//    Document document = new Document(EditorConst.DOCUMENT_LINES);
//    editor = new EditorComponent(api, document);
    editor = new EditorComponent(api);

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
  }

  @Override
  public void onResize(V2i size, double dpr) {
    editorSize.set(size);

    editor.setPos(editorPos, editorSize, dpr);
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
      return editor.onMousePress(event, button, press, clickCount);
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return editor.onMouseMove(event);
    }

    @Override
    public boolean onKey(KeyEvent event) {
      return editor.onKey(event);
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
