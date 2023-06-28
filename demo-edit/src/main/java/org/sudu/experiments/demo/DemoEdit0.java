// todo: highlight current line
// todo: ctrl-left-right move by elements

package org.sudu.experiments.demo;

import org.sudu.experiments.*;
import org.sudu.experiments.demo.ui.PopupMenu;
import org.sudu.experiments.demo.ui.ToolbarItem;
import org.sudu.experiments.demo.ui.ToolbarItemBuilder;
import org.sudu.experiments.fonts.FontDesk;
import org.sudu.experiments.fonts.Fonts;
import org.sudu.experiments.input.InputListener;
import org.sudu.experiments.input.KeyCode;
import org.sudu.experiments.input.KeyEvent;
import org.sudu.experiments.input.MouseEvent;
import org.sudu.experiments.math.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.sudu.experiments.demo.ui.ToolbarItemBuilder.*;

public class DemoEdit0 extends Scene0 {

  WglGraphics g;
  final SetCursor setCursor;

  final PopupMenu popupMenu;
  FontDesk toolBarFont;

  EditorComponent editor;
  V2i editorPos = new V2i();

  public DemoEdit0(SceneApi api) {
    super(api, false);
    this.g = api.graphics;
//    clearColor.set(Color.Cvt.gray(0));
    this.setCursor = SetCursor.wrap(api.window);
    popupMenu = new PopupMenu(g);

    editor = new EditorComponent(api);
    api.input.addListener(new EditInput());
  }

  public Document document() {
    return editor.model.document;
  }

  public EditorComponent editor() {
    return editor;
  }

  @Override
  public void dispose() {
    popupMenu.dispose();
    editor.dispose();
  }

  @Override
  public boolean update(double timestamp) {
    return editor.update(timestamp);
  }

  @Override
  public void paint() {
    super.paint();
    editor.paint();
    popupMenu.paint();
  }

  protected Supplier<ToolbarItem[]> popupMenuContent(V2i eventPosition) {
    ToolbarItemBuilder tbb = new ToolbarItemBuilder();

    cutCopyPaste(tbb);
    tbb.addItem("open ...", Colors.popupText, this::showOpenFilePicker);
    tbb.addItem("goto >", Colors.popupText2, gotoItems(eventPosition));
    tbb.addItem("parser >", Colors.popupText2, parser());
    if (1 < 0) tbb.addItem("old >", Colors.popupText2, oldDev());
    tbb.addItem("theme >", Colors.popupText2, themes());
    tbb.addItem("font size >", Colors.popupText2, fontSize());
    tbb.addItem("fonts >", Colors.popupText2, fontSelect());

    return tbb.supplier();
  }

  private void cutCopyPaste(ToolbarItemBuilder tbb) {
    tbb.addItem("cut", Colors.popupText, this::cutAction);
    tbb.addItem("copy", Colors.popupText, this::copyAction);

    if (api.window.isReadClipboardTextSupported()) {
      tbb.addItem("paste", Colors.popupText, this::pasteAction);
    }
  }

  private Supplier<ToolbarItem[]> gotoItems(V2i eventPosition) {
    return () -> {
      ArrayList<ToolbarItem> l = new ArrayList<>();
      Model model = editor().model();
      String language = model.language();
      String scheme = model.uriScheme();
      EditorRegistrations reg = editor().registrations;

      var declarationProvider = reg.findDeclarationProvider(language, scheme);

      if (declarationProvider != null) {
        l.add(ti(
            "use declarationProvider",
            Colors.popupText,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DECL)));
      }

      var definitionProvider = reg.findDefinitionProvider(language, scheme);

      if (definitionProvider != null) {
        l.add(ti(
            "use definitionProvider",
            Colors.popupText,
            () -> findUsagesDefDecl(eventPosition, DefDeclProvider.Type.DEF)));
      }

      var refProvider = reg.findReferenceProvider(language, scheme);

      if (refProvider != null) {
        l.add(ti(
            "use referenceProvider",
            Colors.popupText,
            () -> findUsages(eventPosition, refProvider)));
      }

      l.add(ti(
          "Definition or usages",
          Colors.popupText,
          () -> findUsagesDefDecl(eventPosition, null)));
      return l.toArray(ToolbarItem[]::new);
    };
  }

  private Supplier<ToolbarItem[]> parser() {
    return ArrayOp.supplier(
        ti("Int", Colors.popupText, editor::debugPrintDocumentIntervals),
        ti("Iter", Colors.popupText, editor::iterativeParsing),
        ti("VP", Colors.popupText, editor::parseViewport),
        ti("Rep", Colors.popupText, editor::parseFullFile));
  }

  private Supplier<ToolbarItem[]> oldDev() {
    return ArrayOp.supplier(
        ti("↓ move", Colors.popupText, editor::moveDown),
        ti("■ stop", Colors.popupText, editor::stopMove),
        ti("↑ move", Colors.popupText, editor::moveUp),
        ti("toggleContrast", Colors.popupText, editor::toggleContrast),
        ti("toggleXOffset", Colors.popupText, editor::toggleXOffset),
        ti("toggleTails", Colors.popupText, editor::toggleTails));
  }

  private Supplier<ToolbarItem[]> themes() {
    return ArrayOp.supplier(
        ti("Dark", Colors.popupText, editor::toggleDark),
        ti("Light", Colors.popupText, editor::toggleLight)
    );
  }

  private Supplier<ToolbarItem[]> fontSize() {
    return ArrayOp.supplier(
        ti("↑ increase", Colors.popupText, editor::increaseFont),
        ti("↓ decrease", Colors.popupText, editor::decreaseFont));
  }

  private Supplier<ToolbarItem[]> fontSelect() {
    return ArrayOp.supplier(
        ti("Segoe UI", Colors.rngToolButton(), this::setSegoeUI),
        ti("Verdana", Colors.rngToolButton(), this::setVerdana),
        ti("JetBrains Mono", Colors.rngToolButton(), this::setJetBrainsMono),
        ti("Consolas", Colors.rngToolButton(), this::setConsolas));
  }

  private void pasteAction() {
    popupMenu.hide();
    api.window.readClipboardText(
        editor::handleInsert,
        onError("readClipboardText error: "));
  }

  private void cutAction() {
    popupMenu.hide();
    editor.onCopy(copyHandler(), true);
  }

  private void copyAction() {
    popupMenu.hide();
    editor.onCopy(copyHandler(), false);
  }

  private Consumer<String> copyHandler() {
    return text -> api.window.writeClipboardText(text,
        org.sudu.experiments.Const.emptyRunnable,
        onError("writeClipboardText error: "));
  }

  private void findUsages(V2i eventPosition, ReferenceProvider.Provider provider) {
    popupMenu.hide();
    editor.findUsages(eventPosition, provider);
  }

  private void findUsagesDefDecl(V2i eventPosition, DefDeclProvider.Type type) {
    popupMenu.hide();
    Model model = editor().model();
    String language = model.language();
    String scheme = model.uriScheme();
    EditorRegistrations reg = editor().registrations;
    DefDeclProvider.Provider provider = type != null ? switch (type) {
      case DEF -> reg.findDefinitionProvider(language, scheme);
      case DECL -> reg.findDeclarationProvider(language, scheme);
    } : null;
    editor.findUsages(eventPosition, provider);
  }

  static Consumer<Throwable> onError(String s) {
    return throwable -> Debug.consoleInfo(s + throwable.getMessage());
  }

  void showOpenFilePicker() {
    api.window.showOpenFilePicker(editor::openFile);
  }

  @Override
  public void onResize(V2i newSize, double newDpr) {
    size.set(newSize);
    editor.setPos(editorPos, size, newDpr);

    if (dpr != newDpr) {
      dpr = newDpr;
      int toolbarFontSize = Numbers.iRnd(EditorConst.TOOLBAR_FONT_SIZE * newDpr);
      toolBarFont = g.fontDesk(EditorConst.TOOLBAR_FONT_NAME, toolbarFontSize);
      popupMenu.setTheme(toolBarFont, Colors.toolbarBg);
    }
    popupMenu.onResize(newSize, newDpr);
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
      popupMenu.hide();
      editor.onFocusLost();
    }

    @Override
    public boolean onMouseWheel(MouseEvent event, double dX, double dY) {
      return editor.onMouseWheel(event, dX, dY);
    }

    @Override
    public boolean onMousePress(MouseEvent event, int button, boolean press, int clickCount) {
      return popupMenu.onMousePress(event.position, button, press, clickCount)
          || editor.onMousePress(event, button, press, clickCount);
    }

    public boolean onContextMenu(MouseEvent event) {
      if (!popupMenu.isVisible()) {
        popupMenu.display(event.position, popupMenuContent(event.position), editor::onFocusGain);
        editor.onFocusLost();
      }
      return true;
    }

    @Override
    public boolean onMouseMove(MouseEvent event) {
      return popupMenu.onMouseMove(event.position, setCursor)
          || editor.onMouseMove(event, setCursor);
    }

    @Override
    public boolean onKey(KeyEvent event) {
      return handleKey(event) || editor.onKey(event);
    }

    private boolean handleKey(KeyEvent event) {
      if (!event.isPressed) return false;

      if (event.keyCode == KeyCode.ESC) {
        if (popupMenu.isVisible()) {
          popupMenu.hide();
          return true;
        }
      }

      if (event.ctrl && event.keyCode == KeyCode.O) {
        if (event.shift) {
          api.window.showDirectoryPicker(
              s -> Debug.consoleInfo("showDirectoryPicker -> " + s));
        } else {
          showOpenFilePicker();
        }
        return true;
      }
      return false;
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
